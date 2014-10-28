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

		LayoutInflater inflater = (LayoutInflater) getBaseContext().
				getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		LinearLayout userList = (LinearLayout) findViewById(R.id.users_to_add);
		
		for(int i = 0; i < usersForGroup.size(); i++)  {
			//Log.v("KEEPUP", "Test display selected Members");
			View unitView = inflater.inflate(R.layout.member_details_template, null);
			unitView = setupGroupMemberView(usersForGroup.get(i), i, unitView);
			userList.addView(unitView);
		}
	}
	
	private View setupGroupMemberView(User user, int i, View rootView) {
		TextView name = (TextView) rootView.findViewById(R.id.members_name);
		name.setText(user.getUsername());
		
		TextView id = (TextView) rootView.findViewById(R.id.members_id);
		id.setText(String.valueOf(user.getId()));
		
		TextView email = (TextView) rootView.findViewById(R.id.members_email);
		email.setText(String.valueOf(user.getEmail()));

		 //Change background colour based on element id.
		rootView.setBackgroundColor(getResources().getColor(i % 2 == 0 ? 
				R.color.unit_grey_even : R.color.unit_grey_odd));
		 
		return rootView;
	}
	
	public void createGroup(View v) {
		String groupName = ((EditText) findViewById(R.id.newGroupName))
				.getText().toString();
		String groupDescription = ((EditText) findViewById(R.id.group_description))
				.getText().toString();
		
		RegisterGroup registerGroupThread = new RegisterGroup();
		registerGroupThread.execute("14", groupName, groupDescription);
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
					usersForGroup.add(usersToDisplay.get(which));
				} else {
					if(usersForGroup.contains(usersToDisplay.get(which)))
						usersForGroup.remove(usersToDisplay.get(which));
				}
				recreate();
			}
		};
			
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Members");
		
		CharSequence[] userNames = new CharSequence[usersToDisplay.size()];
		for(int i = 0; i < usersToDisplay.size(); i++)
			userNames[i] = usersToDisplay.get(i).getUsername();
		
		builder.setMultiChoiceItems(userNames, checkedUser, usersDialogListener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/* THREADED ACTIVITIES */
	
	ArrayList<User> usersToDisplay = new ArrayList<User>();
	List<User> usersForGroup = GlobalVariables.USERSFORGROUP;
	public class PopupAllUsers extends AsyncTask<String, Void, Integer> {
		int totalUserCount = 0;
		
		@Override
		protected Integer doInBackground(String... params) {
			totalUserCount = DatabaseConnector.getUserCount();
			String dbUsers = DatabaseConnector.getAllUsers();
			int startOffset = 0;
			for(int i = 0; i < totalUserCount; i++) {
				
				User user = new User();
				
				int endIndex = nthOccurrence(dbUsers, '^', (i+1)*2) + 1 + 25;
				String builderString = dbUsers.substring(startOffset, endIndex);

				user.setupUser(builderString);
				
				if(user.getId() != GlobalVariables.USERLOGGEDIN.getId())
					usersToDisplay.add(user);
				
				startOffset = endIndex;
			}
			return null;
		}

		protected void onPostExecute(Integer result) {
			showUserOptions();
        }
	}
	
	public class RegisterGroup extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			int groupId = DatabaseConnector.createGroup(Integer.valueOf(params[0]), params[1], params[2]);
			//Log.v("KEEPUP", String.valueOf(groupId));
			//Add self to group, then add all the users we selected.
			if(!DatabaseConnector.addUserToGroup(groupId, GlobalVariables.USERLOGGEDIN.getId()))
				return false;
			if(groupId != 0)
				for(User u: usersForGroup) 
					if(!DatabaseConnector.addUserToGroup(groupId, u.getId()))
						return false;
			return true;
		}
		protected void onPostExecute(Boolean result) {
			if(result)
				createGroupSuccess();
        }
	}
	public void createGroupSuccess() {
		Intent intent = new Intent(this, GroupActivity.class);
    	startActivity(intent);
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
