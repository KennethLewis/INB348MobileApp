package post;


import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.user.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostDatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "UserPosts";

	// Users table name
	private static final String TABLE_POSTS = "Posts";

	// Users Table Columns names
	private static final String KEY_USERNAME = "studentName";
	private static final String KEY_DATE = "date";
	private static final String KEY_POST = "post";

	public PostDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
				+ KEY_USERNAME + " TEXT," + KEY_DATE + " TEXT,"
				+ KEY_POST + " TEXT" + ")";
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

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new User
	public void addPost(Post post) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, post.getStudent());
		values.put(KEY_DATE, post.getTestDate()); 
		values.put(KEY_POST, post.getPost());

		// Inserting Row
		db.insert(TABLE_POSTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single User
	Post getPost() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_POSTS, new String[] { KEY_USERNAME,
				KEY_DATE, KEY_POST }, null, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Post post = new Post (cursor.getString(0),cursor.getString(1), cursor.getString(2));
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
				post.setStudent(cursor.getString(0));
				post.setTestDate(cursor.getString(1));
				post.setPost(cursor.getString(2));
				// Adding User to list
				userPosts.add(post);
			} while (cursor.moveToNext());
		}

		// return User list
		return userPosts;
	}

	// Deleting single User
	public void deleteUser(User User) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_POSTS, KEY_USERNAME + " = ?",
				new String[] { String.valueOf(User.GetId()) });
		db.close();
	}


	// Getting Users Count
	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_POSTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}
	

	// Getting Users Count
	public void emptyDatabase() {
	    // If whereClause is null, it will delete all rows.
	    SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
	    db.delete(PostDatabaseHandler.TABLE_POSTS, null, null);
	}

}
