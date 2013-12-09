package com.worldpcs.tiendecitas2.activities;

import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.worldpcs.tiendecitas2.R;
import com.worldpcs.tiendecitas2.dataSource.ShopDataProvider;
import com.worldpcs.tiendecitas2.fragments.ShopCommentFragment;

public class ShopDetailActivity extends CommentsActionBarActivity {

	private String share_txt=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);

		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	
		//Recuperamos los datos de la tienda
		HashMap<String,String> shop_data=ShopDataProvider.getInstance().getCurrentShop();
		int id=ShopDataProvider.getInstance().getCurrentShopId();
		
		//Establecemos los valores a los textviews
		TextView descTitleShop=	((TextView)findViewById(R.id.descTitleShop));
		TextView descDescShop=	((TextView)findViewById(R.id.descDescShop));
		TextView descDirShop=	((TextView)findViewById(R.id.descDirShop));
		TextView descTlfShop=	((TextView)findViewById(R.id.descTlfShop));
		TextView descTimes=		((TextView)findViewById(R.id.descTimes));
		TextView descWeb=		((TextView)findViewById(R.id.descWeb));
		TextView descEmail=		((TextView)findViewById(R.id.descEmail));
		
		
		descTitleShop.setText(shop_data.get(ShopDataProvider.SHOP_NAME));
		descDescShop.setText(shop_data.get(ShopDataProvider.SHOP_DESC));
		descDirShop.setText(shop_data.get(ShopDataProvider.SHOP_DIR));
		descTlfShop.setText(shop_data.get(ShopDataProvider.SHOP_TLF));
		descTimes.setText(shop_data.get(ShopDataProvider.SHOP_TIME));
		descWeb.setText(shop_data.get(ShopDataProvider.SHOP_WEB));
		descEmail.setText(shop_data.get(ShopDataProvider.SHOP_EMAIL));
		
		//Cambiamos la imagen pequeña por una previsualización de la imagen
		ImageButton galButton=	((ImageButton)findViewById(R.id.galButton));
		galButton.setImageResource(getResources().getIdentifier(
                "img_"+id, "drawable", this.getPackageName())); 		
		
		
		//Generamos el share_txt por si lo queremos compartir
		share_txt=getString(	R.string.share_shop_txt,
								shop_data.get(ShopDataProvider.SHOP_NAME),
								shop_data.get(ShopDataProvider.SHOP_DESC),
								shop_data.get(ShopDataProvider.SHOP_WEB),
								shop_data.get(ShopDataProvider.SHOP_TLF),
								shop_data.get(ShopDataProvider.SHOP_EMAIL)
								);
		
		//Lo hacemos linkeable
		Linkify.addLinks(descTlfShop,Linkify.PHONE_NUMBERS);
		Linkify.addLinks(descWeb,Linkify.WEB_URLS);
		Linkify.addLinks(descEmail,Linkify.EMAIL_ADDRESSES);
		
		//Creamos el evento de llamar
		findViewById(R.id.callButton).setOnClickListener(new OnCallClickListener(this));
		
		//Recuperamos el action bar para insertarle la imagen del icono
		ActionBar ab=this.getSupportActionBar();
		//Usa los botones de la app para abrir y cerrar cosas
		ab.setDisplayHomeAsUpEnabled(true); 
		ab.setHomeButtonEnabled(true); 
		
		
		//Creamos el evento de hacer click en el icono de la galería
		findViewById(R.id.galButton).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int id=ShopDataProvider.getInstance().getCurrentShopId();
				
				Intent intent = new Intent(v.getContext(),ShotActivity.class);
		        intent.putExtra(ShopDataProvider.ID,id);
		        startActivity(intent);
				
			}       
	    });
		
		//Cargamos el fragmento de los comentarios con el parametro indicado
		FragmentManager fm=getSupportFragmentManager();
		ShopCommentFragment f=new ShopCommentFragment();
		Bundle args=new Bundle();
		args.putString(CommentsActionBarActivity.TYPE, CommentsActionBarActivity.DETAIL);
		f.setArguments(args);
		fm.beginTransaction().add(R.id.shop_detail_comments_container, f).commit();
		
		//Ahora añadimos la función de enviar comentario
		ImageButton sendComment=((ImageButton)findViewById(R.id.sendDetailCommentButton));
		sendComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((CommentsActionBarActivity) v.getContext()).sendComment();
			}
			
		});
		//Guardamos el tipo
		comment_type=CommentsActionBarActivity.DETAIL;
		
	}

	public void callShop(){
		String tlf = ((TextView) findViewById(R.id.descTlfShop)).getText().toString();
		Toast.makeText(this, tlf, Toast.LENGTH_SHORT).show();
		tlf="tel:"+tlf;
		Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse(tlf));
		startActivity(callIntent);		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_shop_detail, menu);
		return true;
	}
	//Clase que implementa el click en el botón de llamada
	private class OnCallClickListener implements OnClickListener{
			ShopDetailActivity instance=null;


			public OnCallClickListener(ShopDetailActivity instance) {
				super();
				this.instance = instance;
			}

			@Override
			public void onClick(View v) {
				instance.callShop();				
		    }
			
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
				//Share current text
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT,share_txt);
				startActivity(intent);
				break;
			case R.id.action_call:
				//llamar
				callShop();
				break;
		}
		return super.onOptionsItemSelected(menu);
		
	}
}
