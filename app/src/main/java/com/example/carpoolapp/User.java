package com.example.carpoolapp;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//this class is used to hold user information
public class User implements Serializable {

    public String id;
    public String firstName;
    public String lastName;
    public List<String> carPools = new ArrayList<>();


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

    }

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
    public void updateCarpoolList(String carpoolString, FirebaseFirestore db)
    {
        this.carPools.add(carpoolString);
        db.collection("users").document(this.id)
                .set(this);
    }
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
                });
    }






}
