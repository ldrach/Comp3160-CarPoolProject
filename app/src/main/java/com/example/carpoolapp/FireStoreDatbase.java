package com.example.carpoolapp;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreDatbase {


    public void writeUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(user.id)
                .set(user);





    }

    public void getUserProfile(String id)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                        User myuser = documentSnapshot.toObject(User.class);

                        int stopint =1;
                    }
                });
    }
}
