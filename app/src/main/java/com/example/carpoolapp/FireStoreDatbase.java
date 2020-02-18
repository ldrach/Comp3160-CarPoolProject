package com.example.carpoolapp;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;

public class FireStoreDatbase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //used to put a Users id into the database
    Map<String, Object> userIditem = new HashMap<>();

    ArrayList<String> userIDs ;
    ArrayList<ArrayList> totalIDList = new ArrayList<ArrayList>();

    ArrayList<ArrayList> totalUserList = new ArrayList<ArrayList>();

    public void writeUser(User user) {

        db.collection("users").document(user.id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("s", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("s", "Error writing document", e);
                    }
                });

    }

    public void createCarpool(User user)
    {
        //creates a document
        String uniqueID = UUID.randomUUID().toString();
        db.collection("CarPools").document(uniqueID).collection(user.id).document(user.id).set(user);

        //this is used to put a users id in the document within the CarPools document
        userIditem.put("userID",user.id);
        db.collection("CarPools").document(uniqueID).set(userIditem);

        //add caropoolID to subdocument
        Map<String, Object> carpoolIDMap = new HashMap<>();
        carpoolIDMap.put("0",uniqueID);
        db.collection("CarPools").document(uniqueID).set(carpoolIDMap);

        //update the users list of carpools
        user.updateCarpoolList(uniqueID,db);



    }
    public void addUserToCarpool(User user, String carpoolID,Map carpoolUserIds)
    {
        db.collection("CarPools").document(carpoolID)
                .collection(user.id).document(user.id).set(user);

        //this is used to put a users id in the document within the CarPools document
        userIditem.put("userID",user.id);
        //this has to be changed
        db.collection("CarPools").document(carpoolID).set(userIditem, SetOptions.merge());

    }

    public  void createUser(User user)
    {

    }
//gets a map of users in one of CarPools documents
    public void getCarpoolUserList(String carpoolID)
    {
        db.collection("CarPools").document(carpoolID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       Map  userIDMap= documentSnapshot.getData();
                       //this is a list of users in the carpool with the carpoolID as index 0
                       userIDs = new ArrayList<String>(userIDMap.values());

                       totalIDList.add(userIDs);
                       int stopint =1;

                        //get the actual user objects
                        //---
                        final ArrayList<User> userList = new ArrayList<User>();
                        final int userIdListLength = userIDs.size();
                        //skip the first item because its the carpoolID not a userID
                        for (int index = 1;index < userIdListLength; index++)
                        {
                                db.collection("CarPools").document(userIDs.get(0)).collection(userIDs.get(index)).document(userIDs.get(index))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                User myuser = documentSnapshot.toObject(User.class);

                                                userList.add(myuser);

                                                int stopint =1;
                                                //used in carpool select to get all users from all of the appUsers carpools eg. Sam belongs to 3 carpools with 4 people in each.
                                                // Get a list with 3 elements containing 4 User objects each
                                                if(userList.size() == userIdListLength-1)
                                                {
                                                    totalUserList.add(userList);
                                                }

                                            }
                                        });



                        }
                        //---


                    }
                });

    }
}
