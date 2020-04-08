package com.example.carpoolapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class CarpoolSelectActivity extends AppCompatActivity {
    private static final String TAG = "CarpoolSelectActivity";
    Button carPoolButton1;
    Button carPoolButton2;
    TextView instructionTextView;
    CarpoolSelectActivity context = CarpoolSelectActivity.this;
    private String inviteCarpoolID = "";


    User appUser;
    ArrayList<ArrayList<Object>> carpoolsList = new ArrayList<>();

    //this sets up the values for the list view
    //---
    ListView carPoolList;
    String[] buttonTextArray ;
    //array for displaying names in carpoolselects carpools
    ArrayList<ArrayList<String>> usersArray;
    //----

    FloatingActionButton addCarpoolFAButton;
    FireStoreDatbase fsd = new FireStoreDatbase();
   // TextView instructionTextView = (TextView) findViewById(R.id.instructionTextView);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_carpool_select);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                intent = new Intent(CarpoolSelectActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, intentFilter);


        //get carpools object from previous activity
        carpoolsList = (ArrayList<ArrayList<Object>>) getIntent().getSerializableExtra("Carpools");
        appUser = (User) getIntent().getSerializableExtra("user");

        //update the info label
        instructionTextView = (TextView)findViewById(R.id.instructionTextView);
        if(carpoolsList.size() == 0)
            instructionTextView.setText("Add a Carpool with the Plus button below.");

        final FireStoreDatbase dataBase = new FireStoreDatbase();

        //----this code sets up an adapter for the list view
        buttonTextArray =PopulateCarPoolSelectListAdapterItems.populateCarpools(carpoolsList);
        usersArray = PopulateCarPoolSelectListAdapterItems.populateUsers(carpoolsList);

        CarPoolSelectListAdapter adapter=new CarPoolSelectListAdapter(this, buttonTextArray,usersArray, appUser, CarpoolSelectActivity.this);
        carPoolList=(ListView)findViewById(R.id.carPoolSelectListView);
        carPoolList.setAdapter(adapter);
        //----

        //this code is for scheduling the job that updates driveCount
        schedualJob();

        //------
        //gets invite code
        Intent intent = getIntent();
        if (intent.hasExtra("inviteCarpoolID")) {
            inviteCarpoolID = (String) getIntent().getSerializableExtra("inviteCarpoolID");
            if(!inviteCarpoolID.equals(""))
                joinCarpool(inviteCarpoolID);
        }


        //populate buttons (test)
        //carPoolButton1=findViewById(R.id.button1);
        addCarpoolFAButton = findViewById(R.id.floatingActionButton);


        try {
            carPoolButton1.setText(appUser.carPools.get(0));
            carPoolButton2.setText(appUser.carPools.get(1));
            //carPoolButton3.setText(appUser.carPools.get(2));

        } catch (Exception exeption) {

        }


        //test "user opens first carpool"
//carPoolButton1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//
//                //send extras
//                Intent intent = new Intent(CarpoolSelectActivity.this,MainActivity.class);
//                intent.putExtra("User",appUser);
//                //send carpool id
//                intent.putExtra("carPoolID",appUser.carPools.get(0));
//                //send carpool userIDs List
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("UserIDList", dataBase.totalUserList.get(0));
//                intent.putExtras(bundle);
//                //intent.putParcelableArrayListExtra("UserIDList",dataBase.totalUserList.get(0));
//                //----
//
//                CarpoolSelectActivity.this.startActivity(intent);
//
//                // currentContext.startActivity(activityChangeIntent);
//
//
//            }
//        });

        // list adapter on click
        carPoolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                appUser.carPools.add((String) ((HashMap) carpoolsList.get(position).get(0)).get("carpoolID"));
                //send extras
                Intent intent = new Intent(CarpoolSelectActivity.this,MainActivity.class);
                intent.putExtra("User",appUser);
                //send carpool id
              //  intent.putExtra("carPoolID",appUser.carPools.get(position));
                //send carpool userIDs List
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserIDList", carpoolsList.get(position));
                intent.putExtras(bundle);
                //intent.putParcelableArrayListExtra("UserIDList",dataBase.totalUserList.get(0));
                //----

                CarpoolSelectActivity.this.startActivity(intent);
            }
        });


        addCarpoolFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CarpoolSelectActivity.this)
                        .setTitle("Add Carpool")
                        .setMessage("Do you want to create a new Carpool or join an Existing one?")
                        .setNeutralButton("New", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(getApplicationContext(),"Creating New Carpool..." , Toast.LENGTH_LONG).show();

                                fsd.createCarpool(appUser);

                                showProgressDialog();
                                Refresh r = new Refresh();
                                r.launchCarpoolSelect(appUser.id, appUser, "",CarpoolSelectActivity.this);

                            }
                        })
                        .setPositiveButton("Existing", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder dialog =new AlertDialog.Builder(CarpoolSelectActivity.this)
                                        .setTitle("Enter ID")
                                        .setMessage("Enter you Co-Workers Carpool Id below.\nYour co-workers id is located at the top of there Carpool");
                                //input
                                final EditText input = new EditText(CarpoolSelectActivity.this);
                                input.setInputType(InputType.TYPE_CLASS_TEXT);

                                dialog.setView(input)
                                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                            try {
                                                String CarpoolId = input.getText().toString();
                                                fsd.localAddUserToCarpool(appUser,CarpoolId, CarpoolSelectActivity.this);
                                                //fsd.writeCarPoolUserToCarPoolList(CarpoolId, appUser.id);
                                                //showProgressDialog();

                                               // finish();


                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), "Error Converting String", Toast.LENGTH_LONG).show();
                                            }
                                    }

                                })

                                .show();

                            }
                        })
                        .show();
            }
        });

    }
    private void joinCarpool(String carpoolID)
    {
        fsd.localAddUserToCarpool(appUser,carpoolID, CarpoolSelectActivity.this);
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
                .setPeriodic(1*1000* 60*60*3)//1000 *60*17
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

    //Creates Progress Dialog
    public void showProgressDialog() {
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }


}
