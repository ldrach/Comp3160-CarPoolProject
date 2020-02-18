package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class CarpoolSelectActivity extends AppCompatActivity {
    Button carPoolButton1;
    Button carPoolButton2;
    Button carPoolButton3;

    User appUser;
    ArrayList<User> UserList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_select);
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
    }
    public void getCarpoolUsers()
    {

    }

}
