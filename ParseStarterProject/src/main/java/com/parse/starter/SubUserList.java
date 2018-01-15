package com.parse.starter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SubUserList extends AppCompatActivity {


    ArrayList<String> users;
    ArrayAdapter arrayAdapter;
    ListView listView;
    ArrayList<String> selectedUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        users = new ArrayList<String>();


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);

        listView = (ListView) findViewById(R.id.subUserList);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(arrayAdapter);

        for (Object user: ParseUser.getCurrentUser().getList("isFollowing")){

            users.add(user.toString());

        }

        selectedUsers = new ArrayList<String>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()) {

                    selectedUsers.add(users.get(position));

                } else {
                    selectedUsers.remove(users.get(position));

                }


            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_user_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sendmessage){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Send a message");
            final EditText messageCont = new EditText(this);
            builder.setView(messageCont);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("AppInfo", String.valueOf(messageCont.getText()));
                    //List<String> emptyList = new ArrayList<String>();
                    if (selectedUsers.size() != 0){

                        ParseObject message = new ParseObject("PersonalMessages");

                        message.put("content", String.valueOf(messageCont.getText()));

                        message.put("username", ParseUser.getCurrentUser().getUsername());

                        message.put("subList", selectedUsers);

                        ParseACL acl = new ParseACL();
                        acl.setPublicReadAccess(true);
                        acl.setWriteAccess(ParseUser.getCurrentUser(), true);

                        message.setACL(acl);

                        message.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Message not sent", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }else{

                        Toast.makeText(getApplicationContext(), "Please Select Users", Toast.LENGTH_LONG).show();
                    }

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

        return super.onOptionsItemSelected(item);
    }
}
