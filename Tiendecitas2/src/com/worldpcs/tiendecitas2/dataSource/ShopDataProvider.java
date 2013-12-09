package com.worldpcs.tiendecitas2.dataSource;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopDataProvider {

	//Singleton Pattern
	private static ShopDataProvider _instance=null;
	
	private static ArrayList<String> NO_ITEMS=new ArrayList<String>();
	
	//Keys para recorrer el hashmap
	public static final String SHOP_NAME = 	"Shop"; //== Key
	public static final String SHOP_DESC = 	"Desc";
	public static final String SHOP_DIR = 	"Dir";
	public static final String SHOP_TLF = 	"Tlf";
	public static final String SHOP_TIME = 	"Time";
	public static final String SHOP_WEB = 	"Web";
	public static final String SHOP_EMAIL =	"Email";

	public static final String ID = "Shop_id";
	
	private int current_shop_id=0;
	
	//Nombre->Descripción
	private ArrayList<HashMap<String,String>> shop_data=null;
	private ArrayList<ArrayList<String>> shop_detail_comments=null;
	private ArrayList<ArrayList<String>> shop_photo_comments=null;
	
	private ShopDataProvider(){
		NO_ITEMS.add("No hay comentarios aún");
		//init
		shop_data=new ArrayList<HashMap<String,String>>();
		shop_detail_comments=new ArrayList<ArrayList<String>>();
		shop_photo_comments=new ArrayList<ArrayList<String>>();
		
		//HARDCODING esto se supone que se recupera de alguna fuente de datos
		
		//Rellenamos los datos de las tiendas
		int i=0;
		
		shop_data.add(creaShopDataItem(
				"PCComponentes", 	
				"La mejor tienda de electrónica de españa", 
				"Pol Ind. Las Salinas, Avda. Europa, Parcela 2-5 y 2-6, 30840", 
				"807 40 57 97", 
				"L-V	09:00 – 21:00\nS	10:00 - 21:00", 
				"http://www.pccomponentes.com", 
				"infoweb@pccomponentes.com"));

		shop_detail_comments.add(i, new ArrayList<String>());
		shop_photo_comments.add(i, new ArrayList<String>());
		
		shop_detail_comments.get(i).add("Este es un comentario sobre pc componentes");
		shop_photo_comments.get(i).add("Bonitas fotos de las instalaciones de gamers hechas por PcCompontenes");
		i+=1;
		
		shop_data.add(creaShopDataItem(
				"Ferpei", 	
				"Ferpei obras y reformas S.L. es una empresa dedicada a la construcción", 
				"C/ Condado, 47 13610 - Campo de Criptana (Ciudad Real)", 
				"687897677", 
				"L-V	09:00 – 21:00", 
				"http://ferpei.es", 
				"contacto@ferpei.es"));

		shop_detail_comments.add(i, new ArrayList<String>());
		shop_photo_comments.add(i, new ArrayList<String>());
		
		shop_detail_comments.get(i).add("Este es un comentario sobre ferpei.");
		shop_detail_comments.get(i).add("Este es otro comentario sobre ferpei.");
		shop_photo_comments.get(i).add("Bonita captura de la página web de ferpei.es");
		i+=1;
		
		shop_data.add(creaShopDataItem(
				"Valdemorillo Indoor Padel", 	
				"Tu padel indoor en la Sierra de Madrid", 
				"C/ Prado Cabrero, nº 6 (Valdemorillo)", 
				"91 826 01 98", 
				"L-V: de 10:00 - 23:30\nS-D: de 9:00 - 23:00", 
				"http://www.valdemorilloindoorpadel.com", 
				"contacto@valdemorilloindoorpadel.com"));

		shop_detail_comments.add(i, new ArrayList<String>());
		shop_photo_comments.add(i, new ArrayList<String>());
		
		shop_detail_comments.get(i).add("Este es un comentario sobre vip");
		shop_photo_comments.get(i).add("Bonito logo de valdemorilloindoorpadel.com");
		i+=1;
			
	}
	public static ShopDataProvider getInstance(){
		if(_instance==null)
			_instance=new ShopDataProvider();
		return _instance;
		
	}
	private HashMap<String, String> creaShopDataItem(String nom, String desc, String dir, String tlf, String time, String web, String email){
		HashMap<String,String> out=new HashMap<String,String>();
		
		out.put(SHOP_NAME,nom);
		out.put(SHOP_DESC,desc);	
		out.put(SHOP_DIR,dir);	
		out.put(SHOP_TLF,tlf);	
		out.put(SHOP_TIME,time);	
		out.put(SHOP_WEB,web);	
		out.put(SHOP_EMAIL,email);
		
		return out;
	}
	
	public void addShopDetailComment(String comment){
		this.shop_detail_comments.get(current_shop_id).add(comment);
	}
	public void addShopPhotoComment(String comment){
		this.shop_photo_comments.get(current_shop_id).add(comment);
	}	
	public ArrayList<String> getShopDetailComment(){
		if(shop_detail_comments.get(current_shop_id).isEmpty())
			return NO_ITEMS;
		return shop_detail_comments.get(current_shop_id);		
	}
	public ArrayList<String> getShopPhotoComment(){
		if(shop_photo_comments.get(current_shop_id).isEmpty())
			return NO_ITEMS;
		return shop_photo_comments.get(current_shop_id);		
	}	
	public ArrayList<HashMap<String, String>> getShop_data() {
		return shop_data;
	}
	//Devuelve la información de la tienda i-esima
	public HashMap<String, String> getCurrentShop() {
		if(shop_data==null) return null;
		try{
			return shop_data.get(current_shop_id);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
		
	}	
	//Establece la tienda actual
	public void setCurrentShopId(int shop_id){
		if(shop_id>=0 && shop_data.size()>=shop_id)
			current_shop_id=shop_id;		
	}
	public int getCurrentShopId(){
		return current_shop_id;
	}
	
}
