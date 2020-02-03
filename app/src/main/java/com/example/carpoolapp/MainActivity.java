package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //for database

    Button myButton;
    //curent user, curent carpool to display
    public User appUser;
    String carPoolID;


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

        //get user object from previous activity, gets carpoolID
        Intent intent = getIntent();
        if (intent.hasExtra("User")) {
            appUser = (User) getIntent().getSerializableExtra("User");
            carPoolID = getIntent().getStringExtra("carPoolID");
            getCarPoolData();
        } else {
            runTestCode();
        }
        //---

        //----this code sets up an adapter for the list view
        mainActivityListAdapter adapter=new mainActivityListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        //----






;

        //----
//this tests the carpool select activity
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(MainActivity.this,CarpoolSelectActivity.class);
                intent.putExtra("User", appUser);
                MainActivity.this.startActivity(intent);

                // currentContext.startActivity(activityChangeIntent);


            }
        });
    }

    private void runTestCode()
    {
        //this tests database stuff
        appUser = new User("987654321","Samantha","s");
        User carl = new User("456","Carl","c");
        User newUser;

        FireStoreDatbase dataBase = new FireStoreDatbase();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // dataBase.createCarpool(myuser);
        // dataBase.addUserToCarpool(appUser,"872f2015-8e28-4e77-9604-d65323ff527f");

        appUser.getCarpoolList(db);
        dataBase.addUserToCarpool(carl,"65cd75ca-004a-41e4-be20-66f3e5ffa09c");


        // newUser =
        //dataBase.getUserProfile("123456789");
        int stopint =1;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Intent intent = new Intent(MainActivity.this,CarpoolSelectActivity.class);
        //intent.putExtra("User",appUser);
        //MainActivity.this.startActivity(intent);
    }

    private void getCarPoolData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Task docRef = db.collection("CarPools").document(carPoolID).get();

        db.collection("CarPools").document(carPoolID).collection("456"

        )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*db.collection("CarPools").document(carPoolID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map myuser = documentSnapshot.getData();


                        int stopint =1;
                    }
                });*/

   /* DocumentReference docRef = db.collection("CarPools").document(carPoolID);
docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("", "No such document");
                }
            } else {
                Log.d("", "get failed with ", task.getException());
            }
        }
    });*/
    }



}
