package com.keepup.activities;

import java.util.ArrayList;
import java.util.List;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.NavigationDrawerFragment;
import com.keepup.group.Group;
import com.keepup.unit.Unit;
import com.keepup.user.User;
import com.keepup.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GroupActivity extends Activity implements
NavigationDrawerFragment.NavigationDrawerCallbacks{
	
	private CharSequence mTitle;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.group_navigation_drawer);
		mTitle = getTitle();
		mNavigationDrawerFragment.setUp(R.id.group_navigation_drawer,
				(DrawerLayout) findViewById(R.id.group_drawer_layout));
		
		mNavigationDrawerFragment.selectItem(3);
		
		DisplayGroups displayGroupsThread = new DisplayGroups();
		displayGroupsThread.execute(String.valueOf(GlobalVariables.USERLOGGEDIN.getId()));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.global, menu);
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//CLICK LOGOUT BUTTON
		if (id == R.id.action_logout) {
			GlobalVariables.USERLOGGEDIN = null;
			Intent intent = new Intent(this, LoginActivity.class);
	        startActivity(intent);
			return true;
		}
		
		//CLICK HOME BUTTON -JACK
		if (id == R.id.action_home) {
			Intent intentUnits = new Intent(this, HomeActivity.class);
			startActivity(intentUnits);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void onNavigationDrawerItemSelected(int number) {
		switch (number) {
		case 0:
			mTitle = getString(R.string.news);
			Intent intentHome = new Intent(this, HomeActivity.class);
			startActivity(intentHome);
			break;
			
		case 1:
			mTitle = getString(R.string.units);
			Intent intentUnits = new Intent(this, UnitsActivity.class);
			startActivity(intentUnits);
			break;
			
		case 2:
			mTitle = getString(R.string.groups);
			break;
		}
	}
	public final static String GROUP_NAME = "com.keepup.GROUP_NAME";
	public final static String GROUP_ID = "com.keepup.GROUP_ID";
	public void clickedOnGroup(View v) {
		Group clickedGroup = null;
		for(Group g : groupsToDisplay)
			if(g.getName().equals((String) ((TextView)v.findViewById(R.id.group_name)).getText()))
				clickedGroup = g;
		
		if(clickedGroup == null)
			return;

		//Send information and then go to other Activity.
		Intent intent = new Intent(this, IndividualGroupActivity.class);
		intent.putExtra(GROUP_ID, clickedGroup.getGroupId());
		intent.putExtra(GROUP_NAME, clickedGroup.getName());
		startActivity(intent);
	}
	
	public void createGroup (View v) {
		Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
	}
	
	/* THREADED ACTIVITIES */
	ArrayList<Group> groupsToDisplay = new ArrayList<Group>();
	public class DisplayGroups extends AsyncTask<String, Void, Integer>{
		int groupCount = 0;
		int[] postsUnread;
		int[] groupMemberCount;
		String[] lastAnnouncement;
		String[] memberList;
		
		protected Integer doInBackground(String... params) {
			
			//Set the # of Groups we're keeping up with
			String dbGroups = DatabaseConnector.getGroupsByUser(Integer.parseInt(params[0]));
			groupCount = DatabaseConnector.getGroupCountByUser(Integer.parseInt(params[0]));
			
			//Change the global variable counter to keep news feed updated
			GlobalVariables.GROUPCOUNT = groupCount;
			/*
			 * Have to check if the builder string was null.
			 * If a user wasnt part of any groups the string 
			 * would be null and thus throw errors with the 
			 * nthOccurrence method.
			 */
			if(dbGroups != null) {
				memberList = new String[groupCount];
				lastAnnouncement = new String[groupCount];
				groupMemberCount = new int[groupCount];
				postsUnread = new int[groupCount];
				
				int startOffset = 0;
				for(int i = 0; i < groupCount; i++) {
					memberList[i] = "";
					
					Group group = new Group();
					
					int endIndex = nthOccurrence(dbGroups, '^', (i+1)*2) + 1 + 50 + 150;

					String builderString = dbGroups.substring(startOffset, endIndex);
					group.setupGroup(builderString);
					groupsToDisplay.add(group);
					
					lastAnnouncement[i] = DatabaseConnector.getLastPostInGroup(group.getGroupId());
					groupMemberCount[i] = DatabaseConnector.getUserCountByGroup(group.getGroupId());
					postsUnread[i] = DatabaseConnector.getUnreadPostCountInGroupForUser(group.getGroupId(), Integer.parseInt(params[0]));
					int userCount = DatabaseConnector.getUserCountByGroup(group.getGroupId());
					String allUsers = DatabaseConnector.getAllUsersFromGroup(group.getGroupId());
					
					int startUserOffset = 0;
					for(int j = 0; j < userCount; j++) {
						User user = new User();

						int endIndexUser = nthOccurrence(allUsers, '^', (j+1)*2) + 1 + 25;
						
						String builderStringUsers = allUsers.substring(startUserOffset, endIndexUser);
						user.setupUser(builderStringUsers);
						
						memberList[i] += " - " + user.getUsername();
						startUserOffset = endIndexUser;
					}

					startOffset = endIndex;
				}
			}
			return null;
			
		}
		protected void onPostExecute(Integer result) {
			TextView noOfGroups = (TextView) findViewById(R.id.group_count);
			noOfGroups.setText(groupCount + " Group" + (groupCount > 1 ? "s" : ""));
			
			LayoutInflater inflater = (LayoutInflater) getBaseContext().
					getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	        //ADD UNIT LISTINGS 1 BY 1
			LinearLayout groupList = (LinearLayout)findViewById(R.id.groups_list);
			for(int i = 0; i < groupsToDisplay.size(); i++)  {
				View groupView = inflater.inflate(R.layout.group_template, null);
				 
					groupView = setUpGroupView(groupsToDisplay.get(i), i, groupView);
				 
					//Add to view.
					groupList.addView(groupView);
			}
		}
		
		private View setUpGroupView(Group group, int index, View rootView) {
			
			//Setup Unit Name.
			TextView groupName = (TextView) rootView.findViewById(R.id.group_name);
			groupName.setText(groupsToDisplay.get(index).getName());
			
			TextView groupMembers = (TextView) rootView.findViewById(R.id.group_members);
			groupMembers.setText("Members: " + memberList[index]);
			
			//Setup last announcement.
			String announcement = lastAnnouncement[index].substring(0, 
					lastAnnouncement[index].equals("No recent posts.") ? lastAnnouncement[index].length() : 50).trim();
			TextView announcementLast = (TextView) rootView.findViewById(R.id.last_group_post);
			announcementLast.setText(announcement + "...");
			
			//Setup notification counts.
			TextView postCount = (TextView) rootView.findViewById(R.id.post_value_group);
			postCount.setText("x " + postsUnread[index]);
			TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.membercount_value_group);
			postOnYoursCount.setText("x " + groupMemberCount[index]);
		
			return rootView;
		}
		
	}
	
	//Helper method, understands Strings from MySQL web service results.
	//essentially splits up user ids of variable length.
	public int nthOccurrence(String str, char c, int n) {
	    int pos = str.indexOf(c, 0);
	    n--;
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos + 1);
	    return pos;
	}
}
