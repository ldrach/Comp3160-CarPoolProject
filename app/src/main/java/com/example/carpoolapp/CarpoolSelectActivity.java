package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CarpoolSelectActivity extends AppCompatActivity {
    Button carPoolButton1;
    Button carPoolButton2;
    Button carPoolButton3;

    User appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get user object from previous activity
        appUser = (User) getIntent().getSerializableExtra("User");

        //get carpool userIDs for the query
        final FireStoreDatbase dataBase = new FireStoreDatbase();

        //TODO loop through Users carppol lists extracting carpoolUserIDs inorder to query user objects and pass them to Main activity
        dataBase.getCarpoolUserList("fff0aca5-3987-4157-a1c9-b0cb27ac4ad4");

        //populate buttons (test)
        carPoolButton1=findViewById(R.id.button1);
        carPoolButton2=findViewById(R.id.button2);
        carPoolButton3=findViewById(R.id.button3);

        try {
            carPoolButton1.setText(appUser.carPools.get(0));
            carPoolButton2.setText(appUser.carPools.get(1));
            //carPoolButton3.setText(appUser.carPools.get(2));

        } catch (Exception exeption) {

        }


        int stopint = 1;

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
                ArrayList<String> userIDsList = dataBase.userIDs;
                intent.putStringArrayListExtra("UserIDList",userIDsList);
                //----

                CarpoolSelectActivity.this.startActivity(intent);

                // currentContext.startActivity(activityChangeIntent);


            }
        });
    }

}
