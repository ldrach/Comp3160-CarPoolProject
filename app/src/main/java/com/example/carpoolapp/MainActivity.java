package com.example.carpoolapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //for database

   // Button myButton;
    //curent user, curent carpool to display
    public User appUser;
    String carPoolID;
    ArrayList<User> carpoolUsersList = new ArrayList<User>();


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
    //----


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get user object from previous activity, gets carpoolID, gets users in carpool
        //---
        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            appUser = (User) getIntent().getSerializableExtra("User");
            carPoolID = getIntent().getStringExtra("carPoolID");
            Bundle bundle = intent.getExtras();
            carpoolUsersList = (ArrayList<User>) bundle.getSerializable("UserIDList");

            //populate listAdapter with user info
            imgid = populateListAdapterItems.populateIcon(carpoolUsersList.size() - 1);
            maintitle = populateListAdapterItems.populateMainTitle(carpoolUsersList);
            driveCount = populateListAdapterItems.populateSubTitle(carpoolUsersList);
            weekDaysArray = new String[14];
            for (int i = 0; i < weekDaysArray.length; i++) {
                weekDaysArray[i]="mon";
            }

            int stopint = 1;
        } else {
            runTestCode();
        }
        //---


        //----this code sets up an adapter for the list view
        mainActivityListAdapter adapter = new mainActivityListAdapter(this, maintitle, driveCount, imgid, weekDaysArray);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        //----

        //this code is for scheduling the job that updates driveCount
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(this);

       // if(!preferences.getBoolean("firstRunComplete", false)) {
            //schedule the job only once.
            schedualJob();

            //update shared preference
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRunComplete", true);
            editor.commit();
      //  }
        //------


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

    private void schedualJob() {
        JobScheduler jobScheduler = (JobScheduler)getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(this,
                Alarm.class);

        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setPeriodic(1*1000* 60*16).build();
                //.setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }

    //this class is used for testing
    private void runTestCode() {

//        appUser = new User("0sz0p9YCrTh4gV6hv0vvukzNwYf1","Shane","s");
//        User Sara = new User("393938282","Sara","s");
        User newUser;

        FireStoreDatbase dataBase = new FireStoreDatbase();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

         dataBase.createCarpool(appUser);
         //dataBase.addUserToCarpool(appUser,"872f2015-8e28-4e77-9604-d65323ff527f");

        //appUser.getCarpoolList(db);


        // get userIds in order to add a user

        //dataBase.addUserToCarpool(Sara,"fff0aca5-3987-4157-a1c9-b0cb27ac4ad4",carpoolUsersIDList);


        // newUser =
        //dataBase.getUserProfile("123456789");
        int stopint = 1;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = new Intent(MainActivity.this,CarpoolSelectActivity.class);
        intent.putExtra("User",appUser);
        MainActivity.this.startActivity(intent);
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
        switch(menuItem) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
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
