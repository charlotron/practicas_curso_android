package com.worldpcs.tiendecitas4.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.worldpcs.tiendecitas4.R;
import com.worldpcs.tiendecitas4.fragments.CommunityFragment;
import com.worldpcs.tiendecitas4.fragments.GalleryFragment;
import com.worldpcs.tiendecitas4.fragments.MapFragment;
import com.worldpcs.tiendecitas4.fragments.StoreListFragment;
import com.worldpcs.tiendecitas4.handlers.DrawerHandler;
import com.worldpcs.tiendecitas4.handlers.FragmentNavigationHandler;
import com.worldpcs.tiendecitas4.handlers.HomeButtonHandler;
import com.worldpcs.tiendecitas4.handlers.TabsHandler;
import com.worldpcs.tiendecitas4.interfaces.DataUpdatedNotification;
import com.worldpcs.tiendecitas4.interfaces.FragmentNavigationHandlerNotification;

public class MainActivity extends ActionBarActivity implements	FragmentNavigationHandlerNotification, 
																DataUpdatedNotification,
																ConnectionCallbacks,
																OnConnectionFailedListener,
																LocationListener{

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
	 * Default fragment
	 */
	private String cur_fragment=null;

	/**
	 * Controla nuestro drawer
	 */
	private DrawerHandler drawerHandler=null;
	/**
	 * Controla nuestras tabs
	 */
	private TabsHandler tabsHandler=null;
	/**
	 * Inicia un intent con id CONNECTION_FAILURE_RESOLUTION_REQUEST
	 */
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 5564;
	/**
	 * Maneja la posición actual
	 */
	private LocationClient locationClient=null;
	/**
	 * Solicita las actualizaciones d mapa
	 */
	private LocationRequest locationRequest;
	/**
	 * Milis por cada sec
	 */
	public static final int MILLISECONDS_PER_SECOND = 1000;
	/**
	 * Refresco de posición actual (5sec)
	 */
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * 5;
	/**
	 * Refresco más rápido (1sec)
	 */
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * 1;
	
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//Inicializamos, cliente de pos actual + config de solicitudes de refresco de ubicación
		locationClient=new LocationClient(this, this, this);
        locationRequest = LocationRequest.create();
		
		locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);		

		//Cargamos las Strings de las pseudo-constantes
		GALLERY=this.getResources().getString(R.string.gallery_fragment_key);
		SHOP_LIST=this.getResources().getString(R.string.store_fragment_key);
		COMMUNITY=this.getResources().getString(R.string.community_fragment_key);
		MAP=this.getResources().getString(R.string.map_fragment_key);

		//Asignamos la de inicio
		cur_fragment=FIRST_FRAGMENT=GALLERY;

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
	@SuppressLint("NewApi")
	@Override
	public void notifyActiveFragment(int container_dest_resource_id, String cur_fragment, Fragment frag) {
		if(container_dest_resource_id==R.id.main_container)
			tabsHandler.showTabs(tabsHandler.hasTab(cur_fragment));
		Boolean updateMenu=	(this.cur_fragment!=MAP && cur_fragment==MAP) ||
							(this.cur_fragment==MAP && cur_fragment!=MAP);
		this.cur_fragment=cur_fragment;
		if(updateMenu)
			invalidateOptionsMenu();
	}
	/**
	 * Se le llama al pulsar en una opción del menú
	 */
	public boolean onOptionsItemSelected(MenuItem menu){
		//Esto ocurre cuando se pulsa sobre el botón de la imagen de la app
		int menuItemId = menu.getItemId();
		switch(menuItemId){
			case android.R.id.home:
				this.drawerHandler.toggleOpenCloseDrawer();
				break;
			case R.id.set_map_hibrid:
			case R.id.set_map_normal:
			case R.id.set_map_satellite:
			case R.id.set_map_terrain:
				if(cur_fragment==MAP){
					((MapFragment) fragments.get(MAP)).setMapType(menuItemId);
				}
		}
		

		return super.onOptionsItemSelected(menu);

	}
	/**
	 * Genera el menú
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(cur_fragment==MAP)
			getMenuInflater().inflate(R.menu.fragment_map_menu, menu);
		else
			getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	/**
	 * Es llamado por DataProvider
	 */
	@Override
	public void notifyActiveDataUpdated(String tag) {
		if(cur_fragment==COMMUNITY){
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
	/**
	 * Usado en la ubicación, se llama al conectarse - tener ubicación
	 */
	@Override
	public void onConnected(Bundle arg) {
		//Actualizamos la posición, manualmente
		onLocationChanged(locationClient.getLastLocation());
		//Registramos los listener y los datos de actu
		locationClient.requestLocationUpdates(locationRequest, this);
	}
	/**
	 * Usado en la ubicación, se llama al conectarse - perder ubicación
	 */
	@Override
	public void onDisconnected() {
		//Quitamos datos de actu
		locationClient.removeLocationUpdates(this);
	}
	/**
	 * Usado en la ubicación, al iniciar la actividad (temas de batería)
	 */
	@Override
	protected void onStart() {
		super.onStart();
		locationClient.connect();
	}
	/**
	 * Usado en la ubicación, al finalizar la actividad (temas de batería)
	 */
	@Override
	protected void onStop() {
		super.onStop();
		locationClient.disconnect();		
	}
	/**
	 * Usado en la ubicación
	 */
	@Override
	public void onConnectionFailed(ConnectionResult res) {
		if(res.hasResolution()){
			try {
                res.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } 
			catch (IntentSender.SendIntentException e) {
                    Log.e("ERROR MAPA CONNECTION FAILED",Log.getStackTraceString(e));
			}                    
		}
		else{
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
										res.getErrorCode(),
										this,
										CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (errorDialog != null) 
				errorDialog.show();			
		}
		
	}
	@Override
	public void onLocationChanged(Location loc) {
		//Hace lo que toque
		
	}	
}