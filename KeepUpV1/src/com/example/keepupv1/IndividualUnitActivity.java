package com.example.keepupv1;

import group.GroupDatabaseController;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

import post.Post;
import post.PostDatabaseController;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

public class IndividualUnitActivity extends Activity {
	
	//Global database connection
	PostDatabaseController db;
	GroupDatabaseController groupDb;
	private List<Post> posts; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_individual_unit);
		
		//Add fragment (small view that you make and import)
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//Retrieve title name from where we've clicked from's data.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!= null) {
			String activityName = (String) extras.get("unitId");
			this.setTitle(activityName);
		}
		
		//DATABASE TESTING
		db = new PostDatabaseController(this);
		groupDb = new GroupDatabaseController(this);
		
        // Inserting
        Log.d("Post", "Inserting ..");
        //db.addPost(new Post("Jackson P", "Today, 1/1/2014", "Some post content here, lorem ipsum testaroonie."));
        posts = new ArrayList<Post>();
        posts = db.getAllPosts();
		
		LinearLayout postList = (LinearLayout) findViewById(R.id.posts_list);
		for(int i = 0; i < posts.size(); i++)  {
			View rootView = getLayoutInflater().inflate(R.layout.unit_post_template, null);
			
			if(DatabaseVariables.USERLOGGEDIN != null) {
				Post post = db.getPostWithUnit(i, DatabaseVariables.USERLOGGEDIN.getUnit());
				if(post != null) {
					rootView = setupUnitView(post, i, rootView);
				 
					//Add to view.
					postList.addView(rootView);
				}
			}
		}
		
	}
	
	private View setupUnitView(Post p, int indexNum, View rootView) {
		
		//Setup Unit Name.
		TextView userName = (TextView) rootView.findViewById(R.id.user_name);
		userName.setText(p.getUser());
		
		TextView dateTime = (TextView) rootView.findViewById(R.id.date_time);
		dateTime.setText(p.getDate());
		
		TextView post = (TextView) rootView.findViewById(R.id.published_user_post);
		post.setText(p.getContent());

		 //Change background colour based on element id.
		 if(indexNum % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.individual_unit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//CLICK SETTINGS BUTTON IN ACTION BAR
		if (id == R.id.action_settings) {
			db.emptyDatabase();
			Log.v("Button", "Settings button clicked");
			return true;
		}
		
		//CLICK HOME BUTTON -JACK
		if (id == R.id.action_example) {
			Intent intentUnits = new Intent(this, HomeActivity.class);
			startActivity(intentUnits);
			Toast.makeText(this, "# unread notifications.", Toast.LENGTH_SHORT).show();
			Log.v("Button", "Home button clicked");
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void publishUserPost(View v) {
		
		//Hit send it puts the post into the db with deets
		//reload on create with has a list of the current posts
		EditText userPost = (EditText)findViewById(R.id.text_to_publish);

		
		if(DatabaseVariables.USERLOGGEDIN == null)
			return;
			
		String content = userPost.getText().toString();
		Post newPost = new Post(DatabaseVariables.USERLOGGEDIN.getUsername(), 
				"11/11/11", content, DatabaseVariables.USERLOGGEDIN.getUnit());
        
		db.addPost(newPost);
        
        recreate();
		
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
			View rootView = inflater.inflate(R.layout.fragment_individual_unit,
					container, false);
			return rootView;
		}
	}
	
}
