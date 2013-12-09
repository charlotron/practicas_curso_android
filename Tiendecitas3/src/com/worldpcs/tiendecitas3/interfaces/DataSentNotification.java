package com.worldpcs.tiendecitas3.interfaces;
/**
 * Esta clase implementa que tengan la función notifyActiveDataSent
 * @author Xarly
 *
 */
public interface DataSentNotification {
	/**
	 * Llamada al acabar de carga los datos
	 * @param tag 
	 */
	public void notifyActiveDataSent(String tag);
}
