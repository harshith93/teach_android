/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    CheckBox checkBox;

    public void showUserList(){

        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);
    }

    public void loginOrSignup(View view) {

        ParseUser.logInInBackground(String.valueOf(usernameEditText.getText()), String.valueOf(passwordEditText.getText()), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

                    Log.i("AppInfo", "Logged in");

                    showUserList();
                } else {

                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(String.valueOf(usernameEditText.getText()));
                    newUser.setPassword(String.valueOf(passwordEditText.getText()));
                    List<String> emptyList = new ArrayList<String>();
                    newUser.put("isFollowing", emptyList);


                    checkBox = (CheckBox)findViewById(R.id.checkBox);

                    if (checkBox.isChecked()){

                        newUser.put("isProfessor", true);

                    }else{

                        newUser.put("isProfessor", false);
                    }



                    newUser.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {

                                Log.i("AppInfo", "Signed Up");
                                showUserList();

                            } else {

                                Toast.makeText(getApplication(), "Could not log in or sign up - please try again!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }
            }
        });

    }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        usernameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);

        ParseUser currentUser = ParseUser.getCurrentUser();


        if(currentUser != null){


            showUserList();

        }


  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}

/*
        ParseObject score = new ParseObject("Score");
        score.put("username", "bhargav");
        score.put("score", 190);
        score.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Parse", "Save Succeeded");
                } else {
                    Log.i("Parse", "Save Failed"+ e.getMessage());
                    e.printStackTrace();
                }
            }
        });



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

        query.getInBackground("vaKeUHP7YI", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                object.put("score", 199);
                object.saveInBackground();
            }
        });


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");



        query.whereEqualTo("username", "harshith");
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){

                        objects.get(0).put("score", 150);
                        objects.get(0).saveInBackground();
                    }
                }
            }
        });



        ParseUser user = new ParseUser();
        user.setUsername("harshith");
        user.setPassword("password");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.i("status", "successful");
                }else{
                    Log.i("status", "failed");
                    e.printStackTrace();
                }
            }
        });


        ParseUser.logInInBackground("harshith", "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null){
                    Log.i("Login", "Successful");
                }
            }
        });


        if(ParseUser.getCurrentUser() != null){

            Log.i("Current user", "logged in");

        }else{
            Log.i("Current user", "not logged in");
        }

        ParseUser.logOut();
        */