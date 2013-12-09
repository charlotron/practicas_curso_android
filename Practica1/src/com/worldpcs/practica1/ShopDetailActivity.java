package com.worldpcs.practica1;

import java.util.HashMap;

import com.worldpcs.practica1.dataSource.ShopDataProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ShopDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail);

		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	
		//Recuperamos los datos de la tienda
		HashMap<String,String> shop_data=ShopDataProvider.getInstance().getCurrentShop();
		
		//Establecemos los valores a los textviews
		TextView descTitleShop=	((TextView)findViewById(R.id.descTitleShop));
		TextView descDirShop=	((TextView)findViewById(R.id.descDirShop));
		TextView descTlfShop=	((TextView)findViewById(R.id.descTlfShop));
		TextView descTimes=		((TextView)findViewById(R.id.descTimes));
		TextView descWeb=		((TextView)findViewById(R.id.descWeb));
		TextView descEmail=		((TextView)findViewById(R.id.descEmail));
		
		
		descTitleShop.setText(shop_data.get(ShopDataProvider.SHOP_NAME));
		descDirShop.setText(shop_data.get(ShopDataProvider.SHOP_DIR));
		descTlfShop.setText(shop_data.get(ShopDataProvider.SHOP_TLF));
		descTimes.setText(shop_data.get(ShopDataProvider.SHOP_TIME));
		descWeb.setText(shop_data.get(ShopDataProvider.SHOP_WEB));
		descEmail.setText(shop_data.get(ShopDataProvider.SHOP_EMAIL));
		
		//Lo hacemos linkeable
		Linkify.addLinks(descTlfShop,Linkify.PHONE_NUMBERS);
		Linkify.addLinks(descWeb,Linkify.WEB_URLS);
		Linkify.addLinks(descEmail,Linkify.EMAIL_ADDRESSES);
		
		//Creamos el evento de llamar
		findViewById(R.id.callButton).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String tlf = ((TextView) findViewById(R.id.descTlfShop)).getText().toString();
				Toast.makeText(v.getContext(), tlf, Toast.LENGTH_SHORT).show();
				tlf="tel:"+tlf;
				Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse(tlf));
				startActivity(callIntent);
			}
			
		});
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shop_detail, menu);
		return true;
	}
}
