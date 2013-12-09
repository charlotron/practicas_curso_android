package com.worldpcs.tiendecitas3.fragments;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.data.DataProvider;
import com.worldpcs.tiendecitas3.data.adapters.CommunityListAdapter;
import com.worldpcs.tiendecitas3.data.pojo.Photo;
import com.worldpcs.tiendecitas3.util.FileUtils;

public class CommunityFragment extends Fragment {
	public static final int LOAD_IMAGE=1;
	public static final int CAPTURE_IMAGE=2;

	private ListView lv;
	private View v;
	private File captured_image=null;
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lv.setAdapter(new CommunityListAdapter(getActivity()));
		
		//Vamos a asignarle una tarea a la cámara
		ImageButton ib = ((ImageButton) v.findViewById(R.id.ButtonMakePhoto));
		ib.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			final String[] items=getActivity().getResources().getStringArray(R.array.make_photo_options);
			builder.setTitle(getActivity().getResources().getString(R.string.make_photo_options_title)).setItems(items, new DialogInterface.OnClickListener() {
				

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = null;
					int code = 0;
					switch(which){
					case 0:
						i = new Intent(	MediaStore.ACTION_IMAGE_CAPTURE);
						captured_image=FileUtils.createTempFile("jpg");
						i.putExtra(	android.provider.MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(captured_image));
						code=CAPTURE_IMAGE;
						break;
					case 1:
						i = new Intent(	Intent.ACTION_PICK, 
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						code=LOAD_IMAGE;
						break;
					}
					dialog.dismiss();
					startActivityForResult(i, code);
				}
			});
			builder.show();
		}
			
		});
	}
	/**
	 * Notifica cambios al adapter
	 */
	@SuppressWarnings("unchecked")
	public void updateAdapter(){
		((ArrayAdapter<Photo>) lv.getAdapter()).notifyDataSetChanged();		
		lv.setSelection(lv.getChildCount()-1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		boolean sendPhoto=false;
		if (resultCode == Activity.RESULT_OK) {
			String picturePath = "";
			switch(requestCode){
				case CAPTURE_IMAGE:
					sendPhoto=true;
					//Recuperamos la ruta del archivo generado antes
					picturePath=captured_image.getPath();
					break;
				case LOAD_IMAGE:
					if(data!=null){
						sendPhoto=true;
						//Recuperamos la url de la imagen
						Uri selectedImage = data.getData();
			            String[] filePathColumn = { MediaStore.Images.Media.DATA };
			 
			            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			            cursor.moveToFirst();
			 
			            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			            picturePath = cursor.getString(columnIndex);
			            cursor.close(); 
					}
					break;
			}
			if(sendPhoto)
				//Lo guardamos en nuestro backend
				DataProvider.getInstance().addCommunityPhoto(getActivity(),picturePath);
        }
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState){
		//Genero el viewPager
		v = inflater.inflate(R.layout.fragment_community_listview,container,false);
		lv=(ListView) v.findViewById(R.id.community_list);
		return v;
	}
}
