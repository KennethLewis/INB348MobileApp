package com.example.keepupv1.user;

public class User {

	//private variables
	private int id;
	private String username;
	private String email;
	private UserType rights;
	private enum UserType { ADMIN, USER, UNVERIFIED };
	
	// Empty constructor
	public User() { }
	
	// Constructors
	public User(int id, String username, String email, int rights) {
		this.id = id;
		this.username = username;
		this.email = email;
		SetRights(rights);
	}
	public User(String username, String email, int rights) {
		this.username = username;
		this.email = email;
		SetRights(rights);
	}

	//Get and Set User's Id
	public int GetId() {
		return this.id;
	}
	public void SetId(int id) {
		this.id = id;
	}

	//Get and Set User's Username
	public String GetUsername() {
		return this.username;
	}
	public void SetUsername(String username) {
		this.username = username;
	}

	//Get and Set User's Email
	public String GetEmail() {
		return this.email;
	}
	public void SetEmail(String email) {
		this.email = email;
	}
	
	//Gives user a rights enum
	public void SetRights(int rights) {
		//Give the user rights based on integer.
		switch(rights) {
		case 0:
			this.rights = UserType.ADMIN;
			break;
		case 1:
			this.rights = UserType.USER;
			break;
		case 2:
			this.rights = UserType.UNVERIFIED;
			break;
		default:
			this.rights = UserType.USER;
			break;
		}
	}
}
