package com.keepupv1.unit;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnitDatabaseController extends SQLiteOpenHelper {

	// All Static variables
		// Database Version
		private static final int DATABASE_VERSION = 1;

		// Database Name
		private static final String DATABASE_NAME = "AllUnits.db";

		// Users table name
		private static final String TABLE_UNITS = "Units";

		// Users Table Columns names
		private static final String KEY_ID = "unitId";
		private static final String KEY_UNIT_CODE = "unitcode";
		private static final String KEY_UNIT_NAME = "unitname";
		private static final String KEY_UNIT_MEMBERS = "members";
		private static final String KEY_UNIT_MEMBERS_STUDENTNO = "membersStudentNo";
		

		public UnitDatabaseController(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		// Creating Tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			String CREATE_UNITS_TABLE = "CREATE TABLE " + TABLE_UNITS + "("
					+ KEY_ID + " INTEGER PRIMARY KEY,"
					+ KEY_UNIT_CODE + " TEXT,"
					+ KEY_UNIT_NAME + " TEXT," 
					+ KEY_UNIT_MEMBERS + " TEXT,"
					+ KEY_UNIT_MEMBERS_STUDENTNO + " TEXT"
					+ ")";
			db.execSQL(CREATE_UNITS_TABLE);
		}

		// Upgrading database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNITS);

			// Create tables again
			onCreate(db);
		}

		// Adding new User
		public void addUnit(Unit unit) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_UNIT_CODE, unit.getUnitCode());
			values.put(KEY_UNIT_NAME, unit.getName());
			values.put(KEY_UNIT_MEMBERS, unit.getAllUsers());
			values.put(KEY_UNIT_MEMBERS_STUDENTNO, unit.getAllUsersStudentId());
			
			// Inserting Row
			db.insert(TABLE_UNITS, null, values);
			db.close(); // Closing database connection
		}

		// Getting single User
		public Unit getUnit(String code) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_UNITS, new String[] { KEY_ID,KEY_UNIT_CODE,
					KEY_UNIT_NAME, KEY_UNIT_MEMBERS, KEY_UNIT_MEMBERS_STUDENTNO}, 
						KEY_UNIT_CODE + "=?",
					new String[] { String.valueOf(code) }, null, null, null, null);

			Unit unit = null;
			if (cursor != null)
				if(cursor.moveToFirst())
					unit = new Unit(cursor.getString(1), cursor.getString(2), 
							cursor.getString(3), cursor.getString(4));
		    
			db.close();
			// return User
			return unit;
		}

		// Getting single User
		/*public Unit getUnitWithUser(String userName) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_UNITS, new String[] {KEY_ID,
					KEY_UNIT_NAME, KEY_UNIT_MEMBERS, KEY_UNIT_MEMBERS_STUDENTNO, KEY_GROUP_DESCRIPTION}, KEY_UNIT_MEMBERS + "=?",
					new String[] { userName }, null, null, null, null);
			
			Group com.keepupv1.group = null;
			if (cursor != null)
				if(cursor.moveToFirst())
					com.keepupv1.group = new Group(cursor.getString(0), cursor.getString(1), 
							cursor.getString(2), cursor.getString(3));
			
			db.close();
			// return User
			return com.keepupv1.group;
		}*/
		
		// Getting All Users
		public List<Unit> getAllUnits() {
			List<Unit> allUnits = new ArrayList<Unit>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_UNITS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Unit unit = new Unit(cursor.getString(1),cursor.getString(2),
							cursor.getString(3), cursor.getString(4));
					// Adding User to list
					allUnits.add(unit);
				} while (cursor.moveToNext());
			}

			db.close();
			// return User list
			return allUnits;
		}

		// Deleting single User
		public void deleteUnit(Unit unit) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_UNITS, KEY_UNIT_CODE + " = ?",
					new String[] { String.valueOf(unit.getUnitCode()) });
			db.close();
		}


		// Getting Users Count
		public int getPostsCount() {
			String countQuery = "SELECT  * FROM " + TABLE_UNITS;
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
		    db.delete(UnitDatabaseController.TABLE_UNITS, null, null);
		    
			db.close();
		}

	
}
