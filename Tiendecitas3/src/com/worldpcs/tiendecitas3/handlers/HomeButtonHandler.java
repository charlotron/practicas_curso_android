package com.worldpcs.tiendecitas3.handlers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Esta clase maneja que se muestre u oculte el botón del actionBar de home
 * @author Xarly
 *
 */
public class HomeButtonHandler {
	/**
	 * Métodos de Configuración del botón
	 */
	public static void configureHomeButton(ActionBarActivity activity){
		//Recuperamos el action bar para insertarle la imagen del icono
		ActionBar actionbar=activity.getSupportActionBar();
		//Usa los botones de la app para abrir y cerrar cosas
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
	}
}
