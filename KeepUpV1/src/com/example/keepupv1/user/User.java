package com.example.keepupv1.user;

public class User {

	//private variables
	private int id;
	private String uniId;
	private String username;
	private String email;
	private int rights;
	private String unit;
	private String pw;
	
	// Empty constructor
	public User() { }
	
	//Testing Login
	public User (String uniId){
		this.uniId = uniId;
	}
	//Constructor (Register)
	public User (int id, String username, String email, String pw){
		this.id = id;
		this.username = username;
		this.email = email;
		this.pw = pw;
	}
	public String getUniId() {
		return uniId;
	}

	public void setUniId(String uniId) {
		this.uniId = uniId;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	// Constructors
	public User(int id, String username, String email, int rights, String unit) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.rights = rights;
		this.unit = unit;
	}
	public User(String username, String email, int rights, String unit) {
		this.username = username;
		this.email = email;
		this.rights = rights;
		this.unit = unit;
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

	//Get and Set User's Email
	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
