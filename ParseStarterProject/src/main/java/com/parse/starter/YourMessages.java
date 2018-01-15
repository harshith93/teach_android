package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourMessages extends AppCompatActivity {

    ListView listView;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> messageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.yourMessages);
        messageData = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(this, messageData, android.R.layout.simple_list_item_2, new String[] {"content", "createdAt"}, new int[] {android.R.id.text1, android.R.id.text2});

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageObjects, ParseException e) {
                if (e == null) {

                    if (messageObjects.size() > 0){



                        for (ParseObject messageObject : messageObjects){

                            Map<String, String> message = new HashMap<String, String>(2);

                            message.put("content", messageObject.getString("content"));
                            message.put("createdAt", "At " + messageObject.getCreatedAt().toString());

                            messageData.add(message);

                        }



                        listView.setAdapter(simpleAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                //Intent intent = new Intent(getApplicationContext(), YourFullMessage.class);
                                //startActivity(intent);

                                String text1 = ((TextView)(view.findViewById(android.R.id.text1))).getText().toString();

                                String text2 = ((TextView)(view.findViewById(android.R.id.text2))).getText().toString();

                                Intent intent = new Intent(getApplicationContext(),YourFullMessage.class);

                                intent.putExtra("message", text1);
                                intent.putExtra("item2", text2);


                                startActivity(intent);



                            }
                        });
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            ParseUser.logOut();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}


