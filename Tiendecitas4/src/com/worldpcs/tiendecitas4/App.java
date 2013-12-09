package com.worldpcs.tiendecitas4;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.worldpcs.tiendecitas4.data.DBAdapter;
import com.worldpcs.tiendecitas4.util.BitmapLRUCache;
/**
 * Clase central de la app
 * @author Xarly
 *
 */
public class App extends Application{
	/**
	 * Queue para volley
	 */
	public RequestQueue queue=null;
	/**
	 * Imageloader
	 */
	public ImageLoader imageLoader=null;
	/**
	 * Base de datos
	 */
	private DBAdapter db=null;
	/**
	 * Instancia
	 */
	private static App instance=null;
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Esta instancia
		instance=this;
		
		//Generamos la base de datos
		db = new DBAdapter(getApplicationContext());    
		
		//Generamos la queue de Volley
		queue=Volley.newRequestQueue(this);
			
		//Generamos el loader para volley
		imageLoader=new ImageLoader(	queue, 
										new BitmapLRUCache());		
	}

	/**
	 * DB
	 * @return
	 */
	public DBAdapter getDb() {
		return db;
	}
	/**
	 * Instancia
	 * @return
	 */
	public static App getInstance() {
		return instance;
	}
	
	
	
}
