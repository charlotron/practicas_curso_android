package com.worldpcs.tiendecitas2.activities;

import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.worldpcs.tiendecitas2.R;
import com.worldpcs.tiendecitas2.dataSource.ShopDataProvider;
import com.worldpcs.tiendecitas2.fragments.ShopCommentFragment;

public class ShotActivity extends CommentsActionBarActivity {

	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shot);
		
		//Recuperamos los datos de la tienda
		HashMap<String,String> shop_data=ShopDataProvider.getInstance().getCurrentShop();
		id=ShopDataProvider.getInstance().getCurrentShopId();

		//Establecemos los valores a los textviews
		TextView shotTitle=	((TextView)findViewById(R.id.shotTitle));
		shotTitle.setText(shop_data.get(ShopDataProvider.SHOP_NAME));
		
		//Ponemos la imagen que toque
		ImageView image=(ImageView) findViewById(R.id.shotCurImg);
		image.setImageResource(getResources().getIdentifier(
                "img_"+id, "drawable", this.getPackageName())); 	
		
		//Recuperamos el action bar para insertarle la imagen del icono
		ActionBar ab=this.getSupportActionBar();
		//Usa los botones de la app para abrir y cerrar cosas
		ab.setDisplayHomeAsUpEnabled(true); 
		ab.setHomeButtonEnabled(true); 		
		
		//Cargamos el fragmento de los comentarios con el parametro indicado
		FragmentManager fm=getSupportFragmentManager();
		ShopCommentFragment f=new ShopCommentFragment();
		Bundle args=new Bundle();
		args.putString(CommentsActionBarActivity.TYPE, CommentsActionBarActivity.PHOTO);
		f.setArguments(args);
		fm.beginTransaction().add(R.id.shop_shot_comments_container, f).commit();
		
		//Ahora añadimos la función de enviar comentario
		ImageButton sendComment=((ImageButton)findViewById(R.id.sendShotCommentButton));
		sendComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((CommentsActionBarActivity) v.getContext()).sendComment();
			}
			
		});
		//Guardamos el tipo
		comment_type=CommentsActionBarActivity.PHOTO;
	}
	//Se le llama al pulsar en una opción del menú
	public boolean onOptionsItemSelected(MenuItem menu){
		//Esto ocurre cuando se pulsa sobre el botón de la imagen de la app
		switch(menu.getItemId()){
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this); //Volver
				break;
			case R.id.action_favourite:
				Toast.makeText(this, getResources().getString(R.string.favourite_opt), Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.activity_shop_shot, menu);
		return true;
	}
	//Comparte una image de drawable
	public void shareImage(){
		Uri imageUri = Uri.parse("android.resource://com.worldpcs.tiendecitas2/drawable/img_"+id);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		
		intent.putExtra(Intent.EXTRA_STREAM, imageUri);
		startActivity(Intent.createChooser(intent , "Share"));
		
	}

}
