package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.unit.Unit;
import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

import android.database.sqlite.SQLiteOpenHelper;
import post.Post;
import post.PostDatabaseController;

public class DatabaseVariables {
	
	
	public static User USERLOGGEDIN;
	private static final Unit unit1 = new Unit ("INB100", "Introduction to IT");
	private static final Unit unit2 = new Unit ("INB123", "Programming 101");
	private static final Unit unit3 = new Unit ("INB348", "Mobile App Dev");
	private static final Unit unit4 = new Unit ("INB270", "Advanced Programming");
	
	public static final Unit [] ALLUNITS = {unit1, unit2, unit3, unit4};
	
	private static final IndividualUnitActivity INDIVIDUALUNITACTIVIY 
										= new IndividualUnitActivity();
	private static final MainActivity MAINACTIVITY = new MainActivity();
	
	public static UserDatabaseController USERDATABASECONTROLLER;
	public static PostDatabaseController POSTDATABASEHANDLER;
	
	public DatabaseVariables(){
		USERDATABASECONTROLLER = new UserDatabaseController (MAINACTIVITY);
		POSTDATABASEHANDLER = new PostDatabaseController(INDIVIDUALUNITACTIVIY);
	}
	
	public static UserDatabaseController getUSERDATABASECONTROLLER() {
		return USERDATABASECONTROLLER;
	}

	public static void setUSERDATABASECONTROLLER(
			UserDatabaseController uSERDATABASECONTROLLER) {
		
		USERDATABASECONTROLLER = uSERDATABASECONTROLLER;
	}

	public PostDatabaseController getPOSTDATABASEHANDLER() {
		return POSTDATABASEHANDLER;
	}

	public void setPOSTDATABASEHANDLER(PostDatabaseController pOSTDATABASEHANDLER) {
		POSTDATABASEHANDLER = pOSTDATABASEHANDLER;
	}
	

}
