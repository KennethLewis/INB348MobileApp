package com.keepupv1.activities;


import java.util.ArrayList;
import java.util.List;


import com.keepupv1.GlobalVariables;
import com.keepupv1.NavigationDrawerFragment;
import com.keepupv1.R;
import com.keepupv1.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.keepupv1.R.color;
import com.keepupv1.R.id;
import com.keepupv1.R.layout;
import com.keepupv1.R.menu;
import com.keepupv1.R.string;
import com.keepupv1.unit.Unit;
import com.keepupv1.unit.UnitDatabaseController;
import com.keepupv1.user.User;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UnitsActivity extends Activity implements 
NavigationDrawerFragment.NavigationDrawerCallbacks{
	
	//Global database connection
	private static UnitDatabaseController unitDb;
	private List<Unit> selectedUnits = new ArrayList<Unit>();
	
	private CharSequence mTitle;
	private NavigationDrawerFragment uNavigationDrawerFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_units);
		
		//Add fragment (small view that you make and import)
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.units_top_container, new PlaceholderFragment()).commit();
		}*/
		uNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.units_navigation_drawer);
		mTitle = getTitle();
		uNavigationDrawerFragment.setUp(R.id.units_navigation_drawer,
				(DrawerLayout) findViewById(R.id.unit_drawer_layout));

		//DATABASE TESTING
		unitDb = new UnitDatabaseController(this);
		uNavigationDrawerFragment.selectItem(1);
	}
	
	
	//Displays the list of units to enable selection
	public void showUnits(View v){
		
		switch (v.getId()){
		
		case R.id.select_units:
			showUnitOptions();
			break;
			
		default:
		break;
		}
	}
	
	//Method to show the unit options and enable them to be clicked.
	protected void showUnitOptions(){
		
		boolean[] checkedUnits = new boolean[unitDb.getAllUnits().size()];
		int count = unitDb.getAllUnits().size();
		
		for(int i = 0; i < count; i++)
			checkedUnits[i] = selectedUnits.contains(unitDb.getAllUnits().get(i));
		
		DialogInterface.OnMultiChoiceClickListener 
			unitsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked)
						selectedUnits.add(unitDb.getAllUnits().get(which));
					else
						selectedUnits.remove(unitDb.getAllUnits().get(which));
					
					onChangeSelectedUnits();
				}
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Units");
			
			CharSequence [] unitNames = new CharSequence[unitDb.getAllUnits().size()];
			for(int i =0; i < unitDb.getAllUnits().size(); i++)
				unitNames[i] = unitDb.getAllUnits().get(i).getName();
			
			builder.setMultiChoiceItems(unitNames, checkedUnits, unitsDialogListener);
			
			
			AlertDialog dialog = builder.create();
			dialog.show();
					
	}
	
	//Will refresh the users page once unit is selected
	public void onChangeSelectedUnits(){
		//ADD UNITS TO PAGE, MAYBE REFRESH?
		for(Unit unit: selectedUnits){
			if(GlobalVariables.USERLOGGEDIN.getAllSubjects().contains(unit) == false){
				GlobalVariables.USERLOGGEDIN.addSubject(unit);
				//Add this user to the Unit
				Unit unitCopy = unit;
				unitCopy.addUserToUnit(GlobalVariables.USERLOGGEDIN);
				Unit newUnit = new Unit (unitCopy.getUnitCode(),unitCopy.getName(),
								unitCopy.getAllUsersNames(), unitCopy.getAllUsersStudentId());
				unitDb.deleteUnit(unit);
				unitDb.addUnit(newUnit);
			}
		}
		this.recreate();
	}
	
	
	public void unitDetails(View v){
		
		TextView unitId = (TextView) v.findViewById(R.id.unitcode_code);
		String id = (String) unitId.getText();
		GlobalVariables.USERLOGGEDIN.setUnit(id);
		Intent intent = new Intent(this, IndividualUnitActivity.class);
		intent.putExtra("unitId", id);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.units_activity, menu);
		//return true;
		if (!uNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.units_activity, menu);
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

		private static final String ARG_SECTION_NUMBER = "unit_section_number";

		private int[][] intTests = {{1,2,3}, {4,5,6}, {5,4,3}, {2,1,0}};
		private String [] stringTests = {"Test announcement Lorem ipsum1"};
		private List<Unit> unitsToDisplay = new ArrayList<Unit>();
		
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
			View rootView = inflater.inflate(R.layout.fragment_units,
					container, false);
			
			List<Integer> studentsIdNo;
			for(Unit unit: unitDb.getAllUnits()){
				studentsIdNo = unit.gatherUsersId();
				if(studentsIdNo.contains(GlobalVariables.USERLOGGEDIN.getId()))
					unitsToDisplay.add(unit);
			}
	        //ADD UNIT LISTINGS 1 BY 1
			LinearLayout unitList = (LinearLayout) rootView.findViewById(R.id.units_list);
			
			for(int i = 0; i < unitsToDisplay.size(); i++)  {
				View unitView = inflater.inflate(R.layout.unit_template, null);
				 
				User user = GlobalVariables.USERLOGGEDIN;
				if(user != null) {
					unitView = setupUnitView(i, unitView);
				 
					//Add to view.
					unitList.addView(unitView);
				}
			}
			return rootView;
		}
		
		private View setupUnitView(int i, View rootView) {
			
			//Setup Unit Name.
			TextView unitCode = (TextView) rootView.findViewById(R.id.unitcode_code);
			unitCode.setText(unitsToDisplay.get(i).getUnitCode() + " - " + unitsToDisplay.get(i).getName());
			
			//TextView unitName = (TextView) rootView.findViewById(R.id.unitname_unit);
			//unitName.setText(unitsToDisplay.get(i).getName());
			
			//Setup last announcement.
			TextView announcementLast = (TextView) rootView.findViewById(R.id.announcement_last_unit);
			announcementLast.setText(stringTests[0]);
			
			//Setup notification counts.
			TextView announcementCount = (TextView) rootView.findViewById(R.id.announcement_value_unit);
			announcementCount.setText("x " + String.valueOf(intTests[i][0]));
			TextView postCount = (TextView) rootView.findViewById(R.id.post_value_unit);
			postCount.setText("x " + String.valueOf(intTests[i][1]));
			TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.postsOnYours_value_unit);
			postOnYoursCount.setText("x " + String.valueOf(intTests[i][2]));

			 //Change background colour based on element id.
			 //if(i % 2 == 0)
				 //rootView.findViewById(R.id.unit_template_content).setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
			 //else
				 //rootView.findViewById(R.id.unit_template_content).setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
			 
			return rootView;
		}
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((UnitsActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.news);
			Intent intentHome = new Intent(this, HomeActivity.class);
			startActivity(intentHome);
			break;
			
		case 2:
			mTitle = getString(R.string.units);
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
	public void onNavigationDrawerItemSelected(int position) {
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.units_toplevel_container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}
}
