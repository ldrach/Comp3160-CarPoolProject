package com.example.carpoolapp;

//this class is used to hold user information
public class User {

    public String id;
    public String firstName;
    public String lastName;
    public String[] carPools;
    //FirebaseFirestore db;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

    }






}
