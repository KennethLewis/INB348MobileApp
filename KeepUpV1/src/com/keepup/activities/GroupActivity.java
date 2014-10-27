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
		
		//CLICK SETTINGS BUTTON IN ACTION BAR
		if (id == R.id.action_settings) {
			return true;
		}
		
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
			Toast.makeText(this, "# unread notifications.", Toast.LENGTH_SHORT).show();
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

	public void groupDetails(View v){
		TextView groupName = (TextView) v.findViewById(R.id.group_name);
		String name = (String) groupName.getText();
		
		Group clickedGroup = null;
		for(Group g : groupsToDisplay){
			if(g.getName().equals(name)){
				clickedGroup = g;
			}
		}
		Intent intent = new Intent(this, IndividualGroupActivity.class);
		intent.putExtra("GROUP_ID",clickedGroup.getGroupId());
		intent.putExtra("GROUP_NAME", clickedGroup.getName());
		startActivity(intent);
		
	}
	public void createGroup (View v) {
		Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
	}
	
	
	
	/* THREADED ACTIVITIES */
	int groupCount = 0;
	int userCount = 0;
	int[] postsUnread;
	int[] unitMemberCount;
	String[] lastAnnouncement;
	String allUsers;
	ArrayList<String> usersNamesInGroup = new ArrayList<String>(); 
	ArrayList<Group> groupsToDisplay = new ArrayList<Group>();
	public class DisplayGroups extends AsyncTask<String, Void, Integer>{
		
		
		protected Integer doInBackground(String... params) {
			//Set the # of Groups we're keeping up with
			String dbGroups = DatabaseConnector.getGroupsByUser
					(GlobalVariables.USERLOGGEDIN.getId());
			groupCount = DatabaseConnector.getGroupCountByUser(GlobalVariables.USERLOGGEDIN.getId());
			
			//Change the global variable counter to keep news feed updated
			GlobalVariables.GROUPCOUNT = groupCount;
			/*
			 * Have to check if the builder string was null.
			 * If a user wasnt part of any groups the string 
			 * would be null and thus throw errors with the 
			 * nthOccurrence method.
			 */
			if(dbGroups != null){
				lastAnnouncement = new String[groupCount];
				
				int startOffset = 0;
				for(int i = 0; i < groupCount; i++){
					
					Group group = new Group();
					String builderString;
					int endIndex = nthOccurrence(dbGroups, '^', (i+2)*2) + 1;
					/* nthOccurrence wasnt liking the last group because I've
					 * prob fucked something in returning group strings. So if
					 * the group is the last one it hard codes the end of the
					 * index. 
					 */
					if(i == (groupCount -1) )
						builderString = dbGroups.substring(startOffset, (startOffset + 205));
					else
						builderString = dbGroups.substring(startOffset, endIndex);
					
					/* Again prob cause my endIndex = nthOcc is wrong. NumberCounter
					 * Just gets the total string length of the 2 ids to help set the
					 * index at the right place when reading in the next group. 
					 */
					int numberCounter = group.setupGroup(builderString);
					
					groupsToDisplay.add(group);
					lastAnnouncement[i] = DatabaseConnector.getLastPostInGroup(group.getGroupId());
					
					allUsers = DatabaseConnector.getAllUsersFromGroup(group.getGroupId());
					
					if(allUsers != null){
						Log.v("USERS IN GROUP", allUsers);
						userCount = DatabaseConnector.getUserCountByGroup(group.getGroupId());
						String names = "";
						int startUserOffset = 0;
						for(int j =0; j < userCount; j++){
							User user = new User();
							String builderStringUsers;
							int endIndexUser = nthOccurrence(allUsers, '^', (j+1)*2) + 26;
							
							builderStringUsers = allUsers.substring(startUserOffset, endIndexUser);
							Log.v("BUILDERSTRING",builderStringUsers);
							user.setupUser(builderStringUsers);
							names = names + user.getUsername() + " ";
							startUserOffset = endIndexUser;
							
						}
						
						usersNamesInGroup.add(names);
					}
					if(allUsers == null){
						String nullString = "No Users in this Groups";
						usersNamesInGroup.add(nullString);
					}
					//Get unread post information
					//postsUnread[i] = DatabaseConnector.getUnreadPostCountInGroupForUser(
					//		group.getGroupId(), GlobalVariables.USERLOGGEDIN.getId());
					//unitMemberCount[i] = DatabaseConnector.getGroupMemberCount(group.getGroupId());
					
					startOffset = endIndex - numberCounter ;
				}
			}
			return null;
			
		}
		protected void onPostExecute(Integer result){
			
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
			groupMembers.setText("Members: " + usersNamesInGroup.get(index));
			
			//Setup last announcement.
			String announcement = lastAnnouncement[index].substring(0, 
					lastAnnouncement[index].equals("No recent posts") ? lastAnnouncement[index].length() : 50);
			TextView groupPost = (TextView) rootView.findViewById(R.id.last_group_post);
			groupPost.setText(announcement);
			
			//Setup notification counts.
			
			//TextView postCount = (TextView) rootView.findViewById(R.id.post_value_group);
			//postCount.setText("x " + postsUnread[i]);
			//TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.postsOnYours_value_group);
			//postOnYoursCount.setText("x " + unitMemberCount[i]);
		
			 
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
