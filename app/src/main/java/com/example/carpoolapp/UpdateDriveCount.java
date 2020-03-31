package com.example.carpoolapp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

public class UpdateDriveCount {

    public ArrayList<ArrayList<User>> totalUsers = new ArrayList<>();
    public User curentUser;

    public void onCreate() {

    }

    public void updateDriveCount(Context cxt, String userId) {

        // get ms until 8:00 ish
        Calendar c1 = Calendar.getInstance();
        Date todayTime = c1.getTime();
        c1.set(Calendar.HOUR, 8);
        Date todayEightOclock = c1.getTime();
        long ms = todayEightOclock.getTime() - todayTime.getTime();
        if (ms > 0)
            return;

        getusers(userId, new UserItemCallBack() {
            @Override
            public void OnCallback(ArrayList<User> itemList) {

                totalUsers.add(itemList);

                if (totalUsers.size() == curentUser.carPools.size())//finished geting users
                {
                    int stopint = 0;
                    for (ArrayList<User> list : totalUsers) {
                        sortBasedOnDriveCount(list);

                        if (curentUser.id.equals(list.get(0).id))//if curentuser has the lowest drive count
                        {
                            IncromentDriveCount(list.get(0));
                        }
                    }
                }
            }

            @Override
            public void finished() {
                int stopint = 0;
            }
        });
    }


    private void IncromentDriveCount(User user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CarPools").document(user.carPools.get(0)).collection(user.id).document(user.id)
                .update("driveCount", user.driveCount + 1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sortBasedOnDriveCount(ArrayList<User> list) {
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User user, User t1) {
                if (user.driveCount == t1.driveCount) return user.firstName.compareTo(t1.firstName);

                return Integer.compare(user.driveCount, t1.driveCount);
            }
        });
    }

    private void getusers(String userId, UserItemCallBack callBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User myuser = documentSnapshot.toObject(User.class); //get users carpools
                curentUser = myuser;

                for (String carpoolID : myuser.carPools) {
                    db.collection("CarPools").document(carpoolID)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map userIDMap = documentSnapshot.getData(); // get users in carpool
                            List keys = new ArrayList(userIDMap.keySet());

                            ArrayList<User> itemArrayList = new ArrayList<>();
                            for (int i = 1; i < userIDMap.size(); i++) {
                                db.collection("CarPools").document((String) userIDMap.get(keys.get(0)))
                                        .collection((String) userIDMap.get(keys.get(i))).document((String) userIDMap.get(keys.get(i)))
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class); // get user info
                                        // userIDMap.get("0");

                                        user.carPools.add((String) userIDMap.get("0"));
                                        itemArrayList.add(user);

                                        if (itemArrayList.size() == userIDMap.size() - 1) {
                                            callBack.OnCallback(itemArrayList);
                                        }

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

    private interface UserItemCallBack {
        void OnCallback(ArrayList<User> itemList);

        void finished();

    }

}

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



