package com.keepupv1.activities;

import java.util.ArrayList;
import java.util.List;

import com.keepupv1.GlobalVariables;
import com.keepupv1.NavigationDrawerFragment;
import com.keepupv1.R;
import com.keepupv1.group.Group;
import com.keepupv1.group.GroupDatabaseController;
import com.keepupv1.post.Post;
import com.keepupv1.post.PostDatabaseController;
import com.keepupv1.unit.Unit;
import com.keepupv1.unit.UnitDatabaseController;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	private static PostDatabaseController postDb;
	private static UnitDatabaseController unitDb;
	private static GroupDatabaseController groupDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		postDb = new PostDatabaseController(this);
		unitDb = new UnitDatabaseController(this);
		groupDb = new GroupDatabaseController(this);
		mNavigationDrawerFragment.selectItem(0);
		
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.home_toplevel_container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.news);
			break;
		case 2:
			mTitle = getString(R.string.units);
			Intent intentUnits = new Intent(this, UnitsActivity.class);
			startActivity(intentUnits);
			break;
		case 3:
			mTitle = getString(R.string.groups); 
			Intent intentGroups = new Intent(this, GroupActivity.class);
			startActivity(intentGroups);
			break;
		case 4:
			mTitle = getString(R.string.time_table);
			break;
		case 5:
			mTitle = getString(R.string.mail);
			break;
		case 6:
			mTitle = getString(R.string.blackboard);
			break;
		case 7:
			mTitle = getString(R.string.qut_virtual);
			break;
		case 8:
			mTitle = getString(R.string.qut_news);
			break;
		case 9:
			mTitle = getString(R.string.map);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.home, menu);
			restoreActionBar();
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
		
		//CLICK HOME BUTTON -JACK
		if (id == R.id.action_example) {
			Intent intentUnits = new Intent(this, HomeActivity.class);
			startActivity(intentUnits);
			Toast.makeText(this, "# unread notifications.", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private List<Post> postsForUser = new ArrayList<Post>();
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			TextView noOfUnits = (TextView) rootView.findViewById(R.id.news_unit_count);
			TextView noOfGroups = (TextView) rootView.findViewById(R.id.news_group_count);
			
			int groupCounter = 0;
			int unitCounter = 0;
			for(Unit unit: unitDb.getAllUnits())
			{
				if (unit.gatherUsersId().contains(GlobalVariables.USERLOGGEDIN.getId()))
					unitCounter++;
			}
			for(Group group: groupDb.getAllGroups())
			{
				if (group.gatherUsers().contains(GlobalVariables.USERLOGGEDIN.getId()))
					groupCounter++;
			}
			
			noOfUnits.setText(unitCounter + " Units");
			noOfGroups.setText(groupCounter + " Groups");
			LinearLayout newsList = (LinearLayout) rootView.findViewById(R.id.news_post_list);
			
			/**
			 * TODO
			 * MAKE IT SO ONLY THE UNIT AND GROUP POSTS WHICH THE USER IS ENROLLED
			 * IN ARE VISABLE
			 */
			
			for(int i = 0; i < postDb.getAllPosts().size(); i++)  {
				View newsTemplate = inflater.inflate(R.layout.news_post_template, null);
				
				if(GlobalVariables.USERLOGGEDIN != null) {
					if(postDb.getAllPosts().get(i) != null) {
						newsTemplate = setUpNewsArticle(postDb.getAllPosts().get(i), i, newsTemplate);
						newsList.addView(newsTemplate);
						//news.add(newsTemplate);
						//Add to view.
						//((ViewGroup) rootView).addView((View) newsTemplate);
					}
				}
			}
			return rootView;
		}

		private View setUpNewsArticle(Post p, int indexNum, View rootView) {
			
			//Setup Unit Name.
			TextView userName = (TextView) rootView.findViewById(R.id.username);
			userName.setText(p.getUnit() + " " + "by " + p.getUser());
			
			TextView dateTime = (TextView) rootView.findViewById(R.id.date_time);
			dateTime.setText(p.getDate());
			
			TextView post = (TextView) rootView.findViewById(R.id.published_news);
			post.setText(p.getContent());

			 //Change background colour based on element id.
			 if(indexNum % 2 == 0)
				 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
			 else
				 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
			 
			return rootView;
		}
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((HomeActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
		
		
		
	}
	public void goToHome(View v) {
		Intent intent = new Intent(this, HomeActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}
}
