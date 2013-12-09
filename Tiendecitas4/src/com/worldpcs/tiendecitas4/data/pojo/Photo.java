package com.worldpcs.tiendecitas4.data.pojo;

import java.util.ArrayList;

public class Photo {
	public static final String TYPE="photo";
	
	protected int id=-1;
	protected String URL=null;
	protected String desc=null;
	protected int fav_count=0;
	protected ArrayList<Comment> comments=new ArrayList<Comment>();

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
}
