package com.example.keepupv1;

import group.Group;
import group.GroupDatabaseController;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.unit.Unit;
import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class CreateGroupActivity extends Activity {

	private List<User> selectedUsers = new ArrayList<User>();
	List<User> usersForGrp = new ArrayList<User>();
	private UserDatabaseController db;
	private GroupDatabaseController groupDb;
	ListView userList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.create_group_activity, new PlaceholderFragment()).commit();
		}
		db = new UserDatabaseController(this);
		groupDb = new GroupDatabaseController(this);
		
		for (User user: DatabaseVariables.USERLOGGEDIN.getUsersForGroup()){
			usersForGrp.add(user);
		}
		
		LinearLayout userList = (LinearLayout) findViewById(R.id.group_members_list);
		
			for(int i = 0; i < usersForGrp.size(); i++)  {
				View rootView = getLayoutInflater().inflate(R.layout.member_details_template, null);
				 
				User user = usersForGrp.get(i);
				if(user != null) {
					rootView = setupUnitView(i, rootView);
				 
					//Add to view.
					userList.addView(rootView);
				}
			}
		}
	

	private View setupUnitView(int i, View rootView) {
		
		//Setup Unit Name.
		TextView name = (TextView) rootView.findViewById(R.id.members_name);
		name.setText(usersForGrp.get(i).getUsername());
		
		TextView id = (TextView) rootView.findViewById(R.id.members_id);
		id.setText(String.valueOf(usersForGrp.get(i).getId()));
		
		TextView email = (TextView) rootView.findViewById(R.id.members_email);
		email.setText(usersForGrp.get(i).getEmail());

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
		getMenuInflater().inflate(R.menu.create_group, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_group,
					container, false);
			return rootView;
		}
	}
	
public void showUsers(View v){
		
		switch (v.getId()){
		
		case R.id.add:
			showUsersOptions();
			break;
			
		default:
		break;
		}
	}
	
	//Method to show the unit options and enable them to be clicked.
	protected void showUsersOptions(){
		
		boolean[] checkedUsers = new boolean[db.getAllUsers().size()];
		int count = db.getAllUsers().size();
		
		for(int i = 0; i < count; i++)
			checkedUsers[i] = selectedUsers.contains(db.getAllUsers().get(i));
		
		DialogInterface.OnMultiChoiceClickListener 
			unitsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked)
						selectedUsers.add(db.getAllUsers().get(which));
					else
						selectedUsers.remove(db.getAllUsers().get(which));
					
					onChangeSelectedUsers();
				}
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Users");
			
			CharSequence [] userNames = new CharSequence[db.getAllUsers().size()];
			for(int i =0; i < db.getAllUsers().size(); i++)
				userNames[i] = db.getAllUsers().get(i).getUsername();
			
			builder.setMultiChoiceItems(userNames, checkedUsers, unitsDialogListener);
			
			
			AlertDialog dialog = builder.create();
			dialog.show();
					
	}
	
	//Will refresh the users page once unit is selected
	public void onChangeSelectedUsers(){
		
		for(User user : selectedUsers){
			if(DatabaseVariables.USERLOGGEDIN.getUsersForGroup().contains(user) == false)
				DatabaseVariables.USERLOGGEDIN.addUserForGroup(user);
		}
		this.recreate();
	}
	
	public void createGroup(View v){
		
		String groupName;
		String members ="";
		String membersId = "";
		String description;
		
		for(User users: DatabaseVariables.USERLOGGEDIN.getUsersForGroup()){
			members += users.getUsername() + ",";
			membersId += users.getId() + ",";
		}
		
		EditText name = (EditText)findViewById(R.id.newGroupName);
		EditText desc = (EditText)findViewById(R.id.group_description);
		
		groupName = name.getText().toString();
		description = desc.getText().toString();
		
		Group newGroup = new Group (groupName,members,membersId,description);
		groupDb.addGroup(newGroup);
		
		DatabaseVariables.USERLOGGEDIN.clearUsersForGroup();
		Intent intent = new Intent(this, GroupActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}
}
