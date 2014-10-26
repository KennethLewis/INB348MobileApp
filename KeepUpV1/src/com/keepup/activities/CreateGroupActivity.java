package com.keepup.activities;


import java.util.ArrayList;
import java.util.List;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.activities.RegisterActivity.RegisterUser;
import com.keepup.activities.UnitsActivity.AddUnitToUser;
import com.keepup.activities.UnitsActivity.DisplayUnits;
import com.keepup.activities.UnitsActivity.PopupAllUnits;
import com.keepup.activities.UnitsActivity.RemoveUnitFromUser;
import com.keepup.group.Group;
import com.keepup.unit.Unit;
import com.keepup.user.User;
import com.keepup.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CreateGroupActivity extends Activity {

	ListView userList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.create_group_activity, new PlaceholderFragment()).commit();
		}
		LayoutInflater inflater = (LayoutInflater) getBaseContext().
				getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		LinearLayout userList = (LinearLayout) findViewById(R.id.users_to_add);
		for(int i = 0; i < usersForGroup.size(); i++)  {
			View unitView = inflater.inflate(R.layout.member_details_template, null);
			unitView = setupUnitView(usersForGroup.get(i), i, unitView);
			userList.addView(unitView);
		}
	}
	
	private View setupUnitView(User user, int i, View rootView) {
		
		TextView name = (TextView) rootView.findViewById(R.id.members_name);
		name.setText(user.getUsername());
		
		TextView id = (TextView) rootView.findViewById(R.id.members_id);
		id.setText(String.valueOf(user.getId()));
		
		//TextView email = (TextView) rootView.findViewById(R.id.members_email);
		//email.setText(usersForGrp.get(i).getEmail());

		 //Change background colour based on element id.
		 if(i % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
		 
	}
	public void createGroup(View v) {
		
		String groupName;
		String groupDescription;
		
		EditText name = (EditText)findViewById(R.id.newGroupName);
		EditText desc = (EditText)findViewById(R.id.group_description);
		
		groupName = name.getText().toString();
		groupDescription = desc.getText().toString();
		
		RegisterGroup registerGroupThread = new RegisterGroup();
		registerGroupThread.execute("14", groupName, groupDescription);
		
		if(registerUser == true && registerGroup == true){
			Intent intent = new Intent(this, GroupActivity.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
		}
			
	}

	//HANDLE ALL ADD/REMOVE AND USER UNIT ALTERATIONS
	public void clickAddRemoveUsers(View v) {
		PopupAllUsers popupAllUnitsThread = new PopupAllUsers();
		popupAllUnitsThread.execute();
	}
	
	//Method to show the users options and enable them to be clicked.
	protected void showUserOptions() {
		
		boolean[] checkedUser = new boolean[usersToDisplay.size()];
		for(int i = 0; i < usersToDisplay.size(); i++) {
			for(User u : usersForGroup)
		        if(u.getId() == usersToDisplay.get(i).getId())
					checkedUser[i] = true;
		}
		
		DialogInterface.OnMultiChoiceClickListener usersDialogListener = 
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if(isChecked) {
					AddUserToGroup addUserToGroup = new AddUserToGroup();
					addUserToGroup.execute(usersToDisplay.get(which));
					
				} else {
					RemoveUserFromGroup removeUserFromGroup = new RemoveUserFromGroup();
					removeUserFromGroup.execute(usersToDisplay.get(which));
					Log.v("KEEPUP", "REMOVE");
				}
			}
		};
			
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add/remove Users");
		
		CharSequence[] userNames = new CharSequence[usersToDisplay.size()];
		for(int i = 0; i < usersToDisplay.size(); i++)
			userNames[i] = usersToDisplay.get(i).getUsername();
		
		builder.setMultiChoiceItems(userNames, checkedUser, usersDialogListener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/* THREADED ACTIVITIES */
	
	int totalUserCount = 0; 
	String[] unitCodes;
	ArrayList<User> usersToDisplay = new ArrayList<User>();
	List<User> usersForGroup = GlobalVariables.USERSFORGROUP;
	public class PopupAllUsers extends AsyncTask<String, Void, Integer> {
		
			
		@Override
		protected Integer doInBackground(String... params) {
			
			totalUserCount = DatabaseConnector.getUserCount();
			String builderString = DatabaseConnector.getAllUsers();
			int offset = 0;
			for(int i = 0; i < totalUserCount; i++) {
				
				User user = new User();
				/*
				 * Had to remodel this method from users. For some
				 * reason the below commented out code would keep 
				 * throwing errors about string placement or some such.
				 * The copied code from users works fine and displays
				 * users. If you want to try the other method be my 
				 * guest.
				 * 
				 * So you just told me its cause I have to change the 
				 * numbers in the nthOcc method.. Me no comprenday, so
				 * maybe you can fix it if you wish lol
				 */
				/*User user = new User();
				
				int endIndex = nthOccurrence(dbUsers, '^', (i+1)*2) + 1;
				String builderString = dbUsers.substring(startOffset, endIndex);
				//Log.v("BUILDERSTRING",builderString);
				user.setupUser(builderString);
				usersToDisplay.add(user);
				startOffset = endIndex;*/
				
				String[] segmentedStrings = new String[5];
				
				segmentedStrings[0] = builderString.substring(offset, builderString.indexOf("^", offset));
				offset += segmentedStrings[0].length() + 1;
				segmentedStrings[1] = builderString.substring(offset, offset + 15);
				offset += segmentedStrings[1].length();
				segmentedStrings[2] = builderString.substring(offset, offset + 50);
				offset += segmentedStrings[2].length();
				segmentedStrings[3] = builderString.substring(offset, builderString.indexOf("^", offset));
				offset += segmentedStrings[3].length() + 1;
				segmentedStrings[4] = builderString.substring(offset, offset + 25);
				offset += segmentedStrings[4].length();
				
				user.setId(Integer.parseInt(segmentedStrings[0].replace(" ", "")));
				user.setUsername(segmentedStrings[1].replace(" ", ""));
				user.setEmail(segmentedStrings[2].replace(" ", ""));
				usersToDisplay.add(user);
			}
			return null;
		}

		protected void onPostExecute(Integer result) {
			
			showUserOptions();
        }
	}
	private ArrayList<Integer> usersIdToAdd = new ArrayList<Integer>();
	public class AddUserToGroup extends AsyncTask<User, Void, Void> {
		@Override
		protected Void doInBackground(User... params) {
			Log.v("IN ADDUSERTOGROUP", params[0].getUsername());
			usersIdToAdd.add(params[0].getId());
			GlobalVariables.USERSFORGROUP.add(params[0]);
			return null;
		}
		protected void onPostExecute(Void result) {
			recreate();
        }
	}
	public class RemoveUserFromGroup extends AsyncTask<User, Void, Void> {
		@Override
		protected Void doInBackground(User... params) {
			
			for(Integer i: usersIdToAdd){
				if(params[0].getId() == i)
					usersIdToAdd.remove(i);
			}
			for(User u: GlobalVariables.USERSFORGROUP)
				if(u.getId() == params[0].getId())
					GlobalVariables.USERSFORGROUP.remove(u);
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
	
	private boolean registerUser;
	private boolean registerGroup;
	public class RegisterUserForGroup extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			Log.v("REGISTER USER params 0", params[0]);
			Log.v("REGISTER USER params 0", params[1]);
			if(DatabaseConnector.addUserToGroup(Integer.valueOf(params[0]),Integer.valueOf(params[1]))) {
				Log.v("KEEPUP", "Register User SUCCESS");
				registerUser = true;
				return true;
			}
			Log.v("KEEPUP", "Register User FAIL");
			registerUser = false;
			return false;
		}
		
		
	}
	private int groupId;
	public class RegisterGroup extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			groupId = DatabaseConnector.createGroup(Integer.valueOf(params[0]),params[1], params[2]);
			if(groupId != 0){
				Log.v("KEEPUP", "Register Group SUCCESS");
				Log.v("GROUPID", String.valueOf(groupId));
				registerGroup = true;
				for(User u: usersForGroup){
					RegisterUserForGroup registerThread = new RegisterUserForGroup();
					registerThread.execute(String.valueOf(groupId), String.valueOf(u.getId()));
				}
				return true;
			}
			Log.v("KEEPUP", "Register Group FAIL");
			registerGroup = false;
			return false;
		}
		protected void onPostExecute(boolean result) {
			
        }
	}
	public void Success(){
			
		
	}
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

	}
	
}
