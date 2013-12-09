package com.worldpcs.practica1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.worldpcs.practica1.dataSource.ShopDataProvider;

public class MainActivity extends ListActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		
		setContentView(R.layout.activity_main);
		
		//Creamos el adaptador
		SimpleAdapter adapter = new SimpleAdapter(	this,
													ShopDataProvider.getInstance().getShop_data(),
													android.R.layout.simple_list_item_2,
													new String[]{ShopDataProvider.SHOP_NAME,ShopDataProvider.SHOP_DESC},
													new int[]{	android.R.id.text1,
																android.R.id.text2});
		//Lo establecemos como el definido
		setListAdapter(adapter);
		//Actualizamos los cambios en el adaptador
		//adapter.notifyDataSetChanged();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ShopDataProvider.getInstance().setCurrentShopId(position);
        Intent intent = new Intent(this,ShopDetailActivity.class);
        startActivity(intent);
    }
}
