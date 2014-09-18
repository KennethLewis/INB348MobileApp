package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import group.Group;
import group.GroupDatabaseController;

import com.example.keepupv1.unit.Unit;
import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class GroupActivity extends Activity {

	//UserDatabaseController db;
	GroupDatabaseController groupDb;
	List<Group> allGroups = new ArrayList<Group>();
	private int[][] intTests = {{1,2,3}, {4,5,6}, {5,4,3}, {2,1,0}};
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.groups_top_container, new PlaceholderFragment()).commit();
		}
		
		groupDb = new GroupDatabaseController(this);
		List<Integer> studentNos;
		
		for(Group groups: groupDb.getAllGroups()){
			studentNos = groups.gatherUsers();
			if(studentNos.contains(DatabaseVariables.USERLOGGEDIN.getId()))
				allGroups.add(groups);
		}
		
		LinearLayout groupList = (LinearLayout) findViewById(R.id.groups_list);
		
		for(int i = 0; i < allGroups.size(); i++)  {
			View rootView = getLayoutInflater().inflate(R.layout.group_template, null);
			 
			User user = DatabaseVariables.USERLOGGEDIN;
			if(user != null) {
				rootView = setUpGroupView(i, rootView);
			 
				//Add to view.
				groupList.addView(rootView);
			}
		}
		
	}
	
	private View setUpGroupView(int i, View rootView) {
		
		//Setup Unit Name.
		TextView groupName = (TextView) rootView.findViewById(R.id.group_name);
		groupName.setText(allGroups.get(i).getName());
		
		TextView groupMembers = (TextView) rootView.findViewById(R.id.group_members);
		groupMembers.setText(allGroups.get(i).getGroupMembers());
		
		//Setup last announcement.
		TextView groupPost = (TextView) rootView.findViewById(R.id.last_group_post);
		groupPost.setText("Test Post");
		
		//Setup notification counts.
		/*TextView announcementCount = (TextView) rootView.findViewById(R.id.announcement_value_unit);
		announcementCount.setText("x " + String.valueOf(intTests[i][0]));
		TextView postCount = (TextView) rootView.findViewById(R.id.post_value_unit);
		postCount.setText("x " + String.valueOf(intTests[i][1]));
		TextView postOnYoursCount = (TextView) rootView.findViewById(R.id.postsOnYours_value_unit);
		postOnYoursCount.setText("x " + String.valueOf(intTests[i][2]));
	*/
		 //Change background colour based on element id.
		 if(i % 2 == 0)
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_even));
		 else
			 rootView.setBackgroundColor(getResources().getColor(R.color.unit_grey_odd));
		 
		return rootView;
	}

	public void createGroup (View v){
		Intent intent = new Intent(this, CreateGroupActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
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
			groupDb.emptyDatabase();
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
			View rootView = inflater.inflate(R.layout.fragment_group,
					container, false);
			return rootView;
		}
	}
}
