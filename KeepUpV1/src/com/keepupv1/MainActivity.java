package com.keepupv1;

import com.keepupv1.R;
import com.keepupv1.unit.Unit;
import com.keepupv1.unit.UnitDatabaseController;
import com.keepupv1.user.User;
import com.keepupv1.user.UserDatabaseController;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private UserDatabaseController userDb;
	private UnitDatabaseController unitDb;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.units_toplevel_container, new PlaceholderFragment())
                    .commit();
        }
        userDb = new UserDatabaseController(this);
        unitDb = new UnitDatabaseController(this);
        addAllTestData();
    }

    public void goToHome(View v) {
    	EditText usernameField = (EditText)findViewById(R.id.username);
    	//checkUsers = db.getAllUsers();
    	//String userId = usernameField.getText().toString();
    	
    	int userId = -1;
    	try {
    	    userId = Integer.parseInt(usernameField.getText().toString());
    	} catch(NumberFormatException nfe) {
    	   System.out.println("Could not parse " + nfe);
    	} 
    	
    	//for(User allUsers: checkUsers){
    		if(userDb.getUser(userId) != null) {
    			GlobalVariables.USERLOGGEDIN = userDb.getUser(userId);
    			Intent intent = new Intent(this, HomeActivity.class);
        		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	    startActivity(intent);
    		}
	}
    
    public void goToRegister(View v) {
    	Intent intent = new Intent(this, RegisterActivity.class);
    	startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
			userDb.emptyDatabase();
			unitDb.emptyDatabase();
			return true;
		}
		
		//CLICK HOME BUTTON -JACK
		if (id == R.id.action_example) {
			Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show();
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    public void addAllTestData(){
         
        User testUser = new User(5279615, "Ken","kenneth@live.com.au", 0, "");
        User testUser2 = new User(8600571, "Jackson","Jackson@live.com.au", 0, "");
        User dummyUser1 = new User(123456, "Dummy1", "Dummy1@live.com.au", 0, "");
        User dummyUser2 = new User (987654, "Dummy2", "Dummy2@live.com.au", 0, "");
        if(userDb.getAllUsers().isEmpty()){
	        userDb.addUser(testUser);
	        userDb.addUser(testUser2);
	        userDb.addUser(dummyUser1);
	        userDb.addUser(dummyUser2);
        } 
        Unit unit1 = new Unit ("INB100", "Introduction to IT", "","");
     	Unit unit2 = new Unit ("INB123", "Programming 101", "","");
     	Unit unit3 = new Unit ("INB348", "Mobile App Dev", "","");
     	Unit unit4 = new Unit ("INB270", "Advanced Programming", "","");
     	if (unitDb.getAllUnits().isEmpty()){
	     	unitDb.addUnit(unit1);
	     	unitDb.addUnit(unit2);
	     	unitDb.addUnit(unit3);
	     	unitDb.addUnit(unit4);
     	}
    }
}
