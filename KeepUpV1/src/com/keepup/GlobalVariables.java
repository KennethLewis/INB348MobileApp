package com.keepup;

import java.util.ArrayList;

import com.keepup.unit.Unit;
import com.keepup.user.User;
import com.keepup.group.Group;
import com.keepup.post.*;

public class GlobalVariables {
	
	//Might be redundant, might be better to fetch information on page loads that require them.
	//We shall see though.
	public static User USERLOGGEDIN;
	public static ArrayList<User> USERSFORGROUP = new ArrayList<User>();
	
	public static ArrayList<Post> GROUPPOSTS = new ArrayList<Post>();

	public static int UNITCOUNT;
	public static int GROUPCOUNT;
	
	//All variables to hold news feed items
	public static ArrayList<Post> POSTS = new ArrayList<Post>();
	public static ArrayList<User> USERSWHOPOSTED = new ArrayList<User>();
	public static ArrayList<Unit> UNITSWITHPOSTS = new ArrayList<Unit>();
	public static ArrayList<Group> GROUPSWITHPOSTS = new ArrayList<Group>();
}
