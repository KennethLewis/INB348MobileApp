package com.keepup.user;


import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.keepup.DatabaseConnector;
import com.keepup.group.Group;
import com.keepup.unit.Unit;

public class User {

	//private variables
	private int id;
	private String username;
	private String email;
	private int rights;
	private String password;
	
	//Constructing a User from an SQL String
	public void setupUser(String builderString) {
		String[] segmentedStrings = new String[5];
		
		int offset = 0;
		
		//Log.v("KEEPUP", builderString);
		segmentedStrings[0] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[0].length() + 1;
		segmentedStrings[1] = builderString.substring(offset, offset + 15);
		offset += segmentedStrings[1].length();
		segmentedStrings[2] = builderString.substring(offset, offset + 50);
		offset += segmentedStrings[2].length();
		segmentedStrings[3] = builderString.substring(offset, builderString.indexOf("^", offset));
		offset += segmentedStrings[3].length() + 1;
		segmentedStrings[4] = builderString.substring(offset, offset + 25);
		offset += segmentedStrings[4].length();
		
		this.id = Integer.parseInt(segmentedStrings[0].replace(" ", ""));
		this.username = segmentedStrings[1].replace(" ", "");
		this.email = segmentedStrings[2].replace(" ", "");
		this.rights = Integer.parseInt(segmentedStrings[3].replace(" ", ""));
		this.password = segmentedStrings[4].replace(" ", "");

		//Log.v("KEEPUP", String.valueOf(this.getId()));
		//Log.v("KEEPUP", this.getUsername());
		//Log.v("KEEPUP", this.getEmail());
		//Log.v("KEEPUP", this.getPw());
		//Log.v("KEEPUP", String.valueOf(this.getRights()));
	}
	
	//Constructors
	public User () { }
	public User (int id, String username, String email, int rights, String password) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.rights = rights;
		this.password = password;
	}

	//Get and Set User's Id
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	//Get and Set User's Username
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	//Get and Set User's Email
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	//Gives user a rights enum
	public void setRights(int rights) {
		this.rights = rights;
	}
	public int getRights() {
		return rights;
	}

	public String getPw() {
		return password;
	}

	public void setPw(String pw) {
		this.password = pw;
	}
}
