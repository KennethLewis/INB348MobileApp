package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import post.Post;

import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UnitsActivity extends Activity {
	
	//Global database connection
	UserDatabaseController db;

	private String[][] stringTests =   {{"INB100", "Test announcement Lorem ipsum1"}, 
										{"INB123", "Test announcement Lorem ipsum2"}, 
										{"INB270", "Test announcement Lorem ipsum3"}, 
										{"INB380", "Test announcement Lorem ipsum4"}};
	private int[][] intTests = {{1,2,3}, {4,5,6}, {5,4,3}, {2,1,0}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_units_activity);
		
		//Add fragment (small view that you make and import)
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.units_top_container, new PlaceholderFragment()).commit();
		}

		//DATABASE TESTING
		db = new UserDatabaseController(this);
		
        // Inserting
		/**
		 * PLEASE COMMENT IN/OUT TO CHANGE STUDENTS AT THIS STAGE TO ENABLE
		 * POSTING.
		 */
        Log.d("User", "Inserting ..");
        db.addUser(new User(1, "Jacksane", "insidesin@live.com.au", 0, "INB270"));
        db.addUser(new User(1, "Jacksane", "insidesin@live.com.au", 0, "INB100"));
        //db.addUser(new User(2, "Kenneth", "kenneth@live.com.au", 0, "INB270"));
        //db.addUser(new User(2, "Kenneth", "kenneth@live.com.au", 0, "INB123"));
        
		 
        //ADD UNIT LISTINGS 1 BY 1
		LinearLayout unitList = (LinearLayout) findViewById(R.id.units_list);
		for(int i = 0; i < stringTests.length; i++)  {
			View rootView = getLayoutInflater().inflate(R.layout.unit_template, null);
			 
			User user = db.getUserWithUnit(1, stringTests[i][0]);
			if(user != null) {
				rootView = setupUnitView(i, rootView);
			 
				//Add to view.
				unitList.addView(rootView);
			}
		}
		
	}
	
	private View setupUnitView(int i, View rootView) {
		
		//Setup Unit Name.
		TextView unitName = (TextView) rootView.findViewById(R.id.unitname_unit);
		unitName.setText(stringTests[i][0]);
		
		//Setup last announcement.
		TextView announcementLast = (TextView) rootView.findViewById(R.id.announcement_last_unit);
		announcementLast.setText(stringTests[i][1]);
		
		//Setup notification counts.
		TextView announcementCount = (TextView) rootView.findViewById(R.id.announcement_value_unit);
		announcementCount.setText("x " + String.valueOf(intTests[i][0]));
		TextView postCount = (TextView) rootView.findViewById(R.id.post_value_unit);
		postCount.setText("x " + String.valueOf(intTests[i][1]));
		TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.postsOnYours_value_unit);
		postOnYoursCount.setText("x " + String.valueOf(intTests[i][2]));

		 //Change background colour based on element id.
		 if(i % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
	}
	
	public void unitDetails(View v){
		
		TextView unitId = (TextView) v.findViewById(R.id.unitname_unit);
		String id = (String) unitId.getText();
		Intent intent = new Intent(this, IndividualUnitActivity.class);
		intent.putExtra("unitId", id);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.units_activity, menu);
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

	//A placeholder fragment containing a simple view.
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_units_activity,
					container, false);
			return rootView;
		}
	}
	
}
