/*
 * NOT PROPER WORKING -JACK
 * 
 */

/*package com.keepupv1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.keepupv1.unit.Unit;
import com.keepupv1.user.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Type;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database constants
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "KeepUp_Store.db";

	 USERS TABLE CONSTS 
	private static final String TABLE_USERS 	= "Users";
	private static final String TABLE_UNITS 	= "Units"; 
	private static final String[] TABLE_NAMES = new String[] {
		"Users", "Units"
	};
	private static final String[][] COLUMN_NAMES = new String[][] { 
		//Users Table
		{ "userId", "username", "email", "rights", "unitCode" },
		//Units Table
		{ "unitId", "unitcode", "unitname", "members", "membersStudentNo" },
	};

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Creating Tables
	public void onCreate(SQLiteDatabase db, String tableName) {
		String CREATE_TABLE = "CREATE TABLE " + tableName + 
				fetchCreateParams(Arrays.asList(TABLE_NAMES).indexOf(tableName));
		db.execSQL(CREATE_TABLE);
	}

	// Upgrading database
	public void onUpgrade(SQLiteDatabase db, String tableName, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + tableName);

		// Create tables again
		onCreate(db, tableName);
	}

	*//**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 *//*

	// Adding new User
	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		if(user.getId() > 0)
			values.put(KEY_ID, user.getId()); 
		values.put(KEY_USERNAME, user.getUsername()); 
		values.put(KEY_EMAIL, user.getEmail());
		values.put(KEY_RIGHTS, user.getRights());
		values.put(KEY_UNITCODE, user.getUnit());

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single User
	public User getUser(int id) {
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_RIGHTS, KEY_UNITCODE }, 
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
				KEY_USERNAME, KEY_EMAIL, KEY_RIGHTS, KEY_UNITCODE }, KEY_ID + "=? AND " + KEY_UNITCODE + "=?",
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

	// Updating single row with params
	public int updateRow(int tableIndex, String columnName, String rowValue, Object updatedObj) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		if(updatedObj instanceof User) {
			values.put(COLUMN_NAMES[tableIndex][1], ((User) updatedObj).getUsername());
			values.put(COLUMN_NAMES[tableIndex][2], ((User) updatedObj).getEmail());
			values.put(COLUMN_NAMES[tableIndex][3], ((User) updatedObj).getRights());
			values.put(COLUMN_NAMES[tableIndex][4], ((User) updatedObj).getUnit());
		} 
		else if(updatedObj instanceof Unit) {
			values.put(COLUMN_NAMES[tableIndex][1], ((Unit) updatedObj).getUnitCode());
			values.put(COLUMN_NAMES[tableIndex][2], ((Unit) updatedObj).getName());
			values.put(COLUMN_NAMES[tableIndex][3], ((Unit) updatedObj).getAllUsers());
			values.put(COLUMN_NAMES[tableIndex][4], ((Unit) updatedObj).getAllUsersStudentId());
		}

		return db.update(TABLE_NAMES[tableIndex], values, columnName + " = ?", 
				new String[] { String.valueOf(rowValue) });
	}

	// Deleting single User
	public void deleteRow(String tableName, String column, String rowValue) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tableName, column + " = ?", new String[] { String.valueOf(rowValue) });
		db.close();
	}

	// Getting Users Count
	public int getRowCount(String tableName) {
		String countQuery = "SELECT  * FROM " + tableName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}

	// Empty and delete a table.
	public void emptyDatabase(String tableName) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(tableName, null, null);
		db.close();
	}
	
	private String fetchCreateParams(int tableIndex) {
		switch(tableIndex) {
		
		case 0:
			return "(" + COLUMN_NAMES[tableIndex][0] + " INTEGER NOT NULL," 
					   + COLUMN_NAMES[tableIndex][1] + " TEXT NOT NULL,"
					   + COLUMN_NAMES[tableIndex][2] + " TEXT," 
					   + COLUMN_NAMES[tableIndex][3] + " INTEGER," 
					   + COLUMN_NAMES[tableIndex][4] + " TEXT, "
					   + "PRIMARY KEY( " + COLUMN_NAMES[tableIndex][0] + ", " 
									 	 + COLUMN_NAMES[tableIndex][1] + ", " 
									 	 + COLUMN_NAMES[tableIndex][4] + "))";
		case 1:
			return "(" + COLUMN_NAMES[tableIndex][0] + " INTEGER PRIMARY KEY,"
					   + COLUMN_NAMES[tableIndex][1] + " TEXT,"
					   + COLUMN_NAMES[tableIndex][2] + " TEXT," 
					   + COLUMN_NAMES[tableIndex][3] + " TEXT,"
					   + COLUMN_NAMES[tableIndex][4] + " TEXT )";
			
		default: return null;
		}
	}

}
*/