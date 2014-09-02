package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.user.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "UsersManager";

	// Users table name
	private static final String TABLE_USERS = "Users";

	// Users Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_USERNAME = "name";
	private static final String KEY_EMAIL = "phone_number";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
				+ KEY_EMAIL + " TEXT" + ")";
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
	void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, user.GetUsername()); 
		values.put(KEY_EMAIL, user.GetEmail());

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single User
	User getUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
				KEY_USERNAME, KEY_EMAIL }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), 
				cursor.getString(2), Integer.parseInt(cursor.getString(3)));
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
				User User = new User();
				User.SetId(Integer.parseInt(cursor.getString(0)));
				User.SetUsername(cursor.getString(1));
				User.SetEmail(cursor.getString(2));
				// Adding User to list
				UserList.add(User);
			} while (cursor.moveToNext());
		}

		// return User list
		return UserList;
	}

	// Updating single User
	public int updateUser(User User) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, User.GetUsername());
		values.put(KEY_EMAIL, User.GetEmail());

		// updating row
		return db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(User.GetId()) });
	}

	// Deleting single User
	public void deleteUser(User User) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID + " = ?",
				new String[] { String.valueOf(User.GetId()) });
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
	    db.delete(DatabaseHandler.TABLE_USERS, null, null);
	}

}
