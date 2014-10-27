package com.keepup.activities;

import java.util.ArrayList;

import com.keepup.DatabaseConnector;
import com.keepup.GlobalVariables;
import com.keepup.group.Group;
import com.keepup.post.Post;
import com.keepup.unit.Unit;
import com.keepup.user.User;
import com.keepup.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
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
		
		//CLICK SETTINGS BUTTON IN ACTION BAR
		if (id == R.id.action_settings) {
			
		}
		
		//CLICK HOME BUTTON -JACK
		if (id == R.id.action_home) {
			Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
    
    public void loginFail() {
    	Toast.makeText(this, "Id or Password Incorrect.", Toast.LENGTH_SHORT).show();
    }
    public void loginSuccess() {
        Intent intent = new Intent(this, HomeActivity.class);
    	startActivity(intent);
		Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
    }

	private static User lastFetchedUser;
	
	int postCount = 0;
	
	
	int groupCount = 0;
	int unitCount = 0;
    
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
				
				
				
				unitCount = DatabaseConnector.getUnitCountByUser(lastFetchedUser.getId());
				groupCount = DatabaseConnector.getGroupCountByUser(lastFetchedUser.getId());
				
				//Gather posts for units
				int startOffsetUnits = 0;
				String dbUnits = DatabaseConnector.getUnitsByUser(lastFetchedUser.getId());
				for(int i = 0; i < unitCount; i++) {
					Unit unit = new Unit();
					
					int endIndex = nthOccurrence(dbUnits, '^', (i+1)*2) + 1;

					String builderString = dbUnits.substring(startOffsetUnits, endIndex);
					
					unit.setupUnit(builderString);
					GlobalVariables.UNITSWITHPOSTS.add(unit);
					startOffsetUnits = endIndex;
				}
				
				for(int i = 0; i < GlobalVariables.UNITSWITHPOSTS.size(); i ++){
					postCount = DatabaseConnector.getPostCountInUnit(GlobalVariables.UNITSWITHPOSTS.get(i).getId());
					
					int beginOffset = 0;
					String getPostsString = DatabaseConnector.getPostsInUnit(GlobalVariables.UNITSWITHPOSTS.get(i).getId(), 
							lastFetchedUser.getId(), false);
					for(int c = 0; c < postCount; c++) {
						Post post = new Post();
						int endIndex = nthOccurrence(getPostsString, '^', (c+1)*5) + 1 + 512;

						String builderString = getPostsString.substring(beginOffset, endIndex);
						
						post.setupPost(builderString);
						
						GlobalVariables.POSTS.add(post);
						User user = new User();
						String userDetails = DatabaseConnector.getUser(post.getUserId());
						user.setupUser(userDetails);
						GlobalVariables.USERSWHOPOSTED.add(user);
						beginOffset = endIndex;
						}
					}
				
				//Gather posts for groups
				String dbGroups = DatabaseConnector.getGroupsByUser
						(lastFetchedUser.getId());
				groupCount = DatabaseConnector.getGroupCountByUser(lastFetchedUser.getId());
				
				if(dbGroups != null){
					
					int startOffset = 0;
					for(int i = 0; i < groupCount; i++){
						Group group = new Group();
						String builderString;
						int endIndex = nthOccurrence(dbGroups, '^', (i+2)*2) + 1;
						if(i == (groupCount -1) )
							builderString = dbGroups.substring(startOffset, (startOffset + 205));
						else
							builderString = dbGroups.substring(startOffset, endIndex);
						
						int numberCounter = group.setupGroup(builderString);
						
						GlobalVariables.GROUPSWITHPOSTS.add(group);
						
						startOffset = endIndex - numberCounter ;
					}
					for(int i = 0; i < GlobalVariables.GROUPSWITHPOSTS.size();i++){
						postCount = DatabaseConnector.getPostCountInGroup(GlobalVariables.GROUPSWITHPOSTS.get(i).getGroupId());		
						
								
						int startOffsetGroups = 0;
						String getPostsString = DatabaseConnector.getPostsInGroup
								(GlobalVariables.GROUPSWITHPOSTS.get(i).getGroupId(), lastFetchedUser.getId(), false);
						
						for(int j = 0; j < postCount; j++) {
							Post post = new Post();

							int endIndex = nthOccurrence(getPostsString, '^', (j+1)*5) + 1 + 512;
							
							String builderString = getPostsString.substring(startOffsetGroups, endIndex);
							
							post.setupPost(builderString);
							
									
							GlobalVariables.POSTS.add(post);
							User user = new User();
							String userDetails = DatabaseConnector.getUser(post.getUserId());
							user.setupUser(userDetails);
							GlobalVariables.USERSWHOPOSTED.add(user);
							startOffsetGroups = endIndex;
						}
					}
					
				}
				
			}
			return null;
		}
		public int nthOccurrence(String str, char c, int n) {
		    int pos = str.indexOf(c, 0);
		    n--;
		    while (n-- > 0 && pos != -1)
		        pos = str.indexOf(c, pos + 1);
		    return pos;
		}
		@Override
        protected void onPostExecute(Void result) {
			if(lastFetchedUser != null) {
	            GlobalVariables.USERLOGGEDIN = lastFetchedUser;
	            loginSuccess();
	            Log.v("POST#",String.valueOf(GlobalVariables.POSTS.size()));
			} else {
				loginFail();
			}
        }
	}
}
