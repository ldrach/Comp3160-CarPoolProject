package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class CarpoolSelectActivity extends AppCompatActivity {
    Button carPoolButton1;
    Button carPoolButton2;
    Button carPoolButton3;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_carpool_select);


        //get user object from previous activity
        carpoolsList = (ArrayList<ArrayList<Object>>) getIntent().getSerializableExtra("Carpools");

        //get carpool userIDs for the query
        final FireStoreDatbase dataBase = new FireStoreDatbase();
//        for(String carpoolID : appUser.carPools)// will need to change later for reminder times
//        {
//            dataBase.getCarpoolUserList(carpoolID);
//        }
        //---

        //----this code sets up an adapter for the list view
        buttonTextArray =PopulateCarPoolSelectListAdapterItems.populateCarpools(carpoolsList);
        usersArray = PopulateCarPoolSelectListAdapterItems.populateUsers(carpoolsList);

        CarPoolSelectListAdapter adapter=new CarPoolSelectListAdapter(this, buttonTextArray,usersArray);
        carPoolList=(ListView)findViewById(R.id.carPoolSelectListView);
        carPoolList.setAdapter(adapter);
        //----


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
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();

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
                Toast.makeText(getApplicationContext(),"New Carpool Created " , Toast.LENGTH_LONG).show();
                String userID  =(String)((HashMap) (carpoolsList.get(0).get(0))).get("userID");
                appUser = new User(userID, "noname","noname");
                fsd.createCarpool(appUser);

                //restart activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

    }


}
