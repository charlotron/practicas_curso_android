package com.worldpcs.tiendecitas4.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.worldpcs.tiendecitas4.data.pojo.Comment;
import com.worldpcs.tiendecitas4.data.pojo.Photo;
import com.worldpcs.tiendecitas4.data.pojo.Store;

public class DBAdapter {
    private DBHelper dbHelper;
    private static final String DATABASE_NAME = "stores.db";
    private static final int DATABASE_VERSION = 17;
     
	public DBAdapter (Context context){
		dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		//Llamamos a esta función para forzar que compruebe si hay que actualizar la bd
		dbHelper.getWritableDatabase();
		
		//Comprobamos si es cierto que está creada
		if(!dbHelper.isEmptyTables()){
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.query(DBHelper.TABLE_STORES, null, null, null, null, null, DBHelper.KEY_ID+" ASC");
					    
			if(!cursor.moveToNext()) {
				dbHelper.setEmptyTables(true);
			}
			cursor.close();
		}
	}
	/**
	 * Inserta datos en la bd
	 * @param table
	 * @param values
	 */
	public void insertData(	String table, 
							ContentValues values){
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try{
			long res = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
			if(res!=1 && isEmptyDb()){
				dbHelper.setEmptyTables(false);
			}
		}
		finally {
			db.close();
		}
	}
	/**
	 * Actualiza datos en la bd
	 * @param table
	 * @param values
	 * @param where en forma key=?
	 * @param whereArgs array de valores para keys
	 */
	public void updateData(	String table, 
							ContentValues values, 
							String where, 
							String[] whereArgs){
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try{
			db.updateWithOnConflict(table, values, where, whereArgs, SQLiteDatabase.CONFLICT_IGNORE);
		}
		finally {
			db.close();
		}
	}
	/**
	 * Borra datos en la bd
	 * @param table
	 * @param where en forma key=?
	 * @param whereArgs array de valores para keys
	 */
	public void deleteData(	String table, 
							String where, 
							String[] whereArgs){
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try{
			db.delete(table, where, whereArgs);
		}
		finally {
			db.close();
		}
	}
	/**
	 * Inserta una tienda en la bd
	 * @param store
	 */
	public void insertUpdateStore(Store store){       
		//Vemos si existe la tienda
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String tabla=DBHelper.TABLE_STORES;
		String where=DBHelper.KEY_ID+"=?";
		String[] where_claus=new String[]{store.getId()+""};
		ContentValues values = buildContentValuesFromStore(store);
		//Creamos el cursor
        Cursor cursor = db.query(	tabla, 
        							null, 
        							where, 
        							where_claus, 
        							null, 
        							null, 
        							null);
        
        db = dbHelper.getWritableDatabase();
        //Si existe hacemos update
        if (cursor.moveToNext()) 
        	updateData(	tabla,
        				values, 
        				where,
        				where_claus);
        
        else
        	insertData(	tabla,
        				values);
        
        //Cerramos el cursor
        cursor.close();        
		
        insertUpdateStoreComments(store);
		insertUpdateStorePhotos(store);
	}
	/**
	 * Inserta los comentarios de una tienda
	 * @param store
	 */
	public void insertUpdateStoreComments(Store store){   
		for(Iterator<Comment> it=store.getComments().iterator();it.hasNext();){
			Comment comment=it.next();
			//Vemos si existe la tienda
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String tabla=DBHelper.TABLE_STORES_COMMENTS;
			String where=DBHelper.KEY_ID+"=?";
			String[] where_claus=new String[]{comment.getId()+""};
			ContentValues values = buildContentValuesFromStoreComment(store,comment);
			//Creamos el cursor
	        Cursor cursor = db.query(	tabla, 
	        							null, 
	        							where, 
	        							where_claus, 
	        							null, 
	        							null, 
	        							null);
	        
	        db = dbHelper.getWritableDatabase();
	        //Si existe hacemos update
	        if (cursor.moveToNext()) 
	        	updateData(	tabla,
	        				values, 
	        				where,
	        				where_claus);
	        
	        else
	        	insertData(	tabla,
	        				values);
	        //Cerramos el cursor
	        cursor.close();			
		}
	}
	/**
	 * Inserta un nuevo comentario
	 * @param store
	 */
	public void insertNewStoreComment(Store store, Comment comment){   
		//Vemos si existe la tienda
		String tabla=DBHelper.TABLE_STORES_COMMENTS;
		ContentValues values = buildContentValuesFromStoreComment(store, comment);
		
        //insertamos
       	insertData(	tabla,
        			values);		
	}
	/**
	 * Inserta las fotos de una tienda
	 * @param store
	 */
	public void insertUpdateStorePhotos(Store store){   
		for(Iterator<Photo> it=store.getPhotosList().iterator();it.hasNext();){
			Photo photo=it.next();
			//Vemos si existe la tienda
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String tabla=DBHelper.TABLE_STORES_PHOTOS;
			String where=DBHelper.KEY_PHOTO_ID+"=? AND "+DBHelper.KEY_STORE_ID+"=?";
			String[] where_claus=new String[]{photo.getId()+"",store.getId()+""};
			ContentValues values = buildContentValuesFromStorePhoto(store,photo);
			//Creamos el cursor
	        Cursor cursor = db.query(	tabla, 
	        							null, 
	        							where, 
	        							where_claus, 
	        							null, 
	        							null, 
	        							null);
	        
	        db = dbHelper.getWritableDatabase();
	        //Si existe no hacemos nada, sino insertamos
	        if (!cursor.moveToNext())
	        	insertData(	tabla,
	        				values);
	        //Cerramos el cursor
	        cursor.close();
	        
	        //Insertamos la foto realmente
			insertUpdatePhoto(photo);
		}
	}	
	/**
	 * Inserta una foto en la bd
	 * @param store
	 */
	public void insertUpdatePhoto(Photo photo){    
		//Vemos si existe la tienda
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String tabla=DBHelper.TABLE_PHOTOS;
		String where=DBHelper.KEY_ID+"=?";
		String[] where_claus=new String[]{photo.getId()+""};
		ContentValues values = buildContentValuesFromPhoto(photo);
		//Creamos el cursor
        Cursor cursor = db.query(	tabla, 
        							null, 
        							where, 
        							where_claus, 
        							null, 
        							null, 
        							null);
        
        db = dbHelper.getWritableDatabase();
       //Si existe hacemos update
        if (cursor.moveToNext()) 
        	updateData(	tabla,
        				values, 
        				where,
        				where_claus);
        
        else
        	insertData(	tabla,
        				values);
        //Cerramos el cursor
        cursor.close();
	}
	/**
	 * Inserta los comentarios de una foto
	 * @param photo
	 */
	public void insertPhotoComments(Photo photo){   
		for(Iterator<Comment> it=photo.getComments().iterator();it.hasNext();){
			Comment comment=it.next();
			//Vemos si existe la tienda
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String tabla=DBHelper.TABLE_PHOTOS_COMMENTS;
			String where=DBHelper.KEY_ID+"=?";
			String[] where_claus=new String[]{comment.getId()+""};
			ContentValues values = buildContentValuesFromPhotoComment(photo,comment);
			//Creamos el cursor
	        Cursor cursor = db.query(	tabla, 
	        							null, 
	        							where, 
	        							where_claus, 
	        							null, 
	        							null, 
	        							null);
	        
	        db = dbHelper.getWritableDatabase();
	       //Si existe hacemos update
	        if (cursor.moveToNext()) 
	        	updateData(	tabla,
	        				values, 
	        				where,
	        				where_claus);
	        
	        else
	        	insertData(	tabla,
	        				values);
	        //Cerramos el cursor
	        cursor.close();
		}
	}		
	/**
	 * Inserta un nuevo comentario
	 * @param store
	 */
	public void insertNewPhotoComment(Photo photo, Comment comment){   
		//Vemos si existe la tienda
		String tabla=DBHelper.TABLE_PHOTOS_COMMENTS;
		ContentValues values = buildContentValuesFromPhotoComment(photo, comment);
		
        //insertamos
       	insertData(	tabla,
        			values);		
	}

	/**
	 * Borra comentarios
	 * @param store_id
	 * @param comment_id
	 */
	public void delStoreComment(int comment_id) {
		String tabla=DBHelper.TABLE_STORES_COMMENTS;
		String where=DBHelper.KEY_ID+"=?";
		String[] where_claus=new String[]{comment_id+""};
		
		deleteData(tabla, where, where_claus);
	}
	/**
	 * Borra comentarios
	 * @param store_id
	 * @param comment_id
	 */
	public void delStorePhotoComment(int comment_id) {
		String tabla=DBHelper.TABLE_PHOTOS_COMMENTS;
		String where=DBHelper.KEY_ID+"=?";
		String[] where_claus=new String[]{comment_id+""};
		
		deleteData(tabla, where, where_claus);
	}	
	/**
	 * Recuperar un listado de tiendas para el adapter
	 * @return
	 */
	/*public ArrayList<HashMap<String, String>> getStoresNamesForListAdapter(){
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        Cursor cursor = db.query(DBHelper.TABLE_STORES, null, null, null, null, null, DBHelper.KEY_ID+" ASC");
	        ArrayList<HashMap<String,String>> stores_names = new ArrayList<HashMap<String,String>>();
	        
	        while (cursor.moveToNext()) {
	        	HashMap<String,String> map=new HashMap<String,String>();
	        	map.put(	DataProvider.TAG_NAME, 
	        				cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)));
	        	
	        	map.put(	DataProvider.TAG_DESC, 
	        				cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC)));
	        	
	        	stores_names.add(map);
	        }
	        cursor.close();
	        return stores_names;
	}*/
	/**
	 * Construye valores para inserciones y actualizaciones
	 * @return
	 */
	public ContentValues buildContentValuesFromStore (Store store) {
	        ContentValues values = new ContentValues();
	        if(store.getId()>0)
	        	values.put(DBHelper.KEY_ID, 		store.getId());
	        values.put(DBHelper.KEY_NAME, 		store.getName());
	        values.put(DBHelper.KEY_DESC, 		store.getDesc());
	        values.put(DBHelper.KEY_ADDRESS, 	store.getAddress());
	        values.put(DBHelper.KEY_TELEPHONE, 	store.getTelephone());
	        values.put(DBHelper.KEY_TIMES, 		store.getTimes());
	        values.put(DBHelper.KEY_WEBSITE, 	store.getWebsite());
	        values.put(DBHelper.KEY_EMAIL, 		store.getEmail());
	        values.put(DBHelper.KEY_GEO_LAT, 	store.getGeo_position_lat());
	        values.put(DBHelper.KEY_GEO_LONG, 	store.getGeo_position_long());	        
	        values.put(DBHelper.KEY_FAV_COUNT, 	store.getFav_count());	        
	        return values;
	}
	/**
	 * Construye valores para inserciones y actualizaciones
	 * @return
	 */
	public ContentValues buildContentValuesFromStoreComment(Store store, Comment comment) {
	        ContentValues values = new ContentValues();
	        if(comment.getId()>0)
	        	values.put(DBHelper.KEY_ID, 		comment.getId());
	        values.put(DBHelper.KEY_STORE_ID, 	store.getId());
	        values.put(DBHelper.KEY_COMMENT, 	comment.getComment());
	        return values;
	}	
	/**
	 * Construye valores para inserciones y actualizaciones
	 * @return
	 */
	public ContentValues buildContentValuesFromStorePhoto(Store store, Photo photo) {
	        ContentValues values = new ContentValues();
	        values.put(DBHelper.KEY_STORE_ID, 	store.getId());
	        values.put(DBHelper.KEY_PHOTO_ID, 	photo.getId());
	        return values;
	}	
	/**
	 * Construye valores para inserciones y actualizaciones
	 * @return
	 */
	public ContentValues buildContentValuesFromPhoto (Photo photo) {
	        ContentValues values = new ContentValues();	        
	        if(photo.getId()>0)
	        	values.put(DBHelper.KEY_ID, 		photo.getId());
	        values.put(DBHelper.KEY_URL, 		photo.getURL());
	        values.put(DBHelper.KEY_DESC, 		photo.getDesc());
	        values.put(DBHelper.KEY_FAV_COUNT, 	photo.getFav_count());

	        return values;
	}		
	
	/**
	 * Construye valores para inserciones y actualizaciones
	 * @param store
	 * @return
	 */
	public ContentValues buildContentValuesFromPhotoComment(Photo photo, Comment comment) {
	        ContentValues values = new ContentValues();
	        if(comment.getId()>0)
	        	values.put(DBHelper.KEY_ID, 		comment.getId());
	        values.put(DBHelper.KEY_PHOTO_ID, 	photo.getId());
	        values.put(DBHelper.KEY_COMMENT, 	comment.getComment());
	        return values;
	}	
	/**
	 * If is empty
	 */
	public boolean isEmptyDb(){
		return dbHelper.isEmptyTables();
	}
	/**
	 * Inserta todos los datos que contiene DataProvider en la base de datos
	 */
	public void insertUpdateAllData() {
		//Recorremos las tiendas y fotos y las vamos insertando en la base de datos
		for(Iterator<Store> it=DataProvider.getInstance().getStores().iterator();it.hasNext();)
			insertUpdateStore(it.next());
		for(Iterator<Photo> it=DataProvider.getInstance().getPhotos().iterator();it.hasNext();)
			insertUpdatePhoto(it.next());
	}
	/**
	 * Lee todas las tiendas de la base de datos y las devuelve como un arrayList
	 * @return
	 */
	public ArrayList<Store> readStoreList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_STORES, null, null, null, null, null, DBHelper.KEY_ID+" ASC");
        ArrayList<Store> stores = new ArrayList<Store>();
        
        while (cursor.moveToNext()) {
        	Store store=readStore(cursor);
        	stores.add(store);
        }
        cursor.close();
        return stores;
	}
	/**
	 * Lee todas las fotos de la base de datos
	 * @return
	 */
	public ArrayList<Photo> readPhotoList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PHOTOS, null, DBHelper.KEY_ID+" NOT IN (SELECT "+DBHelper.KEY_PHOTO_ID+" FROM "+DBHelper.TABLE_STORES_PHOTOS+")", null, null, null, DBHelper.KEY_ID+" ASC");
        ArrayList<Photo> photos = new ArrayList<Photo>();
        
        while (cursor.moveToNext()) {
        	Photo photo=readPhoto(cursor);
        	photos.add(photo);
        }
        cursor.close();
        return photos;
	}
	/**
	 * Reads a store from a cursor
	 * @param cursor
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	private Store readStore(Cursor cursor) {
		Store store=new Store();
		store.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
		store.setName(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)));
		store.setDesc(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC)));
		store.setAddress(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS)));
		store.setTelephone(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TELEPHONE)));
		store.setTimes(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIMES)));
		store.setWebsite(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WEBSITE)));
		store.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EMAIL)));
		store.setGeo_position_lat(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GEO_LAT)));
		store.setGeo_position_long(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GEO_LONG)));
		store.setFav_count(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_FAV_COUNT)));
		//Leemos comentarios
		SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.query(DBHelper.TABLE_STORES_COMMENTS, null, DBHelper.KEY_STORE_ID+"=?", new String[]{store.getId()+""}, null, null, DBHelper.KEY_ID+" ASC");
        ArrayList<Comment> comment_list = new ArrayList<Comment>();
        while (cursor1.moveToNext()) {
        	Comment comment=readComment(cursor1);
        	comment_list.add(comment);
        }
        cursor1.close();
        
		store.setComments(comment_list);
        
		//Leemos las Photos
		db = dbHelper.getReadableDatabase();
        cursor1 = db.query(DBHelper.TABLE_STORES_PHOTOS, null, DBHelper.KEY_STORE_ID+"=?", new String[]{store.getId()+""}, null, null, null);
        ArrayList<Photo> photo_list = new ArrayList<Photo>();
        HashMap<Integer,Photo> photo_map = new HashMap<Integer,Photo>();
        while (cursor1.moveToNext()) {
            Cursor cursor2 = db.query(DBHelper.TABLE_PHOTOS, null, DBHelper.KEY_ID+"=?", new String[]{cursor1.getInt(cursor1.getColumnIndex(DBHelper.KEY_PHOTO_ID))+""}, null, null, null);
            while(cursor2.moveToNext()){
	        	Photo photo=readPhoto(cursor2);	        	
	        	photo_list.add(photo);
	        	photo_map.put(photo.getId(),photo);
            }
        	cursor2.close();
        }
        cursor1.close();
        
		store.setPhotosList(photo_list);
		store.setPhotosMap(photo_map);
		
		return store;
	}
	
	/**
	 * Lee una foto
	 * @param cursor1
	 * @return
	 */
	private Photo readPhoto(Cursor cursor) {
		Photo photo=new Photo();
		photo.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
		photo.setURL(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL)));
		photo.setDesc(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC)));
		photo.setFav_count(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_FAV_COUNT)));
		
		//Leemos comentarios
		SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.query(DBHelper.TABLE_PHOTOS_COMMENTS, null, DBHelper.KEY_PHOTO_ID+"=?", new String[]{photo.getId()+""}, null, null, DBHelper.KEY_ID+" ASC");
        ArrayList<Comment> comment_list = new ArrayList<Comment>();
        while (cursor1.moveToNext()) {
        	Comment comment=readComment(cursor1);
        	comment_list.add(comment);
        }
        cursor1.close();
        
        photo.setComments(comment_list);
		
		return photo;
	}
	
	/**
	 * Crea un comentario
	 * @param cursor1
	 * @return
	 */
	private Comment readComment(Cursor cursor) {
		Comment comment=new Comment();
		comment.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
		comment.setComment(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_COMMENT)));
		return comment;
	}
}
