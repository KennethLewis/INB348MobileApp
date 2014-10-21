package com.keepup.activities;

import java.util.ArrayList;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.NavigationDrawerFragment;
import com.keepup.R;
import com.keepup.activities.UnitsActivity.AddUnitToUser;
import com.keepup.activities.UnitsActivity.RemoveUnitFromUser;
import com.keepup.post.Post;
import com.keepup.unit.Unit;
import com.keepup.user.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class IndividualUnitActivity extends Activity implements
	NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_unit);
		
		//Grab information from what Unit we're going into.
		Intent intent = getIntent();
		currentUnitId = intent.getIntExtra(UnitsActivity.UNIT_ID, -1);
		
		//Navigation Drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.individual_unit_layout));
		
		UpdatePostData updatePostDataThread = new UpdatePostData();
		updatePostDataThread.execute(String.valueOf(GlobalVariables.USERLOGGEDIN.getId()));
	}

	//NAVIGATION AND ACTION BAR
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
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position) {
		case 0:
			Intent intentHome = new Intent(this, HomeActivity.class);
			startActivity(intentHome);
			break;
		case 1:
			Intent intentUnits = new Intent(this, UnitsActivity.class);
			startActivity(intentUnits);
			break;
		case 2:
			Intent intentGroups = new Intent(this, GroupActivity.class);
			startActivity(intentGroups);
			break;
		}
	}
	
	public void publishUserPost(View v) {
		PublishPost publishPostThread = new PublishPost();
		publishPostThread.execute(((TextView) findViewById(R.id.text_to_publish)).getText().toString());
		Log.v("KEEPUP", "Clicked to submit a post to Unit: ");
	}
	
	//Method to show the unit options and enable them to be clicked.
	protected void updatePostViews() {
		LayoutInflater inflater = (LayoutInflater) getBaseContext().
				getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //ADD UNIT LISTINGS 1 BY 1
		LinearLayout unitList = (LinearLayout) findViewById(R.id.posts_list);
		unitList.removeAllViews();
		
		for(int i = 0; i < unitPosts.size(); i++)  {
			View unitView = inflater.inflate(R.layout.unit_post_template, null);

			unitView = setupPostView(unitPosts.get(i), i, unitView);
		 
			//Add to view.
			unitList.addView(unitView);
		}
    }
	
	private View setupPostView(Post post, int indexNum, View rootView) {
		//Setup post's user
		TextView userName = (TextView) rootView.findViewById(R.id.username);
		userName.setText(postOwners[indexNum].getUsername());
		
		TextView dateTime = (TextView) rootView.findViewById(R.id.date_time);
		dateTime.setText(post.getTime());
		
		TextView postText = (TextView) rootView.findViewById(R.id.published_user_post);
		postText.setText(post.getContent());

		 //Change background colour based on element id.
		 if(indexNum % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
	}

	/* THREADED ACTIVITIES */
	int currentUnitId = 0;
	int postCount = 0;
	ArrayList<Post> unitPosts = new ArrayList<Post>();
	User[] postOwners;
	public class UpdatePostData extends AsyncTask<String, Void, Integer> {
		
		@Override
		protected Integer doInBackground(String... params) {
			//Set the # of units we're keeping up with
			postCount = DatabaseConnector.getPostCountInUnit(currentUnitId);
			
			unitPosts.clear();
			postOwners = new User[postCount];
			
			int startOffset = 0;
			String getPostsString = DatabaseConnector.getPostsInUnit(currentUnitId, GlobalVariables.USERLOGGEDIN.getId());
			for(int i = 0; i < postCount; i++) {
				Post post = new Post();

				int endIndex = nthOccurrence(getPostsString, '^', (i+1)*5) + 1 + 512;

				String builderString = getPostsString.substring(startOffset, endIndex);
				
				//Log.v("KEEPUP", String.valueOf(endIndex));
				//Log.v("KEEPUP", String.valueOf(startOffset));
				//Log.v("KEEPUP", builderString);
				
				post.setupPost(builderString);
				
				//Fetch and create a User object for the posts
				User postOwner = new User();
				postOwner.setupUser(DatabaseConnector.getUser(post.getUserId()));
				postOwners[i] = postOwner;
				
				unitPosts.add(post);
				GlobalVariables.POSTS.add(post); //Add post to complete list for news display
				startOffset = endIndex;
			}
			return postCount;
		}

		protected void onPostExecute(Integer result) {
			if(postCount > 0)
				updatePostViews();
			//if(requiresRefresh)
				//recreate();
        }
	}

	boolean requiresRefresh = false;
	public class PublishPost extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			//Let's post to the database online.
			if(DatabaseConnector.addPostToUnit(GlobalVariables.USERLOGGEDIN.getId(), currentUnitId, 0, params[0])) {
				requiresRefresh = true;
				return true;
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			if(result) {
				((TextView) findViewById(R.id.text_to_publish)).setText("");
				UpdatePostData updatePostDataThread = new UpdatePostData();
				updatePostDataThread.execute(String.valueOf(GlobalVariables.USERLOGGEDIN.getId()));
			}
        }
	}

	//Helper method, understands Strings from MySQL web service results.
	//essentially splits up user ids of variable length
	public int nthOccurrence(String str, char c, int n) {
	    int pos = str.indexOf(c, 0);
	    n--;
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos + 1);
	    return pos;
	}
	
}
