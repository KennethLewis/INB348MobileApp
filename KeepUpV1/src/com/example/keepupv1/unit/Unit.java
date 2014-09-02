package com.example.keepupv1.unit;

import com.example.keepupv1.user.User;

public class Unit {

	//private variables
	private char[] unitCode = new char[6];
	private String name;
	private User coordinator;
	
	// Empty constructor
	public Unit() { }
	
	// Constructors
	public Unit(char[] unitCode, String name, User coordinator) {
		this.unitCode = unitCode;
		this.name = name;
		this.coordinator = coordinator;
	}
	public Unit(String name, User coordinator) {
		this.name = name;
		this.coordinator = coordinator;
	}

	//Get and Set Unit's unit code
	public char[] GetCode() {
		return this.unitCode;
	}
	public void SetCode(char[] unitCode) {
		this.unitCode = unitCode;
	}

	//Get and Set Unit's name
	public String GetName() {
		return this.name;
	}
	public void SetName(String name) {
		this.name = name;
	}

	//Get and Set Unit's coordinator
	public User GetCoordinator() {
		return this.coordinator;
	}
	public void SetCoordinator(User coordinator) {
		this.coordinator = coordinator;
	}
	
}
