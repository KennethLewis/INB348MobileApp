package com.keepupv1.group;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.keepupv1.post.Post;
import com.keepupv1.user.User;

public class GroupDatabaseController extends SQLiteOpenHelper {

	// All Static variables
		// Database Version
		private static final int DATABASE_VERSION = 2;

		// Database Name
		private static final String DATABASE_NAME = "AllGroups.db";

		// Users table name
		private static final String TABLE_GROUPS = "Groups";

		// Users Table Columns names
		private static final String KEY_ID = "groupId";
		private static final String KEY_GROUP_NAME = "username";
		private static final String KEY_GROUP_MEMBERS = "members";
		private static final String KEY_GROUP_MEMBERS_STUDENTNO = "membersStudentNo";
		private static final String KEY_GROUP_DESCRIPTION = "discription";
		

		public GroupDatabaseController(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		// Creating Tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_GROUPS + "("
					+ KEY_ID + " INTEGER PRIMARY KEY,"
					+ KEY_GROUP_NAME + " TEXT," 
					+ KEY_GROUP_MEMBERS + " TEXT,"
					+ KEY_GROUP_MEMBERS_STUDENTNO + " TEXT,"
					+ KEY_GROUP_DESCRIPTION + " TEXT" 
					+ ")";
			db.execSQL(CREATE_GROUPS_TABLE);
		}

		// Upgrading database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);

			// Create tables again
			onCreate(db);
		}

		// Adding new User
		public void addGroup(Group group) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_GROUP_NAME, group.getName());
			values.put(KEY_GROUP_MEMBERS, group.getGroupMembers());
			values.put(KEY_GROUP_MEMBERS_STUDENTNO, group.getMemberStudentId());
			values.put(KEY_GROUP_DESCRIPTION, group.getGroupDescription());
			
			// Inserting Row
			db.insert(TABLE_GROUPS, null, values);
			db.close(); // Closing database connection
		}

		// Getting single User
		public Group getGroup(String name) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_GROUPS, new String[] { KEY_ID,
					KEY_GROUP_NAME, KEY_GROUP_MEMBERS, KEY_GROUP_MEMBERS_STUDENTNO, KEY_GROUP_DESCRIPTION }, KEY_GROUP_NAME + "=?",
					new String[] { String.valueOf(name) }, null, null, null, null);

			Group group = null;
			if (cursor != null)
				if(cursor.moveToFirst())
					group = new Group(cursor.getString(0), cursor.getString(1), 
							cursor.getString(2), cursor.getString(3));
		    
			db.close();
			// return User
			return group;
		}

		// Getting single User
		public Group getGroupWithUser(String userName) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_GROUPS, new String[] {KEY_ID,
					KEY_GROUP_NAME, KEY_GROUP_MEMBERS, KEY_GROUP_MEMBERS_STUDENTNO, KEY_GROUP_DESCRIPTION}, KEY_GROUP_MEMBERS + "=?",
					new String[] { userName }, null, null, null, null);
			
			Group group = null;
			if (cursor != null)
				if(cursor.moveToFirst())
					group = new Group(cursor.getString(0), cursor.getString(1), 
							cursor.getString(2), cursor.getString(3));
			
			db.close();
			// return User
			return group;
		}
		
		// Getting All Users
		public List<Group> getAllGroups() {
			List<Group> allGroups = new ArrayList<Group>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_GROUPS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Group group = new Group(cursor.getString(1),cursor.getString(2),
							cursor.getString(3), cursor.getString(4));
					// Adding User to list
					allGroups.add(group);
				} while (cursor.moveToNext());
			}

			db.close();
			// return User list
			return allGroups;
		}

		// Deleting single User
		public void deleteUser(User User) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_GROUPS, KEY_GROUP_NAME + " = ?",
					new String[] { String.valueOf(User.getId()) });
			db.close();
		}


		// Getting Users Count
		public int getPostsCount() {
			String countQuery = "SELECT  * FROM " + TABLE_GROUPS;
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
		    db.delete(GroupDatabaseController.TABLE_GROUPS, null, null);
		    
			db.close();
		}

	
}
