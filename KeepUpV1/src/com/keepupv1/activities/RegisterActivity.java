package com.keepupv1.activities;

import com.keepupv1.R;
import com.keepupv1.R.id;
import com.keepupv1.R.layout;
import com.keepupv1.R.menu;
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

public class RegisterActivity extends Activity {

	private UserDatabaseController usersDb;
	private User newUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		usersDb = new UserDatabaseController(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void registerUser(View v){
		
		//DONT NEED TO ASK USER FOR ALL INFORMATION, A LOT CAN COME OFF DATABASES
		//AFTER THE USER LINKS THEIR ACC WITH A QUT EMAIL.
		try {
			EditText userUniId = (EditText)findViewById(R.id.new_student_no);
			//EditText userName = (EditText)findViewById(R.id.new_name);
			EditText userEmail = (EditText)findViewById(R.id.new_email_addy);
			EditText userPw = (EditText)findViewById(R.id.password);
			EditText userPwConfirm = (EditText)findViewById(R.id.confirm_password);
			
			int id = Integer.parseInt(userUniId.getText().toString());
			String [] usersName = userEmail.getText().toString().split("@");
			String name = usersName[0];
			String email = userEmail.getText().toString();
			String pw = userPw.getText().toString();
			String pwConfirm = userPwConfirm.getText().toString();
			
			newUser = new User(id, name, email, 1, pw);
			
			for (User user : usersDb.getAllUsers()){
				if(user.getId() == id){
					Toast.makeText(this, "Error: User already Exists.", Toast.LENGTH_SHORT)
					.show();
					throw new Exception("User Already Exists");
				}
			}
			if (newUser.getId() < 999999){
				Toast.makeText(this, "Error: Invalid Student Number.", Toast.LENGTH_SHORT)
				.show();
				throw new Exception("Invaild Student Number");
			}
				
			else if(pw.matches(pwConfirm) != true){
				Toast.makeText(this, "Error: Passwords dont match.", Toast.LENGTH_SHORT)
				.show();
				throw new Exception("Passwords dont match");
			}
			else if(pw.length() < 6){
				Toast.makeText(this, "Error: Password must be at least 6 Chars long."
						, Toast.LENGTH_LONG).show();
				throw new Exception("Password length less than 6 characters");
			}
			else if (email.contains("qut.edu.au") == false){
				Toast.makeText(this, "Error: Email address must be a valid QUT email."
						, Toast.LENGTH_LONG).show();
				throw new Exception("Not a valid QUT email address");
			}
			else {
				usersDb.addUser(newUser);
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
		}
		catch (Exception e){
			//Delete this toast after testing.
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
			.show();
			this.recreate();
		}
		
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
