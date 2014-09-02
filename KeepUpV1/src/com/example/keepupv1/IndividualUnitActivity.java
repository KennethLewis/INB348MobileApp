package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.user.User;

import post.Post;
import post.PostDatabaseHandler;
import android.R.layout;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class IndividualUnitActivity extends Activity {

	
	private Post testPost = new Post ("TestUser", "Thursday 12:01pm", 
			"This is a test post which has been added automatically");
	private ArrayList<Post> allPosts = new ArrayList<Post>();
	
	public IndividualUnitActivity (){
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allPosts.add(testPost);
		setContentView(R.layout.activity_individual_unit);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if(extras!= null)
		{
			String activityName = (String) extras.get("unitId");
			this.setTitle(activityName);
		}
		
		
		LinearLayout postList = (LinearLayout) findViewById(R.id.posts_list);
		
		// Reading all contacts
       // Log.d("Post", "Reading all posts..");
        //List<Post> posts = DBVariableHolder.POSTDATABASEHANDLER.getAllPosts(); 
		for (Post publish : DBVariableHolder.allPosts){
			View rootView = getLayoutInflater().inflate(R.layout.unit_post_template, null);
			 
			rootView = setupUnitView(publish, rootView);
			 
		 	//Add to view.
			postList.addView(rootView);
		}
		
	}
	
	private View setupUnitView(Post p, View rootView) {
		
		//Setup Unit Name.
		TextView userName = (TextView) rootView.findViewById(R.id.user_name);
		userName.setText(p.getStudent());
		
		TextView dateTime = (TextView) rootView.findViewById(R.id.date_time);
		dateTime.setText(p.getTestDate());
		
		TextView post = (TextView) rootView.findViewById(R.id.published_user_post);
		post.setText(p.getPost());
		 
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void publishUserPost(View v){
		
		//Hit send it puts the post into the db with deets
		//reload on create with has a list of the current posts
		EditText userPost = (EditText)findViewById(R.id.text_to_publish);
		String post = userPost.toString();
		String shit = userPost.getText().toString();
		Post newPost = new Post("User", "11/11/11", shit);
        
		DBVariableHolder.allPosts.add(newPost);
        // Inserting Contacts
        //Log.d("Post", "Inserting ..");
        //DBVariableHolder.POSTDATABASEHANDLER.addPost(newPost);
        
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
