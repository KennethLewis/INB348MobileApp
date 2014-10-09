package com.keepupv1.unit;

import java.util.ArrayList;
import java.util.List;

import com.keepupv1.user.User;

public class Unit {

	//private variables
	private String unitCode;
	private String name;
	private User coordinator;
	private String allUsers;
	private String allUsersStudentId;
	
	private List<User> enrolledUsers = new ArrayList<User>();
	
	// Empty constructor
	public Unit() { }
	
	public Unit (String unitCode, String name, String allUsers, 
					String allUsersStudentId){
		this.unitCode = unitCode;
		this.name = name;
		this.allUsers = allUsers;
		this.allUsersStudentId = allUsersStudentId;
	}
	public Unit (String unitCode, String name){
		this.unitCode = unitCode;
		this.name = name;
	}
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

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public User getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(User coordinator) {
		this.coordinator = coordinator;
	}

	public List<User> getEnrolledUsers() {
		return enrolledUsers;
	}

	public void setEnrolledUsers(List<User> enrolledUsers) {
		this.enrolledUsers = enrolledUsers;
	}
	
	public void addUserToUnit (User u){
		enrolledUsers.add(u);
	}

	public String getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(String allUsers) {
		this.allUsers = allUsers;
	}

	public String getAllUsersNames(){
		for(User user: enrolledUsers)
			allUsers += user.getUsername() + ",";
		return allUsers;
			
	}
	public String getAllUsersStudentId() {
		for(User user: enrolledUsers)
			allUsersStudentId += user.getId() + ",";
		return allUsersStudentId;
	}

	public void setAllUsersStudentId(String allUsersStudentId) {
		this.allUsersStudentId = allUsersStudentId;
	}
	
	public List<Integer> gatherUsersId(){
		
		List<Integer> studentNoAfterParse = new ArrayList<Integer>();
		String [] delimtedStudentNo;
		if(allUsersStudentId.isEmpty() == false){
			delimtedStudentNo = this.allUsersStudentId.split(",");		
			for(String s: delimtedStudentNo){
				s = s.replace(",","");
				studentNoAfterParse.add(Integer.parseInt(s));
			}
		}
		return studentNoAfterParse;
	}
}
