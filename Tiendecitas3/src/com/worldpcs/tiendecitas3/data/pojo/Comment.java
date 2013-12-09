package com.worldpcs.tiendecitas3.data.pojo;

public class Comment {
	public static final String TYPE="comment";
	
	protected int id=-1;
	protected String comments=null;

	public String getComment() {
		return comments;
	}

	public void setComment(String comment) {
		comments = comment;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return comments;
	}

}
