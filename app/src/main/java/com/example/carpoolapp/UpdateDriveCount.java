package com.example.carpoolapp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

public class UpdateDriveCount {

public ArrayList<ArrayList<Object>> totalUsers =new ArrayList<>();
    public void onCreate() {

    }

    public void updateDriveCount(Context cxt, String userId) {

        // get ms until 8:00 ish
        Calendar c1 = Calendar.getInstance();
        Date todayTime = c1.getTime();
        c1.set(Calendar.HOUR, 8);
        Date todayEightOclock = c1.getTime();
        long ms = todayEightOclock.getTime() - todayTime.getTime();
        // if (ms > 0)
        //  return;

        getusers(userId, new UserItemCallBack() {
                    @Override
                    public void OnCallback(ArrayList<Object> itemList) {
                        int stopint = 0;
                        totalUsers.add(itemList);
                    }

            @Override
            public void finished() {
                int stopint = 0;
            }
        });



//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Alan");
//        db.collection("AlarmTest").document("test")
//                .set(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("Alarm:s", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Alarm:s", "Error writing document", e);
//                    }
//                });


    }
    private void getusers(String userId, UserItemCallBack callBack)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User myuser = documentSnapshot.toObject(User.class); //get users carpools

                for (String carpoolID : myuser.carPools) {
                    db.collection("CarPools").document(carpoolID)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map userIDMap = documentSnapshot.getData(); // get users in carpool
                            List keys = new ArrayList(userIDMap.keySet());


                            for (int i = 1; i < userIDMap.size(); i++) {
                                db.collection("CarPools").document((String) userIDMap.get(keys.get(0)))
                                        .collection((String) userIDMap.get(keys.get(i))).document((String) userIDMap.get(keys.get(i)))
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User myuser = documentSnapshot.toObject(User.class); // get user info
                                       // userIDMap.get("0");

                                        ArrayList<Object> itemArrayList = new ArrayList<>();
                                        itemArrayList.add(myuser);
                                        itemArrayList.add( userIDMap.get("0"));

                                        callBack.OnCallback(itemArrayList);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i(TAG, "failed3");
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "failed2");
                        }
                    });
                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "failed");
            }
        });
    }
    private interface UserItemCallBack
    {
        void OnCallback(  ArrayList<Object> itemList );
        void finished();

    }

}



