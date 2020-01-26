package com.example.carpoolapp;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;


public class FireStoreDatbase implements DataListener {


  public User outUser;

    public void writeUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(user.id)
                .set(user);





    }
    public void getUserProfile(String id)
    {

   final DataListener dl = new DataListener() {
        @Override
        public void newDataReceived(ArrayList<User> user) {
            int stopint =1;
        }
    };
        getRecipesFromDB(dl);
    }
    public static void getRecipesFromDB(DataListener dataListener)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        final ArrayList<User> UserList = new ArrayList<>();

        CollectionReference citiesRef = db.collection("eqyV8SSC3BOg3oWCfTwI");
        int stopint2 =1;
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
          db.collection("users").document("987654321")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserList.add(documentSnapshot.toObject(User.class)) ;

                        int stopint =1;
                        //returndata(returnUser);
                    }

                }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                 // Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
              }
          });
//          try
//          {
//              Thread.sleep(10000);
//          }
//          catch(Exception e)
//          {
//
//          }

        dataListener.newDataReceived(UserList);


    //return null;
    }


    @Override
    public void newDataReceived(ArrayList<User> user) {
        int stopint =1;
    }

    public static void returndata(User user)
    {
        String test = "";
        //returnUser  = user;
        test = user.firstName;
       // outUser
        int stopint =1;

    }

}

