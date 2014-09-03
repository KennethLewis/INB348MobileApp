package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteOpenHelper;
import post.Post;
import post.PostDatabaseController;

public class DatabaseVariables {
	
	private static final IndividualUnitActivity INDIVIDUALUNITACTIVIY 
										= new IndividualUnitActivity();
	public static PostDatabaseController POSTDATABASEHANDLER;
	
	public DatabaseVariables(){
		POSTDATABASEHANDLER = new PostDatabaseController(INDIVIDUALUNITACTIVIY);
	}
	
	public PostDatabaseController getPOSTDATABASEHANDLER() {
		return POSTDATABASEHANDLER;
	}

	public void setPOSTDATABASEHANDLER(PostDatabaseController pOSTDATABASEHANDLER) {
		POSTDATABASEHANDLER = pOSTDATABASEHANDLER;
	}
	

}
