package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.user.UserDatabaseController;

import android.database.sqlite.SQLiteOpenHelper;
import post.Post;
import post.PostDatabaseController;

public class DatabaseVariables {
	
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
