package com.worldpcs.tiendecitas3.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.data.DataProvider;
import com.worldpcs.tiendecitas3.fragments.CommunityFragment;
import com.worldpcs.tiendecitas3.fragments.GalleryFragment;
import com.worldpcs.tiendecitas3.fragments.MapFragment;
import com.worldpcs.tiendecitas3.fragments.StoreListFragment;
import com.worldpcs.tiendecitas3.handlers.DrawerHandler;
import com.worldpcs.tiendecitas3.handlers.FragmentNavigationHandler;
import com.worldpcs.tiendecitas3.handlers.HomeButtonHandler;
import com.worldpcs.tiendecitas3.handlers.TabsHandler;
import com.worldpcs.tiendecitas3.interfaces.DataUpdatedNotification;
import com.worldpcs.tiendecitas3.interfaces.FragmentNavigationHandlerNotification;

public class MainActivity extends ActionBarActivity implements FragmentNavigationHandlerNotification, DataUpdatedNotification {

	/**
	 * Id de notificación
	 */
	private static final int NOTIFICATION_DATA_UPLOADED = 52;
	public static String GALLERY;
	public static String SHOP_LIST;
	public static String COMMUNITY;
	public static String MAP;
	/**
	 * Default fragment
	 */
	private static String FIRST_FRAGMENT;
	/**
	 * List of fragments
	 */
	private HashMap<String,Fragment> fragments=null;


	/**
	 * Controla nuestro drawer
	 */
	private DrawerHandler drawerHandler=null;
	/**
	 * Controla nuestras tabs
	 */
	private TabsHandler tabsHandler=null;
	/**
	 * Queue para volley
	 */
	public static RequestQueue queue=null;
	/**
	 * Como no tenemos algunos datos es necesario retrasar la construccion a la recuperación de datos
	 */
	private boolean pendingCreateActions=true;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//Recuperamos los datos
		DataProvider.getInstance().updateAllData(this);

	}
	/**
	 * Esto se ejecuta cuando los datos se hayan cargado
	 */
	public void onPostCreate(){

		//Generamos la queue de Volley
		queue=Volley.newRequestQueue(this);

		//Cargamos las Strings de las pseudo-constantes
		GALLERY=this.getResources().getString(R.string.gallery_fragment_key);
		SHOP_LIST=this.getResources().getString(R.string.store_fragment_key);
		COMMUNITY=this.getResources().getString(R.string.community_fragment_key);
		MAP=this.getResources().getString(R.string.map_fragment_key);

		//Asignamos la de inicio
		FIRST_FRAGMENT=GALLERY;

		setContentView(R.layout.activity_main);


		//Creo una lista de Fragmentos para el Drawer
		fragments=new HashMap<String,Fragment>();
		HashMap<String,Fragment> list_drawer=new HashMap<String,Fragment>();
		HashMap<String,Fragment> list_tabs=new HashMap<String,Fragment>();

		//Rellenamos las listas
		fragments.put(GALLERY,		new GalleryFragment());
		fragments.put(SHOP_LIST,	new StoreListFragment());
		fragments.put(COMMUNITY,	new CommunityFragment());
		fragments.put(MAP,			new MapFragment());

		//Rellenamos el resto de listas
		String[] drawer_keys = new String[]{GALLERY,SHOP_LIST,COMMUNITY};
		for(int i = 0;i<drawer_keys.length;i++)
			list_drawer.put(drawer_keys[i],	fragments.get(drawer_keys[i]));

		String[] tab_keys = new String[]{SHOP_LIST,MAP};
		for(int i = 0;i<tab_keys.length;i++)
			list_tabs.put(tab_keys[i],	fragments.get(tab_keys[i]));

		//Creamos un FragmentNavigationHandler
		FragmentNavigationHandler fnh = new FragmentNavigationHandler(this);

		Set<String> keys = this.fragments.keySet();
		for(Iterator<String> i = keys.iterator();i.hasNext();){
			String key=i.next();
			Fragment frag=fragments.get(key);
			fnh.addFragment(	R.id.main_container,
								key,
								frag,
								key.equals(FIRST_FRAGMENT));
		}



		//Creo mi drawer handler
		drawerHandler=new DrawerHandler(	this,
											R.id.drawer_layout,
											R.id.drawer_list,
											R.layout.drawer_list_item,
											drawer_keys,
											true,
											list_drawer,
											fnh,
											R.id.main_container);

		//Creamos el toggle
		drawerHandler.setDrawerToggle(R.drawable.ic_launcher);

		//Creamos el botón de inicio del actionbar
		HomeButtonHandler.configureHomeButton(this);



		//Creamos las tabs
		tabsHandler=new TabsHandler(	this,
										tab_keys,
										true,
										list_tabs,
										fnh,
										R.id.main_container);
	}

	@Override
	//Este evento más la config del manifest provoca que controle yo manualmente los giros y evitar aberraciones a la hora de cambiar orientaciones por construcciones sucesivas
    public void onConfigurationChanged(Configuration newConfig) {

      super.onConfigurationChanged(newConfig);

     /* if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      }
      else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

      }*/
    }
	/**
	 * Este evento se llama al cambiar de fragmento
	 * Si en el fragmento indicado hay tabs las muestra
	 */
	@Override
	public void notifyActiveFragment(int container_dest_resource_id, String key, Fragment frag) {
		if(container_dest_resource_id==R.id.main_container)
			tabsHandler.showTabs(tabsHandler.hasTab(key));

	}
	/**
	 * Se le llama al pulsar en una opción del menú
	 */
	public boolean onOptionsItemSelected(MenuItem menu){
		//Esto ocurre cuando se pulsa sobre el botón de la imagen de la app
		if(menu.getItemId()==android.R.id.home){
			this.drawerHandler.toggleOpenCloseDrawer();
		}

		return super.onOptionsItemSelected(menu);

	}
	/**
	 * Genera el menú
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	/**
	 * Es llamado por DataProvider
	 */
	@Override
	public void notifyActiveDataUpdated(String tag) {
		if(pendingCreateActions){
			onPostCreate();
			pendingCreateActions=false;
		}
		else{
			((CommunityFragment) fragments.get(COMMUNITY)).updateAdapter();
			notificationDataUploaded();
		}
	}
	/**
	 * Muestra la notificación
	 */
	public void notificationDataUploaded() {
         NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this);
         mBuilder.setSmallIcon(R.drawable.ic_launcher);
         mBuilder.setContentTitle(getResources().getString(R.string.notification_title));
         mBuilder.setContentText(getResources().getString(R.string.notification_desc));
                        
         NotificationManager mNotificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

         mNotificationManager.notify(NOTIFICATION_DATA_UPLOADED, mBuilder.build());        
	}
}