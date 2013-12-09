package com.worldpcs.tiendecitas2.activities;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.worldpcs.tiendecitas2.R;
import com.worldpcs.tiendecitas2.fragments.CommunityFragment;
import com.worldpcs.tiendecitas2.fragments.GalleryFragment;
import com.worldpcs.tiendecitas2.fragments.MapFragment;
import com.worldpcs.tiendecitas2.fragments.ShopListFragment;

public class MainActivity extends ActionBarActivity {

	public static final int GALLERY=0;
	public static final int SHOP_LIST=1;
	public static final int COMUNITY=2;
	public static final int MAP=3;
	
	private static final int FIRST_FRAGMENT=GALLERY;
	
	private DrawerLayout drawerLayout=null;
	private ListView drawerList=null;
	
	private String[] options=null;
	private android.support.v4.app.FragmentManager fm=null;
	              
	private ArrayList<Fragment> frags=null;
	private ActionBarDrawerToggle drawerToggle=null;
	private String[] tabs_strings;
	
	private int currentFragment=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//Recuperamos los datos necesarios
		drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList=(ListView)findViewById(R.id.drawer_list);
		
		//Creamos los fragmentos que utilizaremos
		frags=new ArrayList<Fragment>();
		frags.add(GALLERY,new GalleryFragment());
		frags.add(SHOP_LIST,new ShopListFragment());
		frags.add(COMUNITY,new CommunityFragment());
		frags.add(MAP,new MapFragment());
		
		
		
		options= getResources().getStringArray(R.array.drawer_options);
		
		//Ahora creamos los elementos del menú en base al array con un arrayAdapter
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(	this, 
																R.layout.drawer_list_item,
																options);
		drawerList.setAdapter(adapter);
		
		drawerList.setItemChecked(0, true); //La primera opción es la que se muestra
		drawerList.setOnItemClickListener(new ItemClickListener());
		//Botón que alterna apertura y cierre del drawer (OJO ESTO IMPOSIBLE DE DEDUCIR)
		drawerToggle=new ActionBarDrawerToggle(	this, 
												drawerLayout, 
												R.drawable.ic_launcher,
												R.string.toggle_open,
												R.string.toggle_close){
			public void onDrawerOpened(View v){
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
			public void onDrawerClosed(View v){
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		
		//Recuperamos el action bar para insertarle la imagen del icono
		ActionBar ab=this.getSupportActionBar();
		//Usa los botones de la app para abrir y cerrar cosas
		ab.setDisplayHomeAsUpEnabled(true); 
		ab.setHomeButtonEnabled(true); 
		
		//Guardamos el fragment manager
		fm=getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		
		//Incorporamos los fragmentos 
		for(Iterator<Fragment> it=frags.iterator();it.hasNext();)
			ft.add(R.id.main_container, it.next());
		
		//Hacemos visible el primero sólo
		setActiveFragment(FIRST_FRAGMENT,ft);
	}
	@Override
	//Este evento más la config del manifest provoca que controle yo manualmente los giros
    public void onConfigurationChanged(Configuration newConfig) {

      super.onConfigurationChanged(newConfig);

     /* if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      } 
      else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

      }*/
    }
	
	//Muestra el fragmento adecuado según la opción del drawer que se elija
	public void setActiveFragment(int fragmentNum) {
		FragmentTransaction ft=fm.beginTransaction();
		setActiveFragment(fragmentNum, ft);	
	}
	public void setActiveFragment(int fragmentNum,	FragmentTransaction curTransaction)	{
		if(fragmentNum==currentFragment) return;
		currentFragment=fragmentNum;
		
		ActionBar actionbar=this.getSupportActionBar();
		
		for(int i=0;i<frags.size();i++){
			Fragment cur=frags.get(i);
			if(i==fragmentNum)
				curTransaction.show(cur);
			else
				curTransaction.hide(cur);
		}
		//Vemos si tenemos que mostrar la navegación por tabs o no
		if(fragmentNum==SHOP_LIST || fragmentNum==MAP){
			//Ponemos la navegación a tabs
			actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			//Comprobamos si están definidos los tabs
			if(actionbar.getTabCount()==0){
				//Recuperamos de strings nuestro array
				tabs_strings=getResources().getStringArray(R.array.tab_options);
				for(int i=0;i<tabs_strings.length;i++)
					actionbar.addTab(
							actionbar.newTab()
								.setText(tabs_strings[i])
								.setTabListener(new NavigationBarTabListener(this)));
			}
		}
		else
			actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		curTransaction.commit();
		//Cambiamos el título de la app 1
		if(fragmentNum>=0 && fragmentNum<options.length)
			actionbar.setTitle(options[fragmentNum]);

	}
	//Se le llama al pulsar en una opción del menú
	public boolean onOptionsItemSelected(MenuItem menu){
		//Esto ocurre cuando se pulsa sobre el botón de la imagen de la app
		if(menu.getItemId()==android.R.id.home){
			if(drawerLayout.isDrawerOpen(drawerList)) closeDrawer();
			else openDrawer();
		}
		
		return super.onOptionsItemSelected(menu);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	//Clase que cambia entre opción del drawer
	private class ItemClickListener implements ListView.OnItemClickListener{
	
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				MainActivity context=((MainActivity) parent.getContext());
				context.setActiveFragment(position);
				context.closeDrawer();
				
			}

			
	}
	//Cierra todos los Drawers
	public void openDrawer(){
		if(!drawerLayout.isDrawerOpen(drawerList))
			drawerLayout.openDrawer(drawerList);
	}
	//Cierra todos los Drawers
	public void closeDrawer(){
		if(drawerLayout.isDrawerOpen(drawerList))
			drawerLayout.closeDrawer(drawerList);
	}
	//Listener para el Tab
	private class NavigationBarTabListener implements ActionBar.TabListener{
		private MainActivity activity;
		
		public NavigationBarTabListener(MainActivity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction arg1) {}
	
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction arg1) {
			activity.setTabFragment(tab.getText().toString());
		}
	
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction arg1) {}
		
	}
	
	private void setTabFragment(String tab){
		if(tab.equals(tabs_strings[0]))
			setActiveFragment(SHOP_LIST);
		else
			setActiveFragment(MAP);
	}
	
}