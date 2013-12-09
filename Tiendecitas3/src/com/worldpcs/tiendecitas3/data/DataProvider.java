package com.worldpcs.tiendecitas3.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.data.pojo.Comment;
import com.worldpcs.tiendecitas3.data.pojo.Photo;
import com.worldpcs.tiendecitas3.data.pojo.Store;
import com.worldpcs.tiendecitas3.interfaces.DataSentNotification;
import com.worldpcs.tiendecitas3.interfaces.DataUpdatedNotification;
import com.worldpcs.tiendecitas3.util.FileUtils;
import com.worldpcs.tiendecitas3.util.JSONParser;

/**
 * Esta clase controla la información que entra y sale de la app
 * @author Xarly
 *
 */
@SuppressLint({ "UseSparseArrays", "DefaultLocale" })
public class DataProvider {
	//Patrón SINGLETON
	protected static DataProvider dataHandler=null;
	protected DataProvider() {
        // Creating new JSON Parser
        //jParser = new JSONParser();
		
	}
	public static DataProvider getInstance(){
		if(dataHandler==null)
			dataHandler=new DataProvider();
		return dataHandler;
	}
	//fin Patrón SINGLETON
	/**
	 * Url base
	 */
	protected static final String BASE_URL="http://www.worldpcs.es/curso_android/tiendecitas3/";
	/**
	 * URL para recuperar todos los datos
	 */
	protected static final String URL_GET_ALL=BASE_URL+"?query=all";
	/**
	 * URL para recuperar datos de una tienda con una id
	 */
	protected static final String URL_GET_STORE=BASE_URL+"?query=store&id=";
	/**
	 * URL para recuperar datos de una foto con una id
	 */
	protected static final String URL_GET_PHOTO=BASE_URL+"?query=photo&id=";
	/**
	 * URL para enviar un comentario
	 */
	protected static final String URL_SEND_STORE_COMMENTS=BASE_URL+"?action=add_store_comment&store_id=%d&comment=%s";
	/**
	 * URL para recuperar datos de una tienda con una id
	 */
	protected static final String URL_SEND_STORE_INC_FAV=BASE_URL+"?action=inc_store_fav_count&store_id=%d";
	/**
	 * URL para recuperar datos de una foto con una id
	 */
	protected static final String URL_SEND_PHOTO_COMMENTS=BASE_URL+"?action=add_photo_comment&photo_id=%d&comment=%s";
	/**
	 * URL para recuperar datos de una foto con una id
	 */
	protected static final String URL_SEND_STORE_PHOTO_INC_FAV=BASE_URL+"?action=inc_photo_fav_count&photo_id=%d";		
	/**
	 * URL para enviar una foto
	 */
	protected static final String URL_SEND_PHOTO=BASE_URL+"image_upload.php";		
	/**
	 * Tag que permite preguntar el tipo (Es metadata)
	 */
	public static final String TAG_TYPE="type";	
	public static final String TAG_ID="id";
	public static final String TAG_NAME="name";
	public static final String TAG_DESC="desc";
	public static final String TAG_ADDRESS="address";
	public static final String TAG_TELEPHONE="telephone";
	public static final String TAG_TIMES="times";
	public static final String TAG_WEBSITE="website";
	public static final String TAG_EMAIL="email";
	public static final String TAG_GEO_POSITION="geo_position";
	public static final String TAG_GEO_POSITION_LAT="lat";
	public static final String TAG_GEO_POSITION_LONG="long";
	public static final String TAG_FAV_COUNT="fav_count";
	public static final String TAG_COMMENTS="comments";
	public static final String TAG_COMMENT="comment";
	public static final String TAG_PHOTOS="photos";
	public static final String TAG_URL="URL";
	public static final String TAG_RES="RES";

	/**
	 * Query para recuperar todos los datos
	 */
	public static final String QUERY_ALL="all";	
	/**
	 * Query para recuperar una tienda
	 */
	public static final String QUERY_STORE="store";
	/**
	 * Query para recuperar una tienda
	 */
	public static final String QUERY_STORE_PHOTO="store_photo";	
	/**
	 * Query para recuperar una foto
	 */	
	public static final String QUERY_PHOTO="photo";	
	/**
	 * Enviar comentario a tienda
	 */	
	public static final String ACTION_STORE_SEND_COMMENT="add_store_comment";	
	/**
	 * Aumentar el número de favoritos en tienda
	 */	
	public static final String ACTION_STORE_INC_FAV_COUNT="inc_store_fav_count";	
	/**
	 * Enviar comentario a foto
	 */	
	public static final String ACTION_STORE_PHOTO_SEND_COMMENT="add_photo_comment";	
	/**
	 * Aumentar el número de favoritos en foto
	 */	
	public static final String ACTION_STORE_PHOTO_INC_FAV_COUNT="inc_photo_fav_count";		
	/**
	 * Enviar una foto
	 */	
	public static final String ACTION_PHOTO_SEND="photo_send";		
	/**
	 * Mapa de tiendas actuales id->tienda
	 */
	protected HashMap<Integer,Store> store_map=new HashMap<Integer,Store>();
	/**
	 * Lista de tiendas
	 */
	protected ArrayList<Store> store_list=new ArrayList<Store>();
	/**
	 * Mapa de fotos actuales id->foto
	 */
	protected HashMap<Integer,Photo> photo_map=new HashMap<Integer,Photo>();
	/**
	 * Lista de fotos
	 */
	protected ArrayList<Photo> photo_list=new ArrayList<Photo>();
	/**
	 * Tienda actual (id)
	 */
	private int current_store_id=-1;
	/**
	 * Tienda actual
	 */
	private Store current_store=null;

	
	/**
	 * Descarga todos los datos
	 */
	public void updateAllData(Context context){
		new AsyncHandleData(context, QUERY_ALL).execute();	
	}
	/**
	 * Descarga todos los datos
	 */
	protected void internalUpdateAllData(){
		// Recuperamos el array de datos
		JSONArray json_array = JSONParser.getJSONArrayFromURL(URL_GET_ALL);
        internalUpdateData(json_array);
	}
	/**
	 * Descarga toda la información nueva que pueda sobre una tienda concreta
	 */
	public void updateStore(Context context,int store_id){
		new AsyncHandleData(context,QUERY_STORE,store_id).execute();	
	}
	/**
	 * Descarga toda la información nueva que pueda sobre una tienda concreta
	 */
	protected void internalUpdateStore(int store_id){
		// Recuperamos el array de datos
        JSONArray json_array = JSONParser.getJSONArrayFromURL(URL_GET_STORE+store_id);
        internalUpdateData(json_array);
	}
	/**
	 * Descarga toda la información nueva que pueda sobre una tienda concreta
	 */
	public void updatePhoto(Context context,int photo_id){
		new AsyncHandleData(context,QUERY_PHOTO).execute();
	}
	/**
	 * Descarga toda la información nueva que pueda sobre una tienda concreta
	 */
	protected void internalUpdatePhoto(int photo_id){
		// Recuperamos el array de datos
		JSONArray json_array = JSONParser.getJSONArrayFromURL(URL_GET_PHOTO+photo_id);
        internalUpdateData(json_array);
	}	
	/**
	 * Añadir comentario a una foto o a una tienda
	 * @param store_id
	 * @param msg
	 * @param commonStoreActivity
	 */
	public void addStoreComment(int store_id, String msg, DataUpdatedNotification listener) {
		new AsyncHandleData((Context) listener,ACTION_STORE_SEND_COMMENT,store_id,msg).execute();			
	}	
	/**
	 * Añadir comentario a una foto o a una tienda
	 * @param store_id
	 * @param msg
	 * @param commonStoreActivity
	 */
	public void addStorePhotoComment(int store_id,int photo_id, String msg, DataUpdatedNotification listener) {
		new AsyncHandleData((Context) listener,ACTION_STORE_PHOTO_SEND_COMMENT,store_id,photo_id,msg).execute();			
	}
	/**
	 * 
	 * @param id
	 * @param comment
	 */
	protected void internalAddStoreComment(Context context,int store_id, String comment){
		String url="";
		try {
			url = String.format(URL_SEND_STORE_COMMENTS, store_id, URLEncoder.encode(comment,"UTF-8"));
			JSONParser.getJSONObjectFromURL(url);	
			//Actualizamos los datos
			new AsyncHandleData(context,QUERY_STORE,store_id,false).execute();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	/**
	 * 
	 * @param id
	 * @param comment
	 */
	protected void internalAddStorePhotoComment(Context context,int store_id,int photo_id, String comment){
		String url="";
		try {
			url = String.format(URL_SEND_PHOTO_COMMENTS, photo_id, URLEncoder.encode(comment,"UTF-8"));
			JSONParser.getJSONObjectFromURL(url);	
			//Actualizamos los datos
			new AsyncHandleData(context,QUERY_STORE_PHOTO,store_id,photo_id,false).execute();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	/**
	 * Incrementa los favoritos
	 * @param store_id
	 * @param commonStoreActivity
	 */
	public void incStoreFavCount(int store_id,DataUpdatedNotification listener) {
		new AsyncHandleData((Context) listener,ACTION_STORE_INC_FAV_COUNT,store_id).execute();			
	}	
	/**
	 * Añadir comentario a una foto o a una tienda
	 * @param store_id
	 * @param msg
	 * @param commonStoreActivity
	 */
	public void incStorePhotoFavCount(int store_id,int photo_id, DataUpdatedNotification listener) {
		new AsyncHandleData((Context) listener,ACTION_STORE_PHOTO_INC_FAV_COUNT,store_id,photo_id).execute();			
	}
	/**
	 * 
	 * @param id
	 * @param comment
	 */
	protected void internalIncStoreFavCount(Context context,int store_id){
		String url=String.format(URL_SEND_STORE_INC_FAV, store_id);
		JSONParser.getJSONObjectFromURL(url);	
		//Actualizamos los datos
		new AsyncHandleData(context,QUERY_STORE,store_id,false).execute();
	}
	/**
	 * 
	 * @param id
	 * @param comment
	 */
	protected void internalIncStorePhotoFavCount(Context context,int store_id,int photo_id){
		String url=String.format(URL_SEND_STORE_PHOTO_INC_FAV, photo_id);
		JSONParser.getJSONObjectFromURL(url);	
		//Actualizamos los datos
		new AsyncHandleData(context,QUERY_STORE_PHOTO,store_id,photo_id,false).execute();
	}
	

	/**
	 * Inserta una foto en nuestro backend
	 * @param bm
	 */
	public void addCommunityPhoto(Context context, String picturePath) {
		new AsyncHandleData(context,ACTION_PHOTO_SEND,picturePath).execute();
	}
	/**
	 * Inserta una foto en nuestro backend
	 * @param bm
	 */
	public void internalAddCommunityPhoto(Context context, String picturePath) {
		if(FileUtils.SUCCESS_UPLOAD==FileUtils.uploadFile(picturePath, URL_SEND_PHOTO))
			new AsyncHandleData(context, QUERY_ALL,false).execute();	
	}
	/**
	 * Descarga toda la información nueva que pueda sobre tiendas
	 */
	protected void internalUpdateData(JSONArray json_array){
		
        try{
	        for(int i = 0;i<json_array.length();i++){
	        	JSONObject json_item = (JSONObject) json_array.get(i);
	        	internalUpdateStore(json_item);
	        	internalUpdatePhoto(json_item);
	        }
        }
        catch(JSONException e){
        	  e.printStackTrace();
        }
	}
	/**
	 * Descarga toda la información nueva que pueda sobre una tienda concreta
	 */
	protected void internalUpdateStore(JSONObject json_item){
		if(json_item==null) return;
		try {
			//Nos aseguramos que sea 1 tienda
			if(!json_item.getString(TAG_TYPE).equals(Store.TYPE)) return;
			//Comprobamos que exista esta tienda
			int store_id=json_item.getInt(TAG_ID);
			Store store;
			if(!store_map.containsKey(store_id)){
				store=new Store();
				store.setId(store_id);
				store_map.put(store_id, store);
				store_list.add(store);
			}
			else
				store=store_map.get(store_id);
			
			//Actualizamos los datos		
			store.setName(json_item.getString(TAG_NAME));
			store.setDesc(json_item.getString(TAG_DESC).replace("\\n", "\n"));
			store.setAddress(json_item.getString(TAG_ADDRESS).replace("\\n", "\n"));
			store.setTelephone(json_item.getString(TAG_TELEPHONE).replace("\\n", "\n"));
			store.setTimes(json_item.getString(TAG_TIMES).replace("\\n", "\n"));
			store.setWebsite(json_item.getString(TAG_WEBSITE));
			store.setEmail(json_item.getString(TAG_EMAIL));
			JSONObject json_geo = json_item.getJSONObject(TAG_GEO_POSITION);
			store.setGeo_position_lat(json_geo.getString(TAG_GEO_POSITION_LAT));
			store.setGeo_position_long(json_geo.getString(TAG_GEO_POSITION_LONG));
			store.setFav_count(json_item.getInt(TAG_FAV_COUNT));
			
			JSONArray json_comments = json_item.getJSONArray(TAG_COMMENTS);
			store.getComments().clear();
			for(int i = 0;i<json_comments.length();i++){
	        	JSONObject json_comment = (JSONObject) json_comments.get(i);			
				internalUpdateComment(json_comment,store.getComments());
			}
			
			JSONArray json_photos = json_item.getJSONArray(TAG_PHOTOS);
			for(int i = 0;i<json_photos.length();i++){
	        	JSONObject json_photo = (JSONObject) json_photos.get(i);			
				internalUpdatePhoto(json_photo, store.getPhotosMap(),store.getPhotosList());
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}	


	/**
	 * Guarda los comentarios en la lista indicada
	 */
	protected void internalUpdateComment(JSONObject json_item, ArrayList<Comment> arrayList){
		if(json_item==null) return;
		try {
			//Nos aseguramos que sea 1 comentario
			if(!json_item.getString(TAG_TYPE).equals(Comment.TYPE)) return;
			//Comprobamos que exista este comentario
			int comment_id=json_item.getInt(TAG_ID);
			Comment comment;

			comment=new Comment();
			comment.setId(comment_id);
			arrayList.add(comment);
				
			//Actualizamos los datos		
			comment.setComment(json_item.getString(TAG_COMMENT));
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}			
	/**
	 * Descarga toda la información nueva que pueda sobre una foto concreta, fotos de galería
	 */
	protected void internalUpdatePhoto(JSONObject json_item){
		internalUpdatePhoto(json_item,this.photo_map,this.photo_list);		
	}	
	/**
	 * Descarga toda la información nueva que pueda sobre una foto concreta
	 */
	protected void internalUpdateStorePhoto(int store_id, int photo_id){
		// Recuperamos el array de datos
		JSONArray json_array = JSONParser.getJSONArrayFromURL(URL_GET_PHOTO+photo_id);
		try {
			internalUpdateStorePhoto((JSONObject)json_array.get(0),store_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}			
	/**
	 * Descarga toda la información nueva que pueda sobre una foto concreta
	 */
	protected void internalUpdateStorePhoto(JSONObject json_item,int store_id){
		Store store = store_map.get(store_id);
		internalUpdatePhoto(json_item,store.getPhotosMap(),store.getPhotosList());	
	}		
	/**
	 * Descarga toda la información nueva que pueda sobre una foto concreta
	 */
	protected void internalUpdatePhoto(JSONObject json_item, HashMap<Integer,Photo> photos_map_param, ArrayList<Photo> photos_list_param){
		if(json_item==null) return;
		try {
			//Nos aseguramos que sea 1 comentario
			if(!json_item.getString(TAG_TYPE).equals(Photo.TYPE)) return;
			//Comprobamos que exista este comentario
			int photo_id=json_item.getInt(TAG_ID);
			Photo photo;
			if(!photos_map_param.containsKey(photo_id)){
				photo=new Photo();
				photo.setId(photo_id);
				photos_map_param.put(photo_id, photo);
				photos_list_param.add(photo);
			}
			else
				photo=photos_map_param.get(photo_id);
			//Actualizamos los datos		
			photo.setURL(json_item.getString(TAG_URL));
			photo.setDesc(json_item.getString(TAG_DESC).replace("\\n", "\n"));
			photo.setFav_count(json_item.getInt(TAG_FAV_COUNT));
			
			JSONArray json_comments = json_item.getJSONArray(TAG_COMMENTS);
			photo.getComments().clear();
			for(int i = 0;i<json_comments.length();i++){
	        	JSONObject json_comment = (JSONObject) json_comments.get(i);			
				internalUpdateComment(json_comment,photo.getComments());
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}	

	/**
	 * Esta clase hace las llamadas pertinentes
	 * @author Xarly
	 *
	 */
	protected class AsyncHandleData extends AsyncTask<Void,Void,Void>{
		/**
		 * int1
		 */
		protected int int1=-1;
		/**
		 * int2
		 */
		protected int int2=-1;
		/**
		 * Tipo
		 */
		protected String type;
		/**
		 * Dialogo
		 */
		ProgressDialog dialog;
		/**
		 * Context
		 */
		Context context;
		private String str;
		/**
		 * Has to show dialog
		 */
		private boolean showDialog=true;

		/**
		 * Constructor
		 * @param type
		 */
		public AsyncHandleData(Context context, String type){
			this.context=context;
			this.type=type;
		}
		/**
		 * Constructor
		 * @param type
		 */
		public AsyncHandleData(Context context, String type, boolean showDialog){
			this.context=context;
			this.type=type;
			this.showDialog=showDialog;
		}
		/**
		 * Constructor
		 * @param type
		 * @param id
		 */
		public AsyncHandleData(Context context, String type, int int1){
			this.context=context;
			this.type=type;
			this.int1=int1;
		}
		/**
		 * Constructor
		 * @param type
		 * @param id
		 */
		public AsyncHandleData(Context context, String type, int int1, boolean showDialog){
			this.context=context;
			this.type=type;
			this.int1=int1;
			this.showDialog=showDialog;
		}
		public AsyncHandleData(Context context, String type, int int1, int int2) {
			this.context=context;
			this.type=type;
			this.int1=int1;
			this.int2=int2;
		}
		public AsyncHandleData(Context context, String type, int int1, int int2, boolean showDialog) {
			this.context=context;
			this.type=type;
			this.int1=int1;
			this.int2=int2;
			this.showDialog=showDialog;
		}
		/**
		 * Constructor
		 * @param type
		 * @param id
		 * @param str
		 */
		public AsyncHandleData(Context context, String type, int int1, String str){
			this.context=context;
			this.type=type;
			this.str=str;
			this.int1=int1;
		}
		/**
		 * Constructor
		 * @param type
		 * @param id
		 * @param str
		 */
		public AsyncHandleData(Context context, String type, int int1, String str, boolean showDialog){
			this.context=context;
			this.type=type;
			this.str=str;
			this.int1=int1;
			this.showDialog=showDialog;
		}
		public AsyncHandleData(Context context, String type, int int1, int int2, String str) {
			this.context=context;
			this.type=type;
			this.str=str;
			this.int1=int1;
			this.int2=int2;
		}
		public AsyncHandleData(Context context, String type, int int1, int int2, String str, boolean showDialog) {
			this.context=context;
			this.type=type;
			this.str=str;
			this.int1=int1;
			this.int2=int2;
			this.showDialog=showDialog;
		}
		public AsyncHandleData(Context context, String type, String str) {
			this.context=context;
			this.type=type;
			this.str=str;
		}
		public AsyncHandleData(Context context, String type, String str, boolean showDialog ) {
			this.context=context;
			this.type=type;
			this.str=str;
			this.showDialog=showDialog;
		}		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(showDialog)
				dialog=ProgressDialog.show(	context, 
											context.getResources().getString(R.string.dialog_data_load_title),
											context.getResources().getString(R.string.dialog_data_load_body), 
											true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			super.onPreExecute();
			
			if(type==QUERY_ALL){
				internalUpdateAllData();
			}
			else if(type==QUERY_STORE){
				internalUpdateStore(int1);
			}
			else if(type==QUERY_PHOTO){
				internalUpdatePhoto(int1);
			}
			else if(type==QUERY_STORE_PHOTO){
				internalUpdateStorePhoto(int1,int2);
			}			
			else if(type==ACTION_STORE_SEND_COMMENT){
				internalAddStoreComment(context, int1, str);				
			}
			else if(type==ACTION_STORE_PHOTO_SEND_COMMENT){
				internalAddStorePhotoComment(context, int1,int2,str);				
			}
			else if(type==ACTION_STORE_INC_FAV_COUNT){
				internalIncStoreFavCount(context, int1);
			}
			else if(type==ACTION_STORE_PHOTO_INC_FAV_COUNT){
				internalIncStorePhotoFavCount(context, int1,int2);
			}
			else if(type==ACTION_PHOTO_SEND){
				internalAddCommunityPhoto(context, str);
			}			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(type==QUERY_ALL || type==QUERY_STORE || type==QUERY_PHOTO || type==QUERY_STORE_PHOTO){
				((DataUpdatedNotification)context).notifyActiveDataUpdated(type);
			}
			else if(type==ACTION_STORE_SEND_COMMENT || type==ACTION_STORE_INC_FAV_COUNT || type==ACTION_STORE_PHOTO_SEND_COMMENT || type==ACTION_STORE_PHOTO_INC_FAV_COUNT){
				((DataSentNotification)context).notifyActiveDataSent(type);
			}
			
			if(showDialog){
				dialog.dismiss();
			}
		}

		
	}
	/**
	 * Devuelve las fotos
	 * @return
	 */
	public ArrayList<Photo> getPhotos() {
		return photo_list;
	}
	/**
	 * Devuelve las Tiendas
	 * @return
	 */
	public ArrayList<Store> getStores() {
		return store_list;
	}
	/**
	 * Establece la tienda actual (en activo)
	 * @param position
	 */
	public void setCurrentStore(int position){
		this.current_store=store_list.get(position);
		this.current_store_id=current_store.getId();
	}
	/**
	 * Establece la tienda actual (en activo)
	 * @param store
	 */
	public void setCurrentStore(Store store){
		if(current_store==store) return;
		this.current_store_id=store.getId();
		this.current_store=store;
	}
	/**
	 * Devuelve la id de la tienda actual
	 * @return
	 */
	public int getCurrentStoreId(){
		return this.current_store_id;
	}
	/**
	 * Devuelve la tienda actual
	 * @return
	 */
	public Store getCurrentStore(){
		return this.current_store;
	}

}
