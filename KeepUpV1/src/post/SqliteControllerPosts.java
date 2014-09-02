package post;

import java.util.ArrayList;

import java.util.HashMap;

import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteControllerPosts  extends SQLiteOpenHelper {
	private static final String LOGCAT = null;

	public SqliteControllerPosts(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT,"Created");
    }
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE Posts ( UserName TEXT, Date TEXT, Post TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT,"Posts Created");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS Posts";
		database.execSQL(query);
        onCreate(database);
	}
	
	public void insertPosts(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("UserName", queryValues.get("UserName"));
		database.insert("Posts", null, values);
		database.close();
	}
	
	public int updateStudent(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();	 
	    ContentValues values = new ContentValues();
	    values.put("StudentName", queryValues.get("StudentName"));
	    return database.update("Students", values, "StudentId" + " = ?", new String[] { queryValues.get("StudentId") });
	    //String updateQuery = "Update  words set txtWord='"+word+"' where txtWord='"+ oldWord +"'";
	    //Log.d(LOGCAT,updateQuery);
	    //database.rawQuery(updateQuery, null);
	    //return database.update("words", values, "txtWord  = ?", new String[] { word });
	}
	
	public void deleteStudent(String id) {
		Log.d(LOGCAT,"delete");
		SQLiteDatabase database = this.getWritableDatabase();	 
		String deleteQuery = "DELETE FROM  Students where StudentId='"+ id +"'";
		Log.d("query",deleteQuery);		
		database.execSQL(deleteQuery);
	}
	
	public ArrayList<HashMap<String, String>> getAllStudents() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM Students";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("StudentId", cursor.getString(0));
	        	map.put("StudentName", cursor.getString(1));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return wordList;
	}
	
	public HashMap<String, String> getStudentInfo(String id) {
		HashMap<String, String> wordList = new HashMap<String, String>();
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM Students where StudentId='"+id+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
	        do {
					//HashMap<String, String> map = new HashMap<String, String>();
	        	wordList.put("StudentName", cursor.getString(1));
				   //wordList.add(map);
	        } while (cursor.moveToNext());
	    }				    
	return wordList;
	}	
	
}