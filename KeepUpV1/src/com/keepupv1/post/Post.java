package com.keepupv1.post;

public class Post {

	private int id;
	private String user;
	private String date;
	private String content;
	private String unit;
	
	public Post() { }
	
	public Post(int id, String user, String date, String content, String unit) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.content = content;
		this.unit = unit;
	}

	public Post(String user, String date, String content, String unit) {
		this.user = user;
		this.date = date;
		this.content = content;
		this.unit = unit;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
