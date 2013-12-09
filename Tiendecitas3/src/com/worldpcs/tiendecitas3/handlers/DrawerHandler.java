package com.worldpcs.tiendecitas3.handlers;

import java.util.HashMap;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.worldpcs.tiendecitas3.R;

/**
 * Esta clase maneja un drawer con un listView Básico
 * @author Xarly
 *
 */
public class DrawerHandler implements ListView.OnItemClickListener{

	/**
	 * La actividad que implementa el drawer
	 */
	protected ActionBarActivity activity=null;
	/**
	 * Drawer resource id
	 */
	protected int drawer_layout_resource_id=-1;
	/**
	 * Layout del Drawer
	 */
	protected DrawerLayout drawer_layout=null;
	/**
	 * ListView resource id
	 */
	protected int list_view_layout_resource_id=-1;
	/**
	 * ListView del Drawer
	 */
	protected ListView list_view_layout=null;
	/**
	 * ListView item resource id
	 */
	protected int list_view_item_layout_resource_id=-1;
	/**
	 * List of options
	 */
	protected ListView list_view=null;
	/**
	 * Opciones del drawer
	 */
	protected  String[] option_keys=null;
	/**
	 * Adaptador que controla las opciones del drawer
	 */
	protected  ArrayAdapter<String> adapter=null;
	
	/**
	 * Si tiene un toggle que haga que se abra y cierre
	 */
	protected boolean hasToggle=false;
	/**
	 * Toggle actual
	 */
	protected ActionBarDrawerToggle action_bar_toggle=null;
	/**
	 * Si tiene que cambiar el título con las opciones
	 */
	protected boolean hasToChangeTitleWithOptions=true;
	/**
	 * Action bar de nuestra app
	 */
	protected ActionBar action_bar=null;
	/**
	 * Controla los fragmentos
	 */
	protected FragmentNavigationHandler fnh=null;
	/**
	 * Id de recurso del container
	 */
	protected int container_dest_resource_id=-1;
	
	public DrawerHandler(	ActionBarActivity activity,
							int drawer_layout_resource_id,
							int list_view_layout_resource_id,
							int list_view_item_layout_resource_id,
							String[] option_keys,
							boolean hasToChangeTitleWithOptions,
							HashMap<String,Fragment> fragments,
							FragmentNavigationHandler fnh,
							int container_dest_resource_id) {
		this.activity=activity;
		this.fnh=fnh;
		this.container_dest_resource_id=container_dest_resource_id;
		//Drawer
		this.drawer_layout_resource_id=drawer_layout_resource_id;
		this.drawer_layout=(DrawerLayout) activity.findViewById(drawer_layout_resource_id);
		//ListView
		this.list_view_layout_resource_id=list_view_layout_resource_id;
		this.list_view_layout=(ListView) activity.findViewById(list_view_layout_resource_id);
		//Guardamos el Id del resource del item del layout
		this.list_view_item_layout_resource_id=list_view_item_layout_resource_id;
		//Opciones del drawer
		this.option_keys=option_keys;
		//Si tiene que cambiar el título con las opciones
		this.hasToChangeTitleWithOptions=hasToChangeTitleWithOptions;
		if(hasToChangeTitleWithOptions)
			action_bar=((ActionBarActivity)activity).getSupportActionBar();
		
		//Creamos un adapter
		adapter=new ArrayAdapter<String>(	this.activity,
											this.list_view_item_layout_resource_id,
											this.option_keys);
		list_view_layout.setAdapter(this.adapter);
		
		//Marcamos la primera opción de la lista
		list_view_layout.setItemChecked(0, true);
		
		//Le asignamos el listener a la lista
		list_view_layout.setOnItemClickListener(this);
	}
	/**
	 * Configura un toggle para este drawer
	 */
	public void setDrawerToggle(int icon_resource_id){
		this.hasToggle=true;
		action_bar_toggle=new ActionBarDrawerToggle(	activity,
														drawer_layout,
														icon_resource_id,
														R.string.toggle_open, 
														R.string.toggle_close){
			public void onDrawerOpened(View v){
				ActivityCompat.invalidateOptionsMenu(activity);
			}
			public void onDrawerClosed(View v){
				ActivityCompat.invalidateOptionsMenu(activity);
			}
		};
		drawer_layout.setDrawerListener(action_bar_toggle);
	}
	/**
	 * Cierra el drawer
	 */
	public void closeDrawer(){
		if(drawer_layout.isDrawerOpen(list_view_layout))
			drawer_layout.closeDrawer(list_view_layout);
	}
	/**
	 * Abre el drawer
	 */
	public void openDrawer(){
		if(!drawer_layout.isDrawerOpen(list_view_layout))
			drawer_layout.openDrawer(list_view_layout);
	}
	/**
	 * Alterna el drawer
	 */
	public void toggleOpenCloseDrawer(){
		if(drawer_layout.isDrawerOpen(list_view_layout))
			closeDrawer();
		else
			openDrawer();
	}
	/**
	 * Implementa el click de objetos en el Draw
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		onOptionClick(parent, v, position, id);
		//Si hay que cerrar el drawer
		closeDrawer();
		//Si hay que cambiar el título
		if(this.hasToChangeTitleWithOptions)
			action_bar.setTitle(this.option_keys[position]);
	}
	/**
	 * Acción al pulsar en un objeto, ignorando acciones sobre el drawer(se cierra sólo), si hasToChangeTitleWithOptions=true también cambia título
	 */
	public void onOptionClick(AdapterView<?> parent, View v, int position,	long id){
		fnh.setActiveFragment(container_dest_resource_id, this.option_keys[position]);
	}
}

