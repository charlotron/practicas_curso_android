package com.worldpcs.tiendecitas3.interfaces;
/**
 * Esta clase implementa que tengan la funci�n notifyActiveDataUpdated
 * @author Xarly
 *
 */
public interface DataUpdatedNotification {
	/**
	 * Llamada al acabar de carga los datos
	 */
	public void notifyActiveDataUpdated(String tag);
}
