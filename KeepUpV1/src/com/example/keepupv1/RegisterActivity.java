package com.example.keepupv1;

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
import android.widget.EditText;
import android.os.Build;

public class RegisterActivity extends Activity {

	private UserDatabaseController db;
	private User newUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		db = new UserDatabaseController(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void registerUser(View v){
		
		EditText userUniId = (EditText)findViewById(R.id.new_student_no);
		EditText userName = (EditText)findViewById(R.id.new_name);
		EditText userEmail = (EditText)findViewById(R.id.new_email_addy);
		EditText userPw = (EditText)findViewById(R.id.new_user_pw);
		
		int id = Integer.parseInt(userUniId.getText().toString());
		String name = userName.getText().toString();
		String email = userEmail.getText().toString();
		String pw = userPw.getText().toString();
		
		newUser = new User (id,name,email,0,"");
		db.addUser(newUser);
		
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
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
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			return rootView;
		}
	}
}
