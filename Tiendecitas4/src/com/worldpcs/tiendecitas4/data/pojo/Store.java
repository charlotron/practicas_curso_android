package com.worldpcs.tiendecitas4.data.pojo;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.HashMap;

public class Store {
	public static final String TYPE="store";
	
	protected int id=-1;
	protected String name=null;
	protected String desc=null;
	protected String address=null;
	protected String telephone=null;
	protected String times=null;
	protected String website=null;
	protected String email=null;
	protected String geo_position_lat=null;
	protected String geo_position_long=null;
	protected int fav_count=0;
	protected ArrayList<Comment> comments=new ArrayList<Comment>();
	@SuppressLint("UseSparseArrays")
	protected HashMap<Integer, Photo> photos_map=new HashMap<Integer, Photo>();
	protected ArrayList<Photo> photos_list=new ArrayList<Photo>();

	//private static ArrayList<Object> NO_ITEMS=new ArrayList<Object>();

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGeo_position_lat() {
		return geo_position_lat;
	}

	public void setGeo_position_lat(String geo_position_lat) {
		this.geo_position_lat = geo_position_lat;
	}
	public String getGeo_position_long() {
		return geo_position_long;
	}

	public void setGeo_position_long(String geo_position_long) {
		this.geo_position_long = geo_position_long;
	}

	public int getFav_count() {
		return fav_count;
	}

	public void setFav_count(int fav_count) {
		this.fav_count = fav_count;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public HashMap<Integer,Photo> getPhotosMap() {
		return photos_map;
	}

	public void setPhotosMap(HashMap<Integer, Photo> photos) {
		this.photos_map = photos;
	}
	public ArrayList<Photo> getPhotosList() {
		return photos_list;
	}

	public void setPhotosList(ArrayList<Photo> photos) {
		this.photos_list = photos;
	}
}
