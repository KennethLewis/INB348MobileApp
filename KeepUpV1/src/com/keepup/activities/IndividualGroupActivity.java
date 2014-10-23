package com.keepup.activities;

import java.util.ArrayList;
import java.util.List;

import com.keepup.GlobalVariables;
import com.keepup.post.Post;
import com.keepup.post.PostDatabaseController;
import com.keepup.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

public class IndividualGroupActivity extends Activity {

	private PostDatabaseController postDb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_grp);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		//Retrieve title name from where we've clicked from's data.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!= null) {
			String activityName = (String) extras.get("groupName");
			this.setTitle(activityName);
		}
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
	
}
