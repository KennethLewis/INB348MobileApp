package com.keepup.activities;

import java.util.ArrayList;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.NavigationDrawerFragment;
import com.keepup.R;
import com.keepup.activities.IndividualUnitActivity.UpdatePostData;
import com.keepup.activities.UnitsActivity.DisplayUnits;
import com.keepup.group.Group;
import com.keepup.post.Post;
import com.keepup.unit.Unit;
import com.keepup.user.User;

import android.app.Activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		TextView noOfUnits = (TextView) findViewById(R.id.news_unit_count);
		TextView noOfGroups = (TextView) findViewById(R.id.news_group_count);
		
		
		//@EDIT
		noOfUnits.setText("Units: " + GlobalVariables.UNITCOUNT);
		noOfGroups.setText("Groups: " + GlobalVariables.GROUPCOUNT);
		//Navigation Drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.home_layout));
		mNavigationDrawerFragment.selectItem(0);
	
		
		DisplayUnits displayUnitsThread = new DisplayUnits();
		displayUnitsThread.execute(String.valueOf(GlobalVariables.USERLOGGEDIN.getId()));
		
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position) {
		case 0:
			mTitle = getString(R.string.news);
			break;
		case 1:
			mTitle = getString(R.string.units);
			Intent intentUnits = new Intent(this, UnitsActivity.class);
			startActivity(intentUnits);
			break;
		case 2:
			mTitle = getString(R.string.groups); 
			Intent intentGroups = new Intent(this, GroupActivity.class);
			startActivity(intentGroups);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	public void moveTo(View v) {
		TextView whereTo = (TextView) v.findViewById(R.id.unit_group_user_title);
		String goingTo = whereTo.getText().toString();
		String [] brokenDirection = goingTo.split("by");
		String finalDestination = brokenDirection[0];
		
		if(finalDestination.contains("Unit")){
			Intent intent = new Intent(this, IndividualUnitActivity.class);
			String [] unitName = finalDestination.split(":");
			String trimedName = unitName[1].trim();
			intent.putExtra("unitId", trimedName);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
		}
		else if(finalDestination.contains("Group")){
			Intent intent = new Intent(this, IndividualGroupActivity.class);
			String [] groupName = finalDestination.split(":");
			String trimedName = groupName[1].trim();
			Toast.makeText(this, finalDestination, Toast.LENGTH_SHORT).show();
			intent.putExtra("groupName", trimedName);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.global, menu);
			//restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	
	public void goToHome(View v) {
		Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
	}
	
	protected void updateNewsView() {
		LayoutInflater inflater = (LayoutInflater) getBaseContext().
				getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //ADD UNIT LISTINGS 1 BY 1
		
		LinearLayout unitNewsList = (LinearLayout) findViewById(R.id.news_post_list);
		
		for(int i = GlobalVariables.POSTS.size() - 1; i >= 0; i--)  {
			View unitView = inflater.inflate(R.layout.news_post_template, null);
			unitView = setUpNewsArticle(GlobalVariables.POSTS.get(i), i, unitView);
			//Add to view.
			unitNewsList.addView(unitView);
		}
    }
		
	private View setUpNewsArticle(Post p, int indexNum, View rootView) {
		//Setup Unit Name.
		User user = new User ();
		Unit unit = new Unit();
		Group group = new Group();
		
		for(User u: GlobalVariables.USERSWHOPOSTED){
			if (p.getUserId() == u.getId())
				user = u;
		}
		for(Unit u: GlobalVariables.UNITSWITHPOSTS){
			if(p.getUnitId() == u.getId())
				unit = u;
		}
		
		for(Group g: GlobalVariables.GROUPSWITHPOSTS){
			if(p.getGroupId() == g.getGroupId())
				group = g;
		}
		String nameOfGroupOrUnit = null;
		
		if(unit.getName() != null)
			nameOfGroupOrUnit = unit.getName().trim();
		
		boolean unitOrGroup = false;
		if(nameOfGroupOrUnit == null){
			nameOfGroupOrUnit = group.getName().trim();
			unitOrGroup = true;
		}
		
		String title;
		if(unitOrGroup == true)
			title = "Group";
		else
			title = "Unit";
		
		TextView userName = (TextView) rootView.findViewById(R.id.unit_group_user_title);
		userName.setText(title + ": " + nameOfGroupOrUnit + " " + "by " + user.getUsername());
		
		TextView dateTime = (TextView) rootView.findViewById(R.id.date_time);
		dateTime.setText(p.getTime());
		
		TextView post = (TextView) rootView.findViewById(R.id.published_news);
		post.setText(p.getContent());

		 //Change background colour based on element id.
		 if(indexNum % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
	}

	/* ---------------- THREADED TASKS ----------------- */
	
	public class DisplayUnits extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			//Set the # of units we're keeping up with
			
			return null;
		}
		protected void onPostExecute(Integer result) {
			if(GlobalVariables.POSTS.size() > 0)
				updateNewsView();
        }
		
		public int nthOccurrence(String str, char c, int n) {
		    int pos = str.indexOf(c, 0);
		    n--;
		    while (n-- > 0 && pos != -1)
		        pos = str.indexOf(c, pos + 1);
		    return pos;
		}
	}
}
