package com.example.carpoolapp;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class FireStoreDatbase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void writeUser(User user) {



        db.collection("users").document(user.id)
                .set(user);

    }

    public void createCarpool(User user)
    {
        String uniqueID = UUID.randomUUID().toString();
        db.collection("CarPools").document(uniqueID).collection(user.id).document(user.id).set(user);
        user.updateCarpoolList(uniqueID,db);



    }
    public void addUserToCarpool(User user, String carpoolID)
    {
        db.collection("CarPools").document(carpoolID)
                .collection(user.id).document(user.id).set(user);
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
}
