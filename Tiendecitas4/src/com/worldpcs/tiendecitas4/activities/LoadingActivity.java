package com.worldpcs.tiendecitas4.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.worldpcs.tiendecitas4.R;
import com.worldpcs.tiendecitas4.data.DataProvider;
import com.worldpcs.tiendecitas4.interfaces.DataUpdatedNotification;

public class LoadingActivity extends ActionBarActivity implements DataUpdatedNotification{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		getSupportActionBar().hide();
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		//Recuperamos los datos
		DataProvider.getInstance().updateAllData(this,false);
	}

	@Override
	public void notifyActiveDataUpdated(String tag) {
		Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
		
		//Cerramos esta actividad
		finish();
	}
}
