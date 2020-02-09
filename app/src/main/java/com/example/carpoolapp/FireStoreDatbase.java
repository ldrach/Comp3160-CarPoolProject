package com.example.carpoolapp;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireStoreDatbase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> userIditem = new HashMap<>();
    ArrayList<String> userIDs ;

    public void writeUser(User user) {



        db.collection("users").document(user.id)
                .set(user);

    }

    public void createCarpool(User user)
    {
        String uniqueID = UUID.randomUUID().toString();
        db.collection("CarPools").document(uniqueID).collection(user.id).document(user.id).set(user);

        //this is used to put a users id in the document within the CarPools document
        userIditem.put("userID",user.id);
        db.collection("CarPools").document(uniqueID).set(userIditem);

        user.updateCarpoolList(uniqueID,db);



    }
    public void addUserToCarpool(User user, String carpoolID,Map carpoolUserIds)
    {



        db.collection("CarPools").document(carpoolID)
                .collection(user.id).document(user.id).set(user);

        //this is used to put a users id in the document within the CarPools document
       /// getCarpoolUserList(carpoolID);
        userIditem.put("userID",user.id);
        db.collection("CarPools").document(carpoolID).set(userIditem, SetOptions.merge());

    }

    public void getUserProfile(String id)
    {

        int stopint2 =1;

        db.collection("users").document("987654321")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User myuser = documentSnapshot.toObject(User.class);

                        int stopint =1;
                    }
                });
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
                        userIDs = new ArrayList<String>(userIDMap.values());

                       int stopint =1;
                    }
                });

    }
}
