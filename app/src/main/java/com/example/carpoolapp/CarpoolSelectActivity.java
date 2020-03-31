package com.example.carpoolapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarpoolSelectActivity extends AppCompatActivity {
    private static final String TAG = "CarpoolSelectActivity";
    Button carPoolButton1;
    Button carPoolButton2;
    Button carPoolButton3;

    User appUser;
    ArrayList<User> UserList = new ArrayList<User>();

    //this sets up the values for the list view
    ListView carPoolList;
    String[] buttonTextArray ;

    //----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_carpool_select);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //get user object from previous activity
        appUser = (User) getIntent().getSerializableExtra("User");

        //get carpool userIDs for the query
        final FireStoreDatbase dataBase = new FireStoreDatbase();
        for(String carpoolID : appUser.carPools)// will need to change later for reminder times
        {
            dataBase.getCarpoolUserList(carpoolID);
        }
        //---

        //----this code sets up an adapter for the list view
        buttonTextArray =PopulateCarPoolSelectListAdapterItems.populateCarpools(appUser.carPools);

        CarPoolSelectListAdapter adapter=new CarPoolSelectListAdapter(this, buttonTextArray);
        carPoolList=(ListView)findViewById(R.id.carPoolSelectListView);
        carPoolList.setAdapter(adapter);
        //----

        //this code is for scheduling the job that updates driveCount
        schedualJob();

        //------


        //populate buttons (test)
        carPoolButton1=findViewById(R.id.button1);
        carPoolButton2=findViewById(R.id.button2);


        try {
            carPoolButton1.setText(appUser.carPools.get(0));
            carPoolButton2.setText(appUser.carPools.get(1));
            //carPoolButton3.setText(appUser.carPools.get(2));

        } catch (Exception exeption) {

        }


        //test "user opens first carpool"
carPoolButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //send extras
                Intent intent = new Intent(CarpoolSelectActivity.this,MainActivity.class);
                intent.putExtra("User",appUser);
                //send carpool id
                intent.putExtra("carPoolID",appUser.carPools.get(0));
                //send carpool userIDs List
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserIDList", dataBase.totalUserList.get(0));
                intent.putExtras(bundle);
                //intent.putParcelableArrayListExtra("UserIDList",dataBase.totalUserList.get(0));
                //----

                CarpoolSelectActivity.this.startActivity(intent);

                // currentContext.startActivity(activityChangeIntent);


            }
        });

//        carPoolList.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//            }
//        });

        carPoolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    private void schedualJob() {


        JobScheduler jobScheduler = (JobScheduler)getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);

        //jobScheduler.cancelAll();

        if(checkForJob(1))
            return;

        ComponentName componentName = new ComponentName(this,
                Alarm.class);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("userID", appUser.id);

        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setPeriodic(1*1000* 60*60*3)
                .setExtras(bundle)
        .setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }
    private boolean checkForJob(int jobid)
    {

        JobScheduler scheduler = (JobScheduler) getApplicationContext().getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == jobid ) {
                hasBeenScheduled = true ;
                break ;
            }
        }
        return hasBeenScheduled ;

    }
    private void cancelAllJobs()
    {
        JobScheduler scheduler = (JobScheduler)
                this.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            // if (jobInfo.getId() == jobID) {
            scheduler.cancel(jobInfo.getId());
            Log.i(TAG,"Cancelled Job with ID:" + jobInfo.getId());
            //}
        }
    }


}
