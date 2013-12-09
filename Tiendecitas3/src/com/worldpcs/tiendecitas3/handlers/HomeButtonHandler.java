package com.worldpcs.tiendecitas3.handlers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Esta clase maneja que se muestre u oculte el bot�n del actionBar de home
 * @author Xarly
 *
 */
public class HomeButtonHandler {
	/**
	 * M�todos de Configuraci�n del bot�n
	 */
	public static void configureHomeButton(ActionBarActivity activity){
		//Recuperamos el action bar para insertarle la imagen del icono
		ActionBar actionbar=activity.getSupportActionBar();
		//Usa los botones de la app para abrir y cerrar cosas
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
	}
}
