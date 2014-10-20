package com.keepup.activities;

import java.util.ArrayList;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.NavigationDrawerFragment;
import com.keepup.R;
import com.keepup.unit.Unit;

import android.app.ActionBar;
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

public class UnitsActivity extends Activity implements
	NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_units);
		
		//Navigation Drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.units_layout));
		mNavigationDrawerFragment.selectItem(1);
		
		DisplayUnits displayUnitsThread = new DisplayUnits();
		displayUnitsThread.execute("8600571");//String.valueOf(GlobalVariables.USERLOGGEDIN.getId()));
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
	
	public void clickedUnitName(View v) {
		Log.v("KEEPUP", "Clicked on a Unit from Unit Listing");
	}
	
	//HANDLE ALL ADD/REMOVE AND USER UNIT ALTERATIONS
	public void clickAddRemoveUnits(View v) {
		PopupAllUnits popupAllUnitsThread = new PopupAllUnits();
		popupAllUnitsThread.execute();
	}
	
	//Method to show the unit options and enable them to be clicked.
	protected void showUnitOptions() {
		//Remove those that aren't valid to our user.
		for(int i = 0; i < unitsInWindow.size(); i++) {
			if(unitsInWindow.get(i).getUserId() != GlobalVariables.USERLOGGEDIN.getId()) {
				unitsInWindow.remove(i);
				i--;
			}
		}
		//Check those of the remaining units to say we're already in them.
		boolean[] checkedUnits = new boolean[distinctUnits.length];
		for(int i = 0; i < distinctUnits.length; i++) {
			for(Unit u : unitsInWindow)
		        if(u.getCode().equals(distinctUnits[i].getCode()))
					checkedUnits[i] = true;
		}
		
		DialogInterface.OnMultiChoiceClickListener unitsDialogListener = 
				new DialogInterface.OnMultiChoiceClickListener() {
			//Let's click on a Unit.
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if(isChecked) {
					AddUnitToUser addUnitToUserThread = new AddUnitToUser();
					addUnitToUserThread.execute(distinctUnits[which]);
					Log.v("KEEPUP", "ADD");
				} else {
					RemoveUnitFromUser removeUnitFromUserThread = new RemoveUnitFromUser();
					removeUnitFromUserThread.execute(distinctUnits[which]);
					Log.v("KEEPUP", "REMOVE");
				}
			}
		};
			
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add/remove Units");
		
		CharSequence[] unitNames = new CharSequence[distinctUnits.length];
		for(int i = 0; i < distinctUnits.length; i++)
			unitNames[i] = distinctUnits[i].getCode() + " - " + distinctUnits[i].getName();
		
		builder.setMultiChoiceItems(unitNames, checkedUnits, unitsDialogListener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/* THREADED ACTIVITIES */
	int totalUnitCount = 0;
	Unit[] distinctUnits;
	ArrayList<Unit> unitsInWindow = new ArrayList<Unit>();
	public class PopupAllUnits extends AsyncTask<String, Void, Integer> {
		
		@Override
		protected Integer doInBackground(String... params) {
			//Set the # of units we're keeping up with
			totalUnitCount = DatabaseConnector.getUnitCount();
			
			int offset = 0;
			String allAvailableUnits = DatabaseConnector.getUnitsDistinct();
			distinctUnits = new Unit[allAvailableUnits.length() / (6 + 100)];
			for(int i = 1; i <= allAvailableUnits.length() / (6 + 100); i++) {
				Unit newUnit = new Unit(-1, allAvailableUnits.substring(offset, 6 + offset),
											allAvailableUnits.substring(6 + offset, 106 + offset), -1);
				distinctUnits[i-1] = newUnit;
				offset = i*(6 + 100);
			}
			
			int startOffset = 0;
			String dbUnits = DatabaseConnector.getUnits();
			for(int i = 0; i < totalUnitCount; i++) {
				Unit unit = new Unit();

				int endIndex = nthOccurrence(dbUnits, '^', (i+1)*2) + 1;

				String builderString = dbUnits.substring(startOffset, endIndex);
				
				//Log.v("KEEPUP", String.valueOf(endIndex));
				//Log.v("KEEPUP", String.valueOf(startOffset));
				//Log.v("KEEPUP", builderString);
				
				unit.setupUnit(builderString);
				unitsInWindow.add(unit);
				startOffset = endIndex;
			}
			return null;
		}

		protected void onPostExecute(Integer result) {
			showUnitOptions();
        }
	}
	
	public class DisplayUnits extends AsyncTask<String, Void, Integer> {
		int unitCount = 0;
		ArrayList<Unit> unitsToDisplay = new ArrayList<Unit>();
		
		@Override
		protected Integer doInBackground(String... params) {
			//Set the # of units we're keeping up with
			unitCount = DatabaseConnector.getUnitCountByUser(Integer.parseInt(params[0]));
			
			int startOffset = 0;
			String dbUnits = DatabaseConnector.getUnitsByUser(Integer.parseInt(params[0]));
			for(int i = 0; i < unitCount; i++) {
				Unit unit = new Unit();
				
				int endIndex = nthOccurrence(dbUnits, '^', (i+1)*2) + 1;

				String builderString = dbUnits.substring(startOffset, endIndex);
				
				//Log.v("KEEPUP", String.valueOf(endIndex));
				//Log.v("KEEPUP", String.valueOf(startOffset));
				//Log.v("KEEPUP", builderString);
				
				unit.setupUnit(builderString);
				unitsToDisplay.add(unit);
				startOffset = endIndex;
			}
			return null;
		}

		protected void onPostExecute(Integer result) {
			TextView noOfUnits = (TextView) findViewById(R.id.unit_counter);
			noOfUnits.setText(unitCount + " Unit" + (unitCount > 1 ? "s" : ""));
		
			LayoutInflater inflater = (LayoutInflater) getBaseContext().
					getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	        //ADD UNIT LISTINGS 1 BY 1
			LinearLayout unitList = (LinearLayout) findViewById(R.id.unit_listing_linear);
			
			for(int i = 0; i < unitsToDisplay.size(); i++)  {
				View unitView = inflater.inflate(R.layout.unit_template, null);

				unitView = setupUnitView(unitsToDisplay.get(i), unitView);
			 
				//Add to view.
				unitList.addView(unitView);
			}
        }
		
		private View setupUnitView(Unit unit, View rootView) {
			//Setup Unit Name.
			TextView unitCode = (TextView) rootView.findViewById(R.id.unitcode_code);
			SpannableString content = new SpannableString(unit.getCode() + " - " + unit.getName());
			//content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			unitCode.setText(content);
			
			String announcementTest = "Last announcement text goes here, blah blah...";
			//Setup last announcement.
			TextView announcementLast = (TextView) rootView.findViewById(R.id.announcement_last_unit);
			announcementLast.setText(announcementTest);
			
			int notificationCountTest = 0;	//@EDIT
			//Setup notification counts.
			TextView announcementCount = (TextView) rootView.findViewById(R.id.announcement_value_unit);
			announcementCount.setText("x " + String.valueOf(notificationCountTest));
			TextView postCount = (TextView) rootView.findViewById(R.id.post_value_unit);
			postCount.setText("x " + String.valueOf(notificationCountTest));
			TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.postsOnYours_value_unit);
			postOnYoursCount.setText("x " + String.valueOf(notificationCountTest));
			 
			return rootView;
		}
	}
	public class AddUnitToUser extends AsyncTask<Unit, Void, Void> {
		@Override
		protected Void doInBackground(Unit... params) {
			DatabaseConnector.editUnitUnderUser(params[0].getCode(), params[0].getName(), GlobalVariables.USERLOGGEDIN.getId(), false);
			return null;
		}
		protected void onPostExecute(Void result) {
			recreate();
        }
	}
	public class RemoveUnitFromUser extends AsyncTask<Unit, Void, Void> {
		@Override
		protected Void doInBackground(Unit... params) {
			DatabaseConnector.editUnitUnderUser(params[0].getCode(), params[0].getName(), GlobalVariables.USERLOGGEDIN.getId(), true);
			return null;
		}
		protected void onPostExecute(Void result) {
			recreate();
        }
	}

	//Helper method, understands Strings from MySQL web service results.
	//essentially splits up user ids of variable length.
	public int nthOccurrence(String str, char c, int n) {
	    int pos = str.indexOf(c, 0);
	    n--;
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos + 1);
	    return pos;
	}
}
