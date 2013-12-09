package com.worldpcs.tiendecitas3.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.worldpcs.tiendecitas3.interfaces.FragmentNavigationHandlerNotification;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
/**
 * Esta clase está pensada para navegar entre fragmentos
 * @author Xarly
 *
 */
public class FragmentNavigationHandler {
	/**
	 * Los fragmentos que maneja esta tab container->nombre->fragmento
	 */
	protected HashMap<Integer,HashMap<String,Fragment>> fragments=null;

	/**
	 * Id de recurso del container
	 */
	protected int container_dest_resource_id=-1;
	/**
	 * Destino para los fragmentos
	 */
	//protected View container_dest_resource=null;
	/**
	 * Fragment manager que usaré
	 */
	protected FragmentManager fm=null;
	/**
	 * Actividad a la que pertenece este handler
	 */
	private ActionBarActivity activity=null;
	
	/**
	 * Constructor
	 * @param fragments
	 * @param container_dest
	 * @param fragmentManager
	 */
	@SuppressLint("UseSparseArrays")
	public FragmentNavigationHandler(ActionBarActivity activity) {
		this.activity=activity;
		fragments=new HashMap<Integer,HashMap<String,Fragment>>();
		//Comprobamos 
		this.fm=this.activity.getSupportFragmentManager();
		
	}
	//Añadimos el fragmento
	public void addFragment(int container_dest_resource_id, String key, Fragment frag, boolean visible){
		if(!fragments.containsKey(container_dest_resource_id))
			fragments.put(container_dest_resource_id, new HashMap<String,Fragment>());
		HashMap<String, Fragment> frags_by_dest = fragments.get(container_dest_resource_id);
		if(frags_by_dest.containsValue(frag))
			return;
		frags_by_dest.put(key, frag);
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(container_dest_resource_id,frag);
		if(!visible)
			ft.hide(frag);
		else
			ft.show(frag);
		ft.commit();
		fm.executePendingTransactions();		
	}
	/**
	 * Hace que se muestre el fragmento
	 * @param key
	 */
	public void setActiveFragment(int container_dest_resource_id, String key){
		if(!fragments.containsKey(container_dest_resource_id)) return;
		HashMap<String, Fragment> frags_by_dest = fragments.get(container_dest_resource_id);
		
		//Iniciamos una transacción
		FragmentTransaction ft = fm.beginTransaction();
		
		//Fragmento visible
		Fragment visible=frags_by_dest.get(key);
		ft.show(visible);

		
		Set<String> keys = frags_by_dest.keySet();
		for(Iterator<String> i = keys.iterator();i.hasNext();){
			String keyAux=i.next();
			Fragment frag = frags_by_dest.get(keyAux);
			if(!keyAux.equals(key))
				ft.hide(frag);
		}
		
		ft.commit();
		fm.executePendingTransactions();
		//Notificamos a la actividad que ha cambiado el fragmento - debe de implementar la interfaz..
		((FragmentNavigationHandlerNotification) activity).notifyActiveFragment(container_dest_resource_id,key,visible);
	}
	
	
	
}
