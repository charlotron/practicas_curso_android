package com.worldpcs.tiendecitas3.activities;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.data.DataProvider;
import com.worldpcs.tiendecitas3.data.pojo.Store;
import com.worldpcs.tiendecitas3.fragments.StoreCommentFragment;
import com.worldpcs.tiendecitas3.util.FileUtils;

public class ShotActivity extends CommonStoreActivity {

	private NetworkImageView shot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shot);

		//Recuperamos los datos de la tienda
		Store store=DataProvider.getInstance().getCurrentStore();		
		
		//Establecemos los valores a los textviews
		TextView shotTitle=	((TextView)findViewById(R.id.shotTitle));
		shotTitle.setText(store.getName());

		//Ponemos la imagen que toque
		shot=	((NetworkImageView)findViewById(R.id.shotCurImg));
		//Le establecemos la url
		String url=store.getPhotosList().get(0).getURL();
		shot.setDefaultImageResId(R.drawable.ic_photo);
		shot.setImageUrl(		url,
								imageLoader);
		
		//Cargamos el fragmento de los comentarios con el parametro indicado
		FragmentManager fm=getSupportFragmentManager();
		StoreCommentFragment f=new StoreCommentFragment();
		Bundle args=new Bundle();
		args.putString(CommonStoreActivity.TYPE, CommonStoreActivity.PHOTO);
		f.setArguments(args);
		fm.beginTransaction().add(R.id.store_shot_comments_container, f).commit();
		fm.executePendingTransactions();

		//Ahora añadimos la función de enviar comentario
		ImageButton sendComment=((ImageButton)findViewById(R.id.sendShotCommentButton));
		sendComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((CommonStoreActivity) v.getContext()).sendComment();
			}

		});
		//Guardamos el tipo
		comment_type=CommonStoreActivity.PHOTO;
		//Guardamos el hueco de envío
		et=(EditText)findViewById(R.id.sendShotComment);			

	}

	//Se le llama al pulsar en una opción del menú
	public boolean onOptionsItemSelected(MenuItem menu){
		//Esto ocurre cuando se pulsa sobre el botón de la imagen de la app
		switch(menu.getItemId()){
			case R.id.action_favourite:
				//Toast.makeText(this, getResources().getString(R.string.favourite_opt), Toast.LENGTH_SHORT).show();
				DataProvider.getInstance().incStorePhotoFavCount(DataProvider.getInstance().getCurrentStoreId(),DataProvider.getInstance().getCurrentStore().getPhotosList().get(0).getId(), this);
				break;
			case R.id.action_share:
				shareImage();
				break;
		}
		return super.onOptionsItemSelected(menu);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_store_shot, menu);
		
		updateFavCount(menu);

		return true;
	}
	/**
	 * Actualiza el botón de Favoritos
	 */
	protected void updateFavCount(Menu menu) {
		//Actualizamos 
		MenuItem favourites_button = menu.findItem(R.id.action_favourite);
		favourites_button.setTitle("x"+DataProvider.getInstance().getCurrentStore().getPhotosList().get(0).getFav_count());
	}	
	
	//Comparte una image de drawable
	public void shareImage(){
		//Recuperamos la imagen del imageview
		Drawable drawable = shot.getDrawable();
		BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
		Bitmap bitmap = bitmapDrawable .getBitmap();
		try{
			File savedimage=FileUtils.SaveImage(bitmap);
			Uri imageUri = Uri.fromFile(savedimage);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
	
			intent.putExtra(Intent.EXTRA_STREAM, imageUri);
			startActivity(Intent.createChooser(intent , "Share"));
		}
		catch(IOException e){
			Log.e("SHARE SHOT", e.getMessage());
			Toast.makeText(this, "Hubo un error compartiendo la imagen", Toast.LENGTH_LONG).show();
		}

	}

}
