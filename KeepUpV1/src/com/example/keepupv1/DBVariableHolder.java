package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteOpenHelper;
import post.Post;
import post.PostDatabaseHandler;

public class DBVariableHolder {
	
	private static final IndividualUnitActivity INDIVIDUALUNITACTIVIY 
										= new IndividualUnitActivity();
	public static PostDatabaseHandler POSTDATABASEHANDLER;
	public static List<Post> allPosts = new ArrayList<Post>();
	
	public DBVariableHolder(){
		POSTDATABASEHANDLER = new PostDatabaseHandler(INDIVIDUALUNITACTIVIY);
	}
	
	public PostDatabaseHandler getPOSTDATABASEHANDLER() {
		return POSTDATABASEHANDLER;
	}

	public void setPOSTDATABASEHANDLER(PostDatabaseHandler pOSTDATABASEHANDLER) {
		POSTDATABASEHANDLER = pOSTDATABASEHANDLER;
	}
	

}
