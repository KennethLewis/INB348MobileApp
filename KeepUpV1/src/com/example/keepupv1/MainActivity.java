package com.example.keepupv1;

import java.util.ArrayList;
import java.util.List;

import com.example.keepupv1.user.User;
import com.example.keepupv1.user.UserDatabaseController;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;



public class MainActivity extends Activity {

	private UserDatabaseController db;
	private List<User> checkUsers;
	private User testUser;
	private User testUser2;
	private User dummyUser1;
	private User dummyUser2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.units_toplevel_container, new PlaceholderFragment())
                    .commit();
        }
        
       db = new UserDatabaseController(this);
       testUser = new User(5279615, "Ken","kenneth@live.com.au", 0, "");
       testUser2 = new User(999999, "Jackson","Jackson@live.com.au", 0, "");
       dummyUser1 = new User(123456, "Dummy1", "Dummy1@live.com.au", 0, "");
       dummyUser2 = new User (987654, "Dummy2", "Dummy2@live.com.au", 0, "");
       db.addUser(testUser);
       db.addUser(testUser2);
       db.addUser(dummyUser1);
       db.addUser(dummyUser2);
    }

    public void goToHome(View v){
    	
    	
    	EditText userName = (EditText)findViewById(R.id.user_name);
    	//checkUsers = db.getAllUsers();
    	int userId = Integer.parseInt(userName.getText().toString());
    	
    	//for(User allUsers: checkUsers){
    		if(db.getUser(userId) != null){
    			DatabaseVariables.USERLOGGEDIN = db.getUser(userId);
    			Intent intent = new Intent(this, HomeActivity.class);
        		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	    startActivity(intent);
    		}
    	//}
    		//else {
			//Intent intent = new Intent(this, UnitsActivity.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       // startActivity(intent);
    		//}
	}
    
    public void goToRegister(View v){
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
			db.emptyDatabase();
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
}
