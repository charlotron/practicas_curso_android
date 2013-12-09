package com.worldpcs.tiendecitas3.handlers;

import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

/**
 * Esta clase controla los tabs de una Actividad
 * @author Xarly
 *
 */
public class TabsHandler implements ActionBar.TabListener{
	/**
	 * La actividad que llama a este TabHandler
	 */
	protected ActionBarActivity activity=null;
	/**
	 * El action bar de la actividad
	 */
	protected ActionBar action_bar=null;
	/**
	 * Tabs
	 */
	protected  String[] tab_keys=null;
	/**
	 * Controla los fragmentos
	 */
	protected FragmentNavigationHandler fnh=null;
	/**
	 * Id de recurso del container
	 */
	protected int container_dest_resource_id=-1;
	/**
	 * Si hay que cambiar el título con la tab
	 */
	private boolean hasToChangeTitleWithOptions=false;
	
	public TabsHandler(	ActionBarActivity activity,
						String[] tab_keys,
						boolean hasToChangeTitleWithOptions,
						HashMap<String,Fragment> fragments,
						FragmentNavigationHandler fnh,
						int container_dest_resource_id) {
		this.activity=activity;
		this.tab_keys=tab_keys;
		this.hasToChangeTitleWithOptions=hasToChangeTitleWithOptions;
		this.fnh=fnh;
		this.container_dest_resource_id=container_dest_resource_id;
		
		//Recuperamos el action bar
		this.action_bar=activity.getSupportActionBar();

		
	}
	/**
	 * Muestra o no las tabs
	 * @param show
	 */
	public void showTabs(boolean show){
		if(show)
			showTabs();
		else
			hideTabs();
	}
	/**
	 * Muestra las tabs en el action bar
	 */
	public void showTabs(){
		//Si no están creadas las tabs las creamos (Presuponemos que si no se muestran tabs hay que re-crearlas)
		if(action_bar.getNavigationMode()!=ActionBar.NAVIGATION_MODE_TABS){
			createTabs();
			this.action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}
	/**
	 * Genera las tabs
	 */
	public void createTabs(){
		//Si el número de tabs no es 0 es que venimos de otra actividad, y las borramos
		if(action_bar.getTabCount()!=0)
			action_bar.removeAllTabs();
		
		//Creamos las tabs
		for(int i = 0;i<tab_keys.length;i++){
			action_bar.addTab(
					action_bar.newTab()
						.setText(tab_keys[i])
						.setTabListener(this));
		}
	}
	/**
	 * Oculta las tabs en el action bar
	 */
	public void hideTabs(){
		if(action_bar.getNavigationMode()==ActionBar.NAVIGATION_MODE_TABS)
			this.action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	/**
	 * Esta función cambia de fragmento en base a una tab
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String fragment_name=tab.getText().toString();
		if(this.hasToChangeTitleWithOptions)
			action_bar.setTitle(fragment_name);
		fnh.setActiveFragment(container_dest_resource_id, fragment_name);		
	}
	/**
	 * Devuelve true o false
	 * @param key
	 * @return 
	 */
	public boolean hasTab(String key){
		for(int i=0;i<tab_keys.length;i++){
			if(key.equals(tab_keys[i])) return true;
		}
		return false;
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
}
