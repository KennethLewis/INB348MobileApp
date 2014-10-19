package com.keepup.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseController extends SQLiteOpenHelper {
	
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "UsersManager.db";

	// Users table name
	private static final String TABLE_USERS = "Users";

	// Users Table Columns names
	private static final String KEY_ID = "userId";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_RIGHTS = "rights";
	private static final String KEY_PASSWORD = "password";

	public UserDatabaseController(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_ID + " INTEGER NOT NULL," 
				+ KEY_USERNAME + " TEXT NOT NULL,"
				+ KEY_EMAIL + " TEXT," 
				+ KEY_RIGHTS + " INTEGER," 
				+ KEY_PASSWORD + " TEXT, "
				+ "PRIMARY KEY( " + KEY_ID + ", " + KEY_USERNAME + ", " + KEY_PASSWORD + ")" 
				+ ")";
		db.execSQL(CREATE_USERS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new User
	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		if(user.getId() > 0)
			values.put(KEY_ID, user.getId()); 
		values.put(KEY_USERNAME, user.getUsername()); 
		values.put(KEY_EMAIL, user.getEmail());
		values.put(KEY_RIGHTS, user.getRights());
		values.put(KEY_PASSWORD, user.getPw());

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single User
	public User getUser(int id) {
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_RIGHTS, KEY_PASSWORD }, 
								KEY_ID + "=?", new String[] { String.valueOf(id) }, 
								null, null, null, null);
		
		//Cursor exists, check first data entry, if there is none, return null.
		if (cursor != null)
			if(!cursor.moveToFirst())
				return null;

		User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), 
				cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
		
		db.close();

		return user;
	}

	// Getting single User
	public User getUserWithUnit(int id, String unitCode) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
				KEY_USERNAME, KEY_EMAIL, KEY_RIGHTS, KEY_PASSWORD }, KEY_ID + "=? AND " + KEY_PASSWORD + "=?",
				new String[] { String.valueOf(id), unitCode }, null, null, null, null);
		User user = null;
		if (cursor != null)
			if(cursor.moveToFirst())
				user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), 
						cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
		//User user = new User(1, "Jacksane", "test@test.com", 0, "INB270");
		
		db.close();
		// return User
		return user;
	}
	
	// Getting All Users
	public List<User> getAllUsers() {
		List<User> UserList = new ArrayList<User>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), 
						cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
				UserList.add(user);
			} while (cursor.moveToNext());
		}
		db.close();

		// return User list
		return UserList;
	}

	// Deleting single User
	public void deleteUser(User User) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID + " = ?",
				new String[] { String.valueOf(User.getId()) });
		db.close();
	}


	// Getting Users Count
	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USERS;
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
	    db.delete(UserDatabaseController.TABLE_USERS, null, null);

		db.close();
	}

}