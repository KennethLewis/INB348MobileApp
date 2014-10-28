package com.keepup.activities;

import com.keepup.user.User;
import com.keepup.DatabaseConnector;
import com.keepup.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void startUserRegistration(View v) {
		EditText userUniId = (EditText)findViewById(R.id.new_student_no);
		int id = Integer.parseInt(userUniId.getText().toString());
		
		FindUser findUserThread = new FindUser();
		findUserThread.execute(id);
	}

	public void registerUser() {
		
		//DONT NEED TO ASK USER FOR ALL INFORMATION, A LOT CAN COME OFF DATABASES
		//AFTER THE USER LINKS THEIR ACC WITH A QUT EMAIL.
		try {
			EditText userUniId = (EditText)findViewById(R.id.new_student_no);
			EditText userEmail = (EditText)findViewById(R.id.new_email_addy);
			EditText userPw = (EditText)findViewById(R.id.password);
			EditText userPwConfirm = (EditText)findViewById(R.id.confirm_password);
			
			//Check if all fields are filled in first.
			if(userUniId.getText().toString().isEmpty() || userEmail.getText().toString().isEmpty()
			   || userPw.getText().toString().isEmpty() || userPwConfirm.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			int id = Integer.parseInt(userUniId.getText().toString());
			String username = userEmail.getText().toString().
					substring(0, userEmail.getText().toString().indexOf("@") == -1 ? 0
							   : userEmail.getText().toString().indexOf("@"));
			String email = userEmail.getText().toString();
			String password = userPw.getText().toString();
			String pwConfirm = userPwConfirm.getText().toString();

			if(lastFetchedUser != null) {
				Toast.makeText(this, "Sorry, that user already exists.", Toast.LENGTH_SHORT)
				.show();
			}
			else if (id > 99999999 || id < 0) {
				Toast.makeText(this, "Invalid student number.", Toast.LENGTH_SHORT)
				.show();
			}
			else if(username.length() < 1 && email.contains("@") || username.length() > 15) {
				Toast.makeText(this, "Username must be at least 1-15 Chars long."
						, Toast.LENGTH_LONG).show();
			}
			else if(password.matches(pwConfirm) != true) {
				Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT)
				.show();
			}
			else if(password.length() < 6 || password.length() > 25) {
				Toast.makeText(this, "Password must be at least 6-25 Chars long."
						, Toast.LENGTH_LONG).show();
			}
			else if(email.contains("qut.edu.au") == false || email.length() > 50) {
				Toast.makeText(this, "Email address must be a valid @qut.edu.au email. (and less than 50chars)"
						, Toast.LENGTH_LONG).show();
			}
			else {
				//SUCCESS let's create the user.
				RegisterUser registerThread = new RegisterUser();
				registerThread.execute(String.valueOf(id), username, email, "0", password);
			}
		}
		catch (Exception e){
			//Delete this toast after testing.
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
			.show();
			this.recreate();
		}
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() { }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			return rootView;
		}
	}
	
	private void registerSuccess() {
		//Log.v("KEEPUP", "TEST SUCCESS REGISTERED");
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	/* ---------------- THREADED TASKS ----------------- */
	User lastFetchedUser;
	public class FindUser extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			String buildString = DatabaseConnector.getUser(params[0]);
			lastFetchedUser = null;
			if(buildString != null) {
				lastFetchedUser = new User();
				lastFetchedUser.setupUser(buildString);
			}
			return null;
		}
		
		protected void onPostExecute(Void result) {
			registerUser();
        }
	}
	
	public class RegisterUser extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			if(DatabaseConnector.registerUser(Integer.valueOf(params[0]), params[1], params[2], Integer.valueOf(params[3]), params[4])) {
				//Log.v("KEEPUP", "SUCCESS");
				return true;
			}
			//Log.v("KEEPUP", "FAIL");
			return false;
		}
		
		protected void onPostExecute(Boolean result) {
			if(result)
				registerSuccess();
        }
	}
}
