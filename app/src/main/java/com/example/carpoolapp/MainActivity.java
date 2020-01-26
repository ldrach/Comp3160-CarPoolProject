package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //for database

    Button myButton;
    public User samantha;


    //this sets up the values for the list view
    ListView list;
    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5",
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
    };
    Integer[] imgid={
            R.drawable.icon_1,R.drawable.icon_1,R.drawable.icon_1,R.drawable.icon_1,R.drawable.icon_1,
    };
    //----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButton = findViewById(R.id.button4);

        //----this code sets up an adapter for the list view
        mainActivityListAdapter adapter=new mainActivityListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        //----

        //this tests database stuff
          samantha = new User("987654321","Samantha","s");
        User carl = new User("456","Carl","c");
        User newUser;

        FireStoreDatbase dataBase = new FireStoreDatbase();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

       // dataBase.createCarpool(myuser);
       // dataBase.addUserToCarpool(samantha,"872f2015-8e28-4e77-9604-d65323ff527f");
       samantha.getCarpoolList(db);

       // newUser =
        //dataBase.getUserProfile("123456789");
        int stopint =1;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Intent intent = new Intent(MainActivity.this,CarpoolSelectActivity.class);
        //intent.putExtra("User",samantha);
        //MainActivity.this.startActivity(intent);




;

        //----

        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(MainActivity.this,CarpoolSelectActivity.class);
                intent.putExtra("User",samantha);
                MainActivity.this.startActivity(intent);

                // currentContext.startActivity(activityChangeIntent);


            }
        });
    }



}
