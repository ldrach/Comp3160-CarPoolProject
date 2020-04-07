package com.example.carpoolapp;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FireStoreDatbase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //used to put a Users id into the database
    Map<String, Object> userIditem = new HashMap<>();

    ArrayList<String> userIDs ;
    ArrayList<ArrayList> totalIDList = new ArrayList<ArrayList>();

    ArrayList<ArrayList> totalUserList = new ArrayList<ArrayList>();

    public void writeUser(User user, boolean startCarpoolSelect, boolean overwrite , AppCompatActivity context) {

        final User accesableUser = user;

        db.collection("users").document(user.id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //check if document has data
                        Map<String, Object> map = documentSnapshot.getData();
                        if(map == null || overwrite == true)
                        {
                            db.collection("users").document(user.id)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //start carpoolSelect
                                            if(startCarpoolSelect) {
                                                showProgressDialog(context);
                                                Refresh r = new Refresh();
                                                r.launchCarpoolSelect(accesableUser.id, null,context );
                                                Log.d("s", "DocumentSnapshot successfully written!");
                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("s", "Error writing document", e);
                                        }
                                    });

                        }
                        else
                        {
                            //user exists, start carpoolSelect
                            //start carpoolSelect
                            if(startCarpoolSelect) {
                                showProgressDialog(context);
                                Refresh r = new Refresh();
                                r.launchCarpoolSelect(accesableUser.id, null,context );
                                Log.d("s", "DocumentSnapshot successfully written!");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("s", "Error writing document");
                    }
                });
//


    }
    public void createCarpool(User user)
    {
        //creates a document
        String uniqueID = UUID.randomUUID().toString();
        uniqueID = uniqueID.substring(0,6);

        addUserToCarpool(user,uniqueID, null);

        //this is used to put a users id in the document within the CarPools document
        userIditem.put("userID",user.id);
        db.collection("CarPools").document(uniqueID).set(userIditem);

        //add caropoolID to subdocument
        Map<String, Object> carpoolIDMap = new HashMap<>();
        carpoolIDMap.put("0",uniqueID);
        db.collection("CarPools").document(uniqueID).set(carpoolIDMap);

        // add userID to subdocument

        //update the users list of carpools
        user.updateCarpoolList(uniqueID,db);

    }
    public void deleteCarPoolFromUserCarPoolList(String carpoolId, String userId)
    {
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);
                        //remove and reupload
                        try
                        {
                            user.carPools.remove(carpoolId);
                            db.collection("users").document(userId).set(user);
                        }
                        catch(Exception e)
                        {
                            Log.d("FireStroreDatabase", e.getMessage());
                        }

                    }
                });

    }
    public void writeCarPoolUserToCarPoolList(String carpoolId, String userId)
    {
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = task.getResult().toObject(User.class);

                        if(user.carPools.contains(carpoolId)!= true)
                        user.carPools.add(carpoolId);


                        db.collection("users").document(userId).set(user);
                    }
                });

    }
    public void deleteUserFromCarpool(String carpoolID,String userId )
    {
        db.collection("CarPools").document(carpoolID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> map = documentSnapshot.getData();
                        map.get(userId);
                        for (Map.Entry item : map.entrySet()) {
                            if (item.getValue().toString().compareTo(userId) == 0) {
                              //  map.remove(item.getKey());
                                Map<String,Object> updates = new HashMap<>();
                                updates.put(item.getKey().toString(), FieldValue.delete());
                                db.collection("CarPools").document(carpoolID).update(updates);
                                break;
                            }
                        }

                    }
                });

        db.collection("CarPools").document(carpoolID).collection(carpoolID).document().delete();

    }
    public void deleteCarpool(String carpoolId)
    {
        db.collection("CarPools").document(carpoolId)
                .delete();
    }

    public void addUserToCarpool(User user, final String carpoolID, AppCompatActivity context)
    {
        //add new user collection
        user.carPools.clear();
        user.carPools.add(carpoolID);
        db.collection("CarPools").document(carpoolID).collection(user.id).document(user.id).set(user);


        //get subdocuments hashMap
        final User accesableUser = user;
        db.collection("CarPools").document(carpoolID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userIditem = documentSnapshot.getData();
                        try
                        {
                            userIditem.put(String.valueOf(userIditem.size()),accesableUser.id);

                            db.collection("CarPools").document(carpoolID).set(userIditem);
                        }
                        catch (Exception e)
                        {
                            if(context!= null)
                            Toast.makeText(context, "Error Adding User, check your input.", Toast.LENGTH_LONG).show();
                        }

                    }
                    });


    }
    public void localAddUserToCarpool(User user, final String carpoolID, AppCompatActivity context)
    {

        //get subdocuments hashMap
        final User accesableUser = user;
        db.collection("CarPools").document(carpoolID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userIditem = documentSnapshot.getData();
                        try
                        {
                            userIditem.put(String.valueOf(userIditem.size()),accesableUser.id);

                            //set userId in "Carpool" document
                            db.collection("CarPools").document(carpoolID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Map<String, Object> map = documentSnapshot.getData();
                                    //map.get(userId);
                                    boolean duplicate = false;
                                    for (Map.Entry item : map.entrySet()) {
                                        if (item.getValue().toString().compareTo(user.id) == 0) {
                                            duplicate = true;
                                        }
                                    }
                                    if(!duplicate)
                                    db.collection("CarPools").document(carpoolID).set(userIditem);

                                }
                            });
                            user.carPools.clear();
                            user.carPools.add(carpoolID);
                            db.collection("CarPools").document(carpoolID).collection(user.id).document(user.id).set(user);


                            writeCarPoolUserToCarPoolList(carpoolID, user.id);
                        }
                        catch (Exception e)
                        {
                            if(context!= null)
                                Toast.makeText(context, "Error Adding User, check your input.", Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Refresh r = new Refresh();
                r.launchCarpoolSelect(user.id, user,context);
            }
        });


    }

    public  void writeCarpoolUser(User user)
    {
        db.collection("CarPools").document(user.carPools.get(0)).collection(user.id).document(user.id)
            .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
                       //this is a list of users in the carpool with the carpoolID as index 0
                       userIDs = new ArrayList<String>(userIDMap.values());

                       totalIDList.add(userIDs);

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

                                                //reverse list
                                                // Arraylist for storing reversed elements
                                                for (int i = 0; i < userList.size() / 2; i++) {
                                                    User temp = userList.get(i);
                                                    userList.set(i, userList.get(userList.size() - i - 1));
                                                    userList.set(userList.size() - i - 1, temp);
                                                }



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
    //Creates Progress Dialog
    public void showProgressDialog(AppCompatActivity context) {
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
           // mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }
}
