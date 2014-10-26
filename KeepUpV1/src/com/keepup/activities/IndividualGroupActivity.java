package com.keepup.activities;

import java.util.ArrayList;
import java.util.List;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.NavigationDrawerFragment;
import com.keepup.activities.IndividualUnitActivity.PublishPost;
import com.keepup.activities.IndividualUnitActivity.UpdatePostData;
import com.keepup.post.Post;
import com.keepup.post.PostDatabaseController;
import com.keepup.user.User;
import com.keepup.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IndividualGroupActivity extends Activity implements
NavigationDrawerFragment.NavigationDrawerCallbacks {

	private PostDatabaseController postDb;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_grp);
		
		//Retrieve title name from where we've clicked from's data.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!= null) {
			String activityName = (String) extras.get("GROUP_NAME");
			this.setTitle(activityName);
			currentGroupId = (Integer) extras.getInt("GROUP_ID");
			Log.v("GROUPID",String.valueOf(currentGroupId));
		}
		//Navigation Drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.individual_group_layout));
				
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
			
	public void publishGroupUserPost(View v) {
		PublishPost publishPostThread = new PublishPost();
		publishPostThread.execute(((TextView) findViewById(R.id.group_text_to_publish)).getText().toString());
		Log.v("KEEPUP", "Clicked to submit a post to Group: ");
	}
			
	//Method to show the unit options and enable them to be clicked.
	protected void updatePostViews() {
		LayoutInflater inflater = (LayoutInflater) getBaseContext().
				getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		   //ADD UNIT LISTINGS 1 BY 1
		LinearLayout unitList = (LinearLayout) findViewById(R.id.group_posts_list);
		unitList.removeAllViews();
				
		for(int i = 0; i < groupPosts.size(); i++)  {
			View unitView = inflater.inflate(R.layout.group_post_template, null);

			unitView = setupPostView(groupPosts.get(i), i, unitView);
				 
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
	int currentGroupId = 0;
	int postCount = 0;
	ArrayList<Post> groupPosts = new ArrayList<Post>();
	User[] postOwners;
	public class UpdatePostData extends AsyncTask<String, Void, Integer> {
				
				@Override
		protected Integer doInBackground(String... params) {
			//Set the # of units we're keeping up with
			postCount = DatabaseConnector.getPostCountInGroup(currentGroupId);
			Log.v("GROUPID",String.valueOf(currentGroupId));
			Log.v("GROUP POST COUNTER",String.valueOf(postCount));		
			groupPosts.clear();
			postOwners = new User[postCount];
					
			int startOffset = 0;
			String getPostsString = DatabaseConnector.getPostsInGroup(currentGroupId, 
					GlobalVariables.USERLOGGEDIN.getId(), true);
			
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
						
				groupPosts.add(post);
				GlobalVariables.GROUPPOSTS.add(post); //Add post to complete list for news display
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
			if(DatabaseConnector.addPostToGroup(GlobalVariables.USERLOGGEDIN.getId(),14, currentGroupId, params[0])) {
				requiresRefresh = true;
				return true;
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			if(result) {
				((TextView) findViewById(R.id.group_text_to_publish)).setText("");
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		postDb = new PostDatabaseController(this);
        // Inserting
        Log.d("Post", "Inserting ..");
		List<Post> postWithGroup = new ArrayList<Post>();
		
		
		LinearLayout postList = (LinearLayout) findViewById(R.id.group_posts_list);
		for(int i = 0; i < postDb.getAllPosts().size(); i++)  {
			View rootView = getLayoutInflater().inflate(R.layout.group_post_template, null);
			if(GlobalVariables.USERLOGGEDIN != null) {
				if(postDb.getAllPosts() != null) {
					rootView = setupUnitView(postDb.getAllPosts().get(i), i, rootView);
				 
					//Add to view.
					postList.addView(rootView);
				}
			}
		}
	}

	private View setupUnitView(Post p, int indexNum, View rootView) {
		
		//Setup Unit Name.
		TextView userName = (TextView) rootView.findViewById(R.id.username);
		userName.setText("Sample User");
		
		TextView dateTime = (TextView) rootView.findViewById(R.id.date_time);
		dateTime.setText(p.getTime());
		
		TextView post = (TextView) rootView.findViewById(R.id.published_user_post);
		post.setText(p.getContent());

		 //Change background colour based on element id.
		 if(indexNum % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
	}
	
	public void publishUserPost(View v) {
		
//		//Hit send it puts the com.keepup.post into the db with deets
//		//reload on create with has a list of the current posts
		EditText userPost = (EditText)findViewById(R.id.post_to_publish_to_grp);

		Time dateTime = new Time(Time.getCurrentTimezone());
		dateTime.setToNow();
		String postTime = dateTime.monthDay + "/" + dateTime.month + "/"
					+ dateTime.year + " at " + dateTime.hour + ":" +
					dateTime.minute;
		if(GlobalVariables.USERLOGGEDIN == null)
			return;
		
		String content = userPost.getText().toString();
		Post newPost = new Post(this.getTitle().toString(),
				GlobalVariables.USERLOGGEDIN.getId(), postTime, content);
				
        
		postDb.addPost(newPost);
        
        recreate();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
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
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
		/*
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_individual_group,
					container, false);
			return rootView;
		}
	}
	
}*/
