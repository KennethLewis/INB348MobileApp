package com.keepup.post;

import java.util.ArrayList;
import java.util.List;

import com.keepup.user.User;

import android.content.ContentValues;
import android.content.Context;
/import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostDatabaseController extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "UserPosts.db";

	// Users table name
	private static final String TABLE_POSTS = "Posts";

	// Users Table Columns names
	private static final String KEY_ID = "postId";
	private static final String KEY_USER_ID = "username";
	private static final String KEY_DATE = "date";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_UNITCODE = "unitCode";

	public PostDatabaseController(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_USER_ID + " INTEGER," 
				+ KEY_DATE + " TEXT,"
				+ KEY_CONTENT + " TEXT," 
				+ KEY_UNITCODE + " TEXT" 
				+ ")";
		db.execSQL(CREATE_POSTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);

		// Create tables again
		onCreate(db);
	}

	// Adding new User
	public void addPost(Post post) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, post.getDate()); 
		values.put(KEY_CONTENT, post.getContent());
		
		values.put(KEY_USER_ID, post.getUser());
		
		if(post.getUnit() != null)
			values.put(KEY_UNITCODE, post.getUnit());
		
		// Inserting Row
		db.insert(TABLE_POSTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single User
	public Post getPost(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_POSTS, new String[] { KEY_ID,
				KEY_USER_ID, KEY_DATE, KEY_CONTENT, KEY_UNITCODE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		Post post = null;
		if (cursor != null)
			if(cursor.moveToFirst())
				post = new Post(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), 
						cursor.getString(3), cursor.getString(4));
	    
		db.close();
		// return User
		return post;
	}

	// Getting single User
	public Post getPostWithUnit(String unitCode) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_POSTS, new String[] { KEY_ID,
				KEY_USER_ID, KEY_DATE, KEY_CONTENT, KEY_UNITCODE }, KEY_UNITCODE + "=?",
				new String[] { unitCode }, null, null, null, null);
		
		Post post = null;
		if (cursor != null)
			if(cursor.moveToFirst())
				post = new Post(Integer.parseInt(cursor.getString(0)), cursor.getString(1), 
						cursor.getString(2), cursor.getString(3), cursor.getString(4));
		
		db.close();
		// return User
		return post;
	}
	
	// Getting All Users
	public List<Post> getAllPosts() {
		List<Post> userPosts = new ArrayList<Post>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_POSTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Post post = new Post();
				post.setUser(cursor.getString(1));
				post.setDate(cursor.getString(2));
				post.setContent(cursor.getString(3));
				post.setUnit(cursor.getString(4));
				// Adding User to list
				userPosts.add(post);
			} while (cursor.moveToNext());
		}

		db.close();
		// return User list
		return userPosts;
	}

	// Deleting single User
	public void deleteUser(User User) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_POSTS, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(User.getId()) });
		db.close();
	}


	// Getting Users Count
	public int getPostsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_POSTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		db.close();
		// return count
		return count;
	}
	

	// Getting Users Count
	public void emptyDatabase() {
	    // If whereClause is null, it will delete all rows.
	    SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
	    db.delete(PostDatabaseController.TABLE_POSTS, null, null);
	    
		db.close();
	}

}
