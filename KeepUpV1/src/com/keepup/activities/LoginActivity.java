package com.keepup.activities;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.user.User;
import com.keepup.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	public static boolean serverIssue = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
    }

    public void goToHome(View v) {
    	EditText usernameField = (EditText)findViewById(R.id.username);
    	EditText userPassword = (EditText)findViewById(R.id.password);
    	
    	int userId = -1;
    	String userPw = null;
    	try {
    	    userId = Integer.parseInt(usernameField.getText().toString());
    	    userPw = userPassword.getText().toString();
    	} catch(NumberFormatException nfe) {
    		System.out.println("Could not parse " + nfe);
    	} 

    	if(userId == -1 || userPw == null)
    		return;
    	
    	Login loginThread = new Login();
    	loginThread.setId(userId);
    	loginThread.setPassword(userPw);
    	loginThread.execute();
	}
    
    public void goToRegister(View v) {
    	Intent intent = new Intent(this, RegisterActivity.class);
    	startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//CLICK HOME BUTTON -JACK
		if (id == R.id.action_home) {
			Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
    
    public void loginFail() {
    	if(serverIssue)
    		Toast.makeText(this, "Issue connecting to Server.", Toast.LENGTH_SHORT).show();
    	else
    		Toast.makeText(this, "Id or Password Incorrect.", Toast.LENGTH_SHORT).show();
    }
    public void loginSuccess() {
        Intent intent = new Intent(this, HomeActivity.class);
    	startActivity(intent);
		Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
    }
    
    User lastFetchedUser = null;
    public class Login extends AsyncTask<String, Void, Void> {
		protected int id;
		protected String password;
		
		public void setId(int id) {
			this.id = id;
		}
		
		public void setPassword(String pass) {
			this.password = pass;
		}
		
		@Override
		protected Void doInBackground(String... params) {
			lastFetchedUser = null;
			 
			if(DatabaseConnector.getLoggedIn(id, password)) {
				lastFetchedUser = new User();
				lastFetchedUser.setupUser(DatabaseConnector.getUser(id));
			}
			return null;
		}
		@Override
        protected void onPostExecute(Void result) {
			if(lastFetchedUser != null) {
	            GlobalVariables.USERLOGGEDIN = lastFetchedUser;
	            loginSuccess();
	            //Log.v("POST#",String.valueOf(GlobalVariables.POSTS.size()));
			} else {
				loginFail();
			}
        }
	}
	
}
