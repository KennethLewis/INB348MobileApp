package com.keepup.unit;

import android.util.Log;

public class Unit {

	//private variables
	private int id;
	private String code;
	private String name;
	private int userId;
	
	//Constructing a Unit from an SQL String
	public void setupUnit(String builderString) {
		String[] segmentedStrings = new String[4];
		
		int offset = 0;
		
		//Log.v("KEEPUP", builderString);
		segmentedStrings[0] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[0].length() + 1;
		segmentedStrings[1] = builderString.substring(offset, offset + 6);
		offset += segmentedStrings[1].length();
		segmentedStrings[2] = builderString.substring(offset, offset + 100);
		offset += segmentedStrings[2].length();
		segmentedStrings[3] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[3].length() + 1;
		
		this.id = Integer.parseInt(segmentedStrings[0].replace(" ", ""));
		this.code = segmentedStrings[1].replace(" ", "");
		this.name = segmentedStrings[2];
		this.userId = Integer.parseInt(segmentedStrings[3].replace(" ", ""));

		//Log.v("KEEPUP", String.valueOf(this.getId()));
		//Log.v("KEEPUP", this.getCode());
		//Log.v("KEEPUP", this.getName());
		//Log.v("KEEPUP", String.valueOf(this.getUserId()));
	}
	
	// Empty constructor
	public Unit() { }
	public Unit (int id, String code, String name, int userId) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.userId = userId;
	}

	//Get and Set Unit's id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	//Get and Set Unit's code
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	//Get and Set Unit's name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//Get and Set Unit's userId
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
