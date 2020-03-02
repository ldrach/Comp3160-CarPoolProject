package com.example.carpoolapp;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

//this class is used to hold user information
public class User implements Serializable {

    public String id;
    public String firstName;
    public String lastName;
    public List<String> carPools = new ArrayList<>();
    public int driveCount;


    /**
     create a random user (for testing)
     */
    public User() {
        Random generate = new Random();
        String[] name = {"John", "Marcus", "Susan", "Henry","RUBY",
                "Emily",
                "Grace	",
                "Chloe",
                "Sophie	",
                "Amelia",
                "Ella",
                "Charlotte",
                "Lucy",
                "Megan",
                "Ellie",
                "Isabelle",
                "Isabella",
                "Hannah"};

        this.firstName  = name[generate.nextInt(18)];
        this.lastName  = name[generate.nextInt(18)];
        this.id = UUID.randomUUID().toString();
        this.driveCount = generate.nextInt(20);
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.driveCount =0;
    }

    // gets users carpools
    public void getCarpools(FirebaseFirestore db)
    {

        db.collection("CarPools").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User myuser = documentSnapshot.toObject(User.class);

                        int stopint =1;
                    }
                });
    }
    //used to add a carpool to a users list of carpools
    public void updateCarpoolList(String carpoolString, FirebaseFirestore db)
    {
        //get curent carpool list
        this.getCarpoolList(db);
        this.carPools.add(carpoolString);
        db.collection("users").document(this.id)
                .set(this);
    }
    //gets users carpool list
    public void getCarpoolList(FirebaseFirestore db)
    {
        db.collection("users").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User myuser = documentSnapshot.toObject(User.class);
                        carPools = myuser.carPools;
                        int stopint =1;
                    }


                }
                );

    }





}
