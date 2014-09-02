package com.example.keepupv1;

import com.example.keepupv1.R.string;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class UnitsActivity extends Activity {

	private String[][] stringTests =   {{"INB100 - UnitName1", "Test announcement Lorem ipsum1"}, 
										{"INB123 - UnitName2", "Test announcement Lorem ipsum2"}, 
										{"INB270 - UnitName3", "Test announcement Lorem ipsum3"}, 
										{"INB380 - UnitName4", "Test announcement Lorem ipsum4"}};
	private int[][] intTests = {{1,2,3}, {4,5,6}, {5,4,3}, {2,1,0}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_units_activity2);
		
		//Add fragment (small view that you make and import)
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.units_top_container, new PlaceholderFragment()).commit();
		}
		 
		 LinearLayout unitList = (LinearLayout) findViewById(R.id.units_list);
		 for(int i = 0; i < intTests.length; i++)  {
			 View rootView = getLayoutInflater().inflate(R.layout.unit_template, null);
			 
			 rootView = setupUnitView(i, rootView);
			 
		 	//Add to view.
			 unitList.addView(rootView);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.units_activity2, menu);
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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_units_activity2,
					container, false);
			return rootView;
		}
	}
	
}
