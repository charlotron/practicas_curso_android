package com.worldpcs.practica1;

import java.util.HashMap;

import com.worldpcs.practica1.dataSource.ShopDataProvider;

import android.os.Bundle;
import android.app.Activity;
import android.text.util.Linkify;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ShotActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shot);
		
		//Recuperamos los datos de la tienda
		HashMap<String,String> shop_data=ShopDataProvider.getInstance().getCurrentShop();
		
		//Establecemos los valores a los textviews
		TextView shotTitle=	((TextView)findViewById(R.id.shotTitle));
		TextView shotDesc=	((TextView)findViewById(R.id.shotDesc));	
		
		shotTitle.setText(shop_data.get(ShopDataProvider.SHOP_NAME));
		shotDesc.setText(shop_data.get(ShopDataProvider.SHOP_SHOT));
		
		//Lo hacemos linkeable
		Linkify.addLinks(shotDesc,Linkify.ALL);
		
		//Ponemos la imagen que toque
		ImageView image=(ImageView) findViewById(R.id.shotCurImg);
		image.setImageResource(getResources().getIdentifier(
                "img_"+ShopDataProvider.getInstance().getCurrentShopId(), "drawable", "com.worldpcs.practica1")); 		
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shot, menu);
		return true;
	}

}
