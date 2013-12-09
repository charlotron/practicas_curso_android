package com.worldpcs.tiendecitas4.interfaces;
/**
 * Esta clase implementa que tengan la función notifyActiveDataUpdated
 * @author Xarly
 *
 */
public interface DataUpdatedNotification {
	/**
	 * Llamada al acabar de carga los datos
	 */
	public void notifyActiveDataUpdated(String tag);
}
