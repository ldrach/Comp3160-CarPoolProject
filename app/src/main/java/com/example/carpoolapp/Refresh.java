package com.example.carpoolapp;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Refresh {
String TAG = "RefreshClass";

    //used to launch carpool select
    void launchCarpoolSelect(String userid, User appUsertemp, AppCompatActivity loginActivity) {
//        //Clear users carpools and add one (for testing)
//        FireStoreDatbase fsd =new FireStoreDatbase();
//        User use = new User(user.getUid(),"Shane","s");
//        fsd.createCarpool(use);
        final User[] appUser = new User[]{appUsertemp};
        getCurentUser(userid, new getCurentUserCallback() {
            @Override
            public void onCallBack(User user) {
                //create a User object from the FirebaseUser
                appUser[0] = new User(user.id, user.firstName, user.lastName);



                final ArrayList<ArrayList<Object>> totalUserList = new ArrayList<>();

                getUsersCarpoolList(appUser[0].id, new FirestoreCallback() {
                    @Override
                    public void OnCallback(ArrayList<User> userList) { //userList is a list of the logged in user (list always has length of one)
                        Log.d(TAG, "complete");


                        final int carpoolListLength = userList.get(0).carPools.size() - 1;
                        //if no carpools
                        if(carpoolListLength ==-1)
                        {
                            Intent intent = new Intent(loginActivity, CarpoolSelectActivity.class);
                            intent.putExtra("Carpools", (Serializable) totalUserList);
                            intent.putExtra("user", (Serializable) appUser[0]);
                            loginActivity.startActivity(intent);
                        }
                        for (int i = 0; i <= carpoolListLength; i++) {
                            getUsersInCarpool(appUser[0],carpoolListLength, userList.get(0).carPools.get(i), new FirestoreCallback() {
                                @Override
                                public void OnCallback(ArrayList<User> userList) {

                                }

                                @Override
                                public void OnCallbackTotalCarpoolList(ArrayList<ArrayList<Object>> totalCarpoolList) {

                                    totalUserList.add(totalCarpoolList.get(0));
                                    //all the carpools are in and we can send it back to the listener
                                    if (totalUserList.size() == carpoolListLength + 1) {
                                        Intent intent = new Intent(loginActivity, CarpoolSelectActivity.class);
                                        intent.putExtra("Carpools", (Serializable) totalUserList);
                                        intent.putExtra("user", (Serializable) appUser[0]);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //finish();
                                        loginActivity.startActivity(intent);
                                        loginActivity.finish();
                                    }

                                }
                            });
                        }
                    }

                    @Override
                    public void OnCallbackTotalCarpoolList(ArrayList<ArrayList<Object>> totalCarpoolList) {

                    }
                });
            }
        });
    }

    private void getUsersInCarpool(User appUser, final int carpoolListLength, String carpoolID, final FirestoreCallback fireCallBack) {
        //get database instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //list containing a carpool with users in them
        final ArrayList<ArrayList<Object>> totalUserList = new ArrayList<>();

        db.collection("CarPools").document(carpoolID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map userIDMap = documentSnapshot.getData();
                        //this is a list of users in the carpool with the carpoolID as index 0
                        ArrayList<String> userIDs = new ArrayList<String>(userIDMap.values());

                        //get the actual user objects
                        //---
                        final ArrayList<Object> userList = new ArrayList<Object>();

                        //adds carpool info (users id and carpools id)
                        Map<String, Object> info = new HashMap<>();
                        info.put("userID",appUser.id);
                        info.put("carpoolID",userIDs.get(0));
                        userList.add(info);

                        final int userIdListLength = userIDs.size();
                        // the first item is the carpoolID not a userID but we still ned it
                        for (int index = 1; index < userIdListLength; index++) {


                            //get the users in the carpool
                            db.collection("CarPools").document(userIDs.get(0)).collection(userIDs.get(index)).document(userIDs.get(index))
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                              @Override
                                                              public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                  User myuser = documentSnapshot.toObject(User.class);

                                                                  userList.add(myuser);


                                                                  int stopint = 1;
                                                                  //used in carpool select to get all users from all of the appUsers carpools eg. Sam belongs to 3 carpools with 4 people in each.
                                                                  // Get a list with 3 elements containing 4 User objects each
                                                                  if (userList.size() == userIdListLength) {
                                                                      totalUserList.add(userList);
                                                                      fireCallBack.OnCallbackTotalCarpoolList(totalUserList);
                                                                  }


                                                              }

                                                          }
                                    ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    int stopint =1;
                                }
                            });
                        }
                        //---
                    }
                });
    }

    private void getUsersCarpoolList(final String userId, final FirestoreCallback fireCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<User> itemList = new ArrayList<>();

        final User sendUser;
       // showProgressDialog();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                              User myuser = documentSnapshot.toObject(User.class);
                                              itemList.add(myuser);
                                              // carPools = myuser.carPools;
                                              // continueBool = true;
                                              fireCallback.OnCallback(itemList);


                                              // LoginActivity.startCarpoolSelect()
                                              int stopint = 1;
                                          }


                                      }
                );
    }

    private interface FirestoreCallback {
        void OnCallback(ArrayList<User> userList);

        void OnCallbackTotalCarpoolList(ArrayList<ArrayList<Object>> totalCarpoolList);


    }

    private void getCurentUser(String id, getCurentUserCallback cb)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User myuser = documentSnapshot.toObject(User.class);
                        cb.onCallBack(myuser);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    private interface getCurentUserCallback
    {
        void onCallBack(User user);
    }
}
