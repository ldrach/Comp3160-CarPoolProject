package com.example.carpoolapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityTAG";
    //for database

    // Button myButton;
    //curent user, curent carpool to display
    public User appUser;
    String carPoolID;
    ArrayList<Object> carpoolUsersList = new ArrayList<Object>();
    mainActivityListAdapter adapter;


    //this sets up the values for the list view (for testing)
    ListView list;
    String[] weekDaysArray;
    String[] maintitle = {
            "Title 1", "Title 2",
    };

    String[] driveCount = {
            "Sub Title 1", "Sub Title 2",
    };
    Integer[] imgid = {
            R.drawable.icon_1, R.drawable.icon_1, R.drawable.icon_1, R.drawable.icon_1, R.drawable.icon_1,
    };
    String[] UserId;
    //----


    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //get user object from previous activity, gets carpoolID, gets users in carpool
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, intentFilter);


        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            appUser = (User) getIntent().getSerializableExtra("User");
            carPoolID = getIntent().getStringExtra("carPoolID");
            Bundle bundle = intent.getExtras();
            carpoolUsersList = (ArrayList<Object>) bundle.getSerializable("UserIDList");
            //Action bar
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" +
                    "Your Carpool ID is: "+((HashMap)carpoolUsersList.get(0)).get("carpoolID").toString().substring(0,6)
                    + "</font>"));

            //populate listAdapter with user info
            imgid = populateListAdapterItems.populateIcon(carpoolUsersList.size() - 1);
            maintitle = populateListAdapterItems.populateMainTitle(carpoolUsersList);
            driveCount = populateListAdapterItems.populateSubTitle(carpoolUsersList);
            UserId = populateListAdapterItems.populateUserID(carpoolUsersList);

            weekDaysArray = new String[14];
            for (int i = 0; i < weekDaysArray.length; i++) {
                weekDaysArray[i] = "mon";
            }

            //----this code sets up an adapter for the list view
            adapter = new mainActivityListAdapter(this, maintitle, driveCount, imgid, weekDaysArray, UserId);

            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);
            //----


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Do you want to Remove " + adapter.totalList.get(i).maintitle + "?")
                            .setMessage("This action can't be undone")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int dialogI) {
                                    String userId = adapter.totalList.get(i).userID;
                                    String carPoolId = (String) ((HashMap) carpoolUsersList.get(0)).get("carpoolID");
                                    FireStoreDatbase fsd = new FireStoreDatbase();
                                    fsd.deleteCarPoolFromUserCarPoolList(carPoolId, userId);
                                    fsd.deleteUserFromCarpool(carPoolId, userId);

                                    Refresh r = new Refresh();
                                    r.launchCarpoolSelect(appUser.id, appUser, MainActivity.this);

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();


                    return false;
                }
            });

            int stopint = 1;
        } else {
            runTestCode();
        }
        //---


        //this tests the carpool select activity
//        myButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                Intent intent = new Intent(MainActivity.this, CarpoolSelectActivity.class);
//                intent.putExtra("User", (Serializable) appUser);
//                MainActivity.this.startActivity(intent);
//
//                // currentContext.startActivity(activityChangeIntent);
//
//
//               // Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    //this method is used for testing
    private void runTestCode() {


    }

    /*Sets up Toolbar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("User", appUser);
                startActivity(intent);
                break;
            case R.id.action_carpoolSelect:
                Refresh r = new Refresh();
                r.launchCarpoolSelect(appUser.id, appUser, MainActivity.this);
                break;
            case R.id.action_logs:
                // Uncomment once Log Activity is complete
                // Intent intent = new Intent(SettingsActivity.this, LogActivity.class);
                // startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
