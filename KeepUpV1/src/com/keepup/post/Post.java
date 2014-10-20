package com.keepup.post;

import android.util.Log;

public class Post {

	private int id;
	private int userId;
	private int unitId;
	private int groupId;
	private String time;
	private String content;
	
	//Constructing a Post from an SQL String
	public void setupPost(String builderString) {
		String[] segmentedStrings = new String[6];
		
		int offset = 0;
		
		//Log.v("KEEPUP", builderString);
		segmentedStrings[0] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[0].length() + 1;
		segmentedStrings[1] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[1].length() + 1;
		segmentedStrings[2] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[2].length() + 1;
		segmentedStrings[3] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[3].length() + 1;
		segmentedStrings[4] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[4].length() + 1;
		segmentedStrings[5] = builderString.substring(offset, offset + 512);
		offset += segmentedStrings[5].length();
		
		this.id = Integer.parseInt(segmentedStrings[0].replace(" ", ""));
		this.userId = Integer.parseInt(segmentedStrings[1].replace(" ", ""));
		this.unitId = Integer.parseInt(segmentedStrings[2].replace(" ", ""));
		this.groupId = Integer.parseInt(segmentedStrings[3].replace(" ", ""));
		this.time = segmentedStrings[4];
		this.content = segmentedStrings[5];

//		Log.v("KEEPUP", String.valueOf(this.getId()));
//		Log.v("KEEPUP", String.valueOf(this.getUserId()));
//		Log.v("KEEPUP", String.valueOf(this.getUnitId()));
//		Log.v("KEEPUP", String.valueOf(this.getGroupId()));
//		Log.v("KEEPUP", this.getTime());
//		Log.v("KEEPUP", this.getContent());
	}
	
	public Post() { }
	public Post(int id, int userId, int unitId, int groupId, String time, String content) {
		this.id = id;
		this.userId = userId;
		this.unitId = unitId;
		this.groupId = groupId;
		this.time = time;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
