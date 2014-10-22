package com.keepup;

import java.util.ArrayList;

import com.keepup.user.User;
import com.keepup.post.*;

public class GlobalVariables {
	
	//Might be redundant, might be better to fetch information on page loads that require them.
	//We shall see though.
	public static User USERLOGGEDIN;
	public static ArrayList<User> USERSFORGROUP = new ArrayList<User>();
	public static ArrayList<Post> POSTS = new ArrayList<Post>();

}
