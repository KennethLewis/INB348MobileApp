package com.example.keepupv1.unit;

import com.example.keepupv1.user.User;

public class Unit {

	//private variables
	private String unitCode;
	private String name;
	private User coordinator;
	
	// Empty constructor
	public Unit() { }
	
	// Constructors
	public Unit(String unitCode, String name, User coordinator) {
		this.unitCode = unitCode;
		this.name = name;
		this.coordinator = coordinator;
	}
	public Unit(String name, User coordinator) {
		this.name = name;
		this.coordinator = coordinator;
	}

	//Get and Set Unit's unit code
	public String getCode() {
		return this.unitCode;
	}
	public void setCode(String unitCode) {
		this.unitCode = unitCode;
	}

	//Get and Set Unit's name
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
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
