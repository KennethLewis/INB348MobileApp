package com.example.keepupv1;

import group.GroupDatabaseController;

import java.util.ArrayList;
import java.util.List;

import post.Post;

import com.example.keepupv1.unit.Unit;
import com.example.keepupv1.unit.UnitDatabaseController;
import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UnitsActivity extends Activity implements OnClickListener {
	
	//Global database connection
	UserDatabaseController userDb;
	UnitDatabaseController unitDb;
	GroupDatabaseController groupDb;
	
	private String [] stringTests = {"Test announcement Lorem ipsum1"};
	private List<String> userSubsCode = new ArrayList<String>();
	private List<String> userSubsNames = new ArrayList<String>();
	private int[][] intTests = {{1,2,3}, {4,5,6}, {5,4,3}, {2,1,0}};
	
	private List<Unit> unitsFromDb;
	private List<Unit> selectedUnits = new ArrayList<Unit>();
	
	
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
		userDb = new UserDatabaseController(this);
		groupDb = new GroupDatabaseController(this);
		unitDb = new UnitDatabaseController(this);
		
		unitsFromDb = unitDb.getAllGroups();
		 
        //ADD UNIT LISTINGS 1 BY 1
		LinearLayout unitList = (LinearLayout) findViewById(R.id.units_list);
		
		
		
		for (Unit units: DatabaseVariables.USERLOGGEDIN.getAllSubjects()){
			userSubsCode.add(units.getCode());
			userSubsNames.add(units.getName());
		}
		for(int i = 0; i < userSubsNames.size(); i++)  {
			View rootView = getLayoutInflater().inflate(R.layout.unit_template, null);
			 
			User user = DatabaseVariables.USERLOGGEDIN;
			if(user != null) {
				rootView = setupUnitView(i, rootView);
			 
				//Add to view.
				unitList.addView(rootView);
			}
		}
		
	}
	
	private View setupUnitView(int i, View rootView) {
		
		//Setup Unit Name.
		TextView unitCode = (TextView) rootView.findViewById(R.id.unitcode_code);
		unitCode.setText(userSubsCode.get(i));
		
		TextView unitName = (TextView) rootView.findViewById(R.id.unitname_unit);
		unitName.setText(userSubsNames.get(i));
		
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
		 if(i % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
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
		
		boolean[] checkedUnits = new boolean[unitsFromDb.size()];
		int count = unitsFromDb.size();
		
		for(int i = 0; i < count; i++)
			checkedUnits[i] = selectedUnits.contains(unitsFromDb.get(i));
		
		DialogInterface.OnMultiChoiceClickListener 
			unitsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked)
						selectedUnits.add(unitsFromDb.get(which));
					else
						selectedUnits.remove(unitsFromDb.get(which));
					
					onChangeSelectedUnits();
				}
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Units");
			
			CharSequence [] unitNames = new CharSequence[unitsFromDb.size()];
			for(int i =0; i < unitsFromDb.size(); i++)
				unitNames[i] = unitsFromDb.get(i).getName();
			
			builder.setMultiChoiceItems(unitNames, checkedUnits, unitsDialogListener);
			
			
			AlertDialog dialog = builder.create();
			dialog.show();
					
	}
	
	//Will refresh the users page once unit is selected
	public void onChangeSelectedUnits(){
		//ADD UNITS TO PAGE, MAYBE REFRESH?
		for(Unit unit: selectedUnits){
			if(DatabaseVariables.USERLOGGEDIN.getAllSubjects().contains(unit) == false){
				DatabaseVariables.USERLOGGEDIN.addSubject(unit);
				//Add this user to the Unit
			}
		}
		this.recreate();
	}
	
	
	public void unitDetails(View v){
		
		TextView unitId = (TextView) v.findViewById(R.id.unitcode_code);
		String id = (String) unitId.getText();
		DatabaseVariables.USERLOGGEDIN.setUnit(id);
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
			//db.emptyDatabase();//CLEARS USERS DATABASE!
			groupDb.emptyDatabase();
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

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
}
