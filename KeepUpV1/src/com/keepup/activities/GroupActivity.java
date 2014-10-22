package com.keepup.activities;

import java.util.ArrayList;
import java.util.List;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.NavigationDrawerFragment;
import com.keepup.group.Group;
import com.keepup.group.GroupDatabaseController;
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
	private GroupDatabaseController groupDb; 
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
		groupDb = new GroupDatabaseController(this);
		DisplayGroups displayGroupsThread = new DisplayGroups();
		displayGroupsThread.execute(String.valueOf(GlobalVariables.USERLOGGEDIN.getId()));
		//displayGroupsThread.execute("8600572");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.global, menu);
			//restoreActionBar();
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
			groupDb.emptyDatabase();
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
		
		case 1:
			mTitle = getString(R.string.news);
			Intent intentHome = new Intent(this, HomeActivity.class);
			startActivity(intentHome);
			break;
			
		case 2:
			mTitle = getString(R.string.units);
			Intent intentUnits = new Intent(this, UnitsActivity.class);
			startActivity(intentUnits);
			break;
			
		case 3:
			mTitle = getString(R.string.groups);
			break;
		}
	}

	public void groupDetails(View v){
		Log.v("KEEPUP", "Clicked on a Group from Group Listing");
		TextView groupName = (TextView) v.findViewById(R.id.group_name);
		String name = (String) groupName.getText();
		//GlobalVariables.USERLOGGEDIN.setUnit(name);
		Intent intent = new Intent(this, IndividualGroupActivity.class);
		intent.putExtra("groupName", name);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}
	public void createGroup (View v) {
		Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
	}
	
	
	
	/* THREADED ACTIVITIES */
	
	public class DisplayGroups extends AsyncTask<String, Void, Integer>{
		int groupCount = 0;
		ArrayList<Group> groupsToDisplay = new ArrayList<Group>();
		
		protected Integer doInBackground(String... params) {
			//Set the # of units we're keeping up with
			
			
			List<Integer> studentNos;
			
			for(Group group: groupDb.getAllGroups()){
				studentNos = group.gatherUsers();
				if(studentNos.contains(GlobalVariables.USERLOGGEDIN.getId()))
					groupsToDisplay.add(group);
			}
			groupCount = groupsToDisplay.size();
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
		private View setUpGroupView(Group group, int i, View rootView) {
			
			//Setup Unit Name.
			TextView groupName = (TextView) rootView.findViewById(R.id.group_name);
			groupName.setText(groupsToDisplay.get(i).getName());
			
			TextView groupMembers = (TextView) rootView.findViewById(R.id.group_members);
			groupMembers.setText("Members: " + groupsToDisplay.get(i).getGroupMembers());
			
			//Setup last announcement.
			TextView groupPost = (TextView) rootView.findViewById(R.id.last_group_post);
			groupPost.setText("Test Post");
			
			//Setup notification counts.
			/*TextView announcementCount = (TextView) rootView.findViewById(R.id.announcement_value_unit);
			announcementCount.setText("x " + String.valueOf(intTests[i][0]));
			TextView postCount = (TextView) rootView.findViewById(R.id.post_value_unit);
			postCount.setText("x " + String.valueOf(intTests[i][1]));
			TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.postsOnYours_value_unit);
			postOnYoursCount.setText("x " + String.valueOf(intTests[i][2]));
		*/
			//Change background colour based on element id.
			//if(i % 2 == 0)
			//	rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
			//else
			//	rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
			 
			return rootView;
		}
	/**
	 * TODO
	 * IF COPIED FROM UNITS ALL THIS NOT REQ
	 * SAVING FOR NOW JUST INCASE.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		private static final String ARG_GROUP_NUMBER = "group_section_number";
		List<Group> allGroups = new ArrayList<Group>();
		
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_GROUP_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_groups_listing,
					container, false);
			
			
			
			int groupCounter = 0;
			for(Group group: groupDb.getAllGroups())
			{
				if (group.gatherUsers().contains(GlobalVariables.USERLOGGEDIN.getId()))
					groupCounter++;
			}
			if(groupCounter == 1)
				noOfGroups.setText(groupCounter + " Group");
			else
				noOfGroups.setText(groupCounter + " Groups");
			
			
			
			
			
			
			
			return rootView;
		}
		
		
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((GroupActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_GROUP_NUMBER));
		}
	}*/

	
	/*public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}*/

	/*public void onNavigationDrawerItemSelected(int position) {
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.groups_toplevel_container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}*/
	
	
		
		
		
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
