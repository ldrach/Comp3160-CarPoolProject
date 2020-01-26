package com.example.carpoolapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {
    //for database
TextView mytxt;
    mainActivityListAdapter adapter;


    //this sets up the values for the list view
    ListView list;
    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5"

    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
    };
    Integer[] imgid={
            R.drawable.icon_1,R.drawable.icon_1,R.drawable.icon_1,R.drawable.icon_1,R.drawable.icon_1,
    };
    //----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytxt = findViewById(R.id.textView2);
        mytxt.setText("1");

        //----this code sets up an adapter for the list view
        final mainActivityListAdapter adapter=new mainActivityListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        //----

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("987654321")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       User test =  documentSnapshot.toObject(User.class);
                        maintitle[4] = test.firstName;
                        mytxt.setText("test");
                        int stopint =1;
                        //returndata(returnUser);
                        String[] maintitle1 ={
                                "test","Title",
                                "Title 3","Title 4",
                                "Title 5"

                        };
                         adapter.maintitle = maintitle1;
                        list=(ListView)findViewById(R.id.list);
                        list.setAdapter(adapter);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
            }
        });



        //this tests database stuff
        User myuser = new User("987654321","James","j");
        User newUser;

        FireStoreDatbase dataBase = new FireStoreDatbase();





        //db.writeUser(myuser);

        //newUser =
       // dataBase.getUserProfile("987654321");

        //User newUser1 = dataBase.getUser();
        int stopint =1;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();



;

        //----
    }


}
