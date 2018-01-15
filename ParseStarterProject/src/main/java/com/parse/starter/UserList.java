package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class UserList extends AppCompatActivity {


    ArrayList<String> users;
    ArrayAdapter arrayAdapter;
    ListView listView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Toast.makeText(getApplication(), editText.getText().toString(), Toast.LENGTH_LONG).show();


        if (ParseUser.getCurrentUser().get("isFollowing") == null){
            List<String> emptyList = new ArrayList<String>();
            ParseUser.getCurrentUser().put("isFollowing", emptyList);
        }

        Log.i("AppInfo", String.valueOf(ParseUser.getCurrentUser().getList("isFollowing")));



        users = new ArrayList<String>();


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);

        listView = (ListView) findViewById(R.id.listView);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;
                /*if (checkedTextView.isChecked()) {
                    Log.i("AppInfo", "Checked");
                    ParseUser.getCurrentUser().getList("isFollowing").add(users.get(position));
                    ParseUser.getCurrentUser().saveAllInBackground();


                } else {
                    Log.i("AppInfo", "Row is not Checked");
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(position));
                    ParseUser.getCurrentUser().saveInBackground();
                }*/
                if (checkedTextView.isChecked()) {

                    (ParseUser.getCurrentUser().getList("isFollowing")).add(users.get(position));
                    List newlist = (ParseUser.getCurrentUser().getList("isFollowing"));
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", newlist);
                    ParseUser.getCurrentUser().saveInBackground();

                } else {
                    (ParseUser.getCurrentUser().getList("isFollowing")).remove(users.get(position));
                    List newlist = (ParseUser.getCurrentUser().getList("isFollowing"));
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", newlist);
                    ParseUser.getCurrentUser().saveInBackground();

                }

            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        if (ParseUser.getCurrentUser().get("isProfessor") != true) {
            query.whereEqualTo("isProfessor", true);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        users.clear();

                        for (ParseUser user : objects) {

                            users.add(user.getUsername());
                        }
                        arrayAdapter.notifyDataSetChanged();

                        for (String username : users) {

                            if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {

                                listView.setItemChecked(users.indexOf(username), true);

                            }

                        }

                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            query.whereEqualTo("isProfessor", false);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        users.clear();

                        for (ParseUser user : objects) {
                            if(user.getList("isFollowing").contains(ParseUser.getCurrentUser().getUsername())) {
                                users.add(user.getUsername());
                            }
                        }
                        arrayAdapter.notifyDataSetChanged();

                        for (String username : users) {

                            if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {

                                listView.setItemChecked(users.indexOf(username), true);

                            }

                        }

                    } else {
                        e.printStackTrace();
                    }
                }
            });

        }





    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.feed){

            Intent intent  = new Intent(getApplicationContext(), ViewMessage.class);

            startActivity(intent);
        }

        if (id == R.id.myfeed){

            Intent intent = new Intent(getApplicationContext(), YourMessages.class);

            startActivity(intent);
        }

        if (id == R.id.subUserList){

            Intent intent = new Intent(getApplicationContext(), SubUserList.class);

            startActivity(intent);
        }



        if (id == R.id.message) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Send a message");
            final EditText messageCont = new EditText(this);
            builder.setView(messageCont);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("AppInfo", String.valueOf(messageCont.getText()));

                    ParseObject message = new ParseObject("Message");

                    message.put("content", String.valueOf(messageCont.getText()));

                    message.put("username", ParseUser.getCurrentUser().getUsername());

                    ParseACL acl = new ParseACL();
                    acl.setPublicReadAccess(true);
                    acl.setWriteAccess(ParseUser.getCurrentUser(), true);

                    message.setACL(acl);

                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null){
                                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Message not sent", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

            return true;
        }

        if (id == R.id.logout){

            ParseUser.logOut();

            Intent intent  = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);


        }

        if (id == R.id.viewSecretMessages){

            Intent intent  = new Intent(getApplicationContext(), ViewSecretMessages.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



}
