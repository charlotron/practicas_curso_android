package com.worldpcs.tiendecitas3.activities;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.data.DataProvider;
import com.worldpcs.tiendecitas3.data.pojo.Comment;
import com.worldpcs.tiendecitas3.data.pojo.Store;
import com.worldpcs.tiendecitas3.interfaces.DataSentNotification;
import com.worldpcs.tiendecitas3.interfaces.DataUpdatedNotification;
import com.worldpcs.tiendecitas3.util.BitmapLRUCache;

public abstract class CommonStoreActivity extends ActionBarActivity implements DataUpdatedNotification,DataSentNotification{

	public static final String DETAIL = "detail";
	public static final String PHOTO = 	"photo";
	public static final String TYPE = "type";

	protected String comment_type=null;
	private RequestQueue queue;
	protected ImageLoader imageLoader;
	protected EditText et;

	public CommonStoreActivity() {
		super();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//Generamos los datos para volley y esta actividad
		queue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(	queue,
										new BitmapLRUCache());
	}


	//Env�a un comentario
	protected void sendComment() {
		//Recupero datos de la tienda
		Store store=DataProvider.getInstance().getCurrentStore();
		//Recupero el mensaje
		String msg=et.getText().toString();
		if(msg.trim().equals("")) return;

		if(comment_type==DETAIL)
			DataProvider.getInstance().addStoreComment(store.getId(),msg,this);
		else
			DataProvider.getInstance().addStorePhotoComment(store.getId(),store.getPhotosList().get(0).getId(),msg,this);


	}
	/**
	 * Se le llama al acabar de actualizar los datos
	 */
	public void notifyActiveDataSent(String tag){
		et.setText(null);
	}
	/**
	 * Se le llama al acabar de actualizar los datos
	 */
	public void notifyActiveDataUpdated(String tag){
		setCommentAdapter();
		updateFavCount();
	}

	/**
	 * Crea el adapter de comentarios
	 */
	public void setCommentAdapter(){
		//Ya sabemos el tipo, recuperamos el array de comentarios
		ArrayList<Comment> comments=null;
		//FrameLayout container=null;
		if(comment_type==DETAIL){
			comments=DataProvider.getInstance().getCurrentStore().getComments();
			//container=(FrameLayout) findViewById(R.id.store_detail_comments_container);
		}
		else{
			comments=DataProvider.getInstance().getCurrentStore().getPhotosList().get(0).getComments();
			//container=(FrameLayout) getActivity().findViewById(R.id.store_photo_comments_container);
		}
		ArrayList<String> string_comments = new ArrayList<String>();
		//Tenemos que convertirlo
		for(Iterator<Comment> it=comments.iterator();it.hasNext();)
			string_comments.add(it.next().toString());
		
		//Creamos el adaptador
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(	this,
																R.layout.fragment_store_comment_item,
																string_comments);

		//Lo establecemos como el definido
		ListView lv=((ListView) findViewById(R.id.comment_list));
		lv.setAdapter(adapter);
		//Estu suple un problema de poner un ListView dentro de un ScrollView
		setListViewHeightBasedOnChildren(lv);
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
	/**
	 * Actualiza el bot�n de Favoritos
	 */
	public void updateFavCount() {
		//Tal y como est�n hechos los men�s hay que volverlo a generar
		supportInvalidateOptionsMenu();
	}
	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		super.onPrepareOptionsPanel(view, menu);
		updateFavCount(menu);
		return true;
	}


	protected abstract void updateFavCount(Menu menu);


}
