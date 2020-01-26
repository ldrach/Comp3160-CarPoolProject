package com.example.carpoolapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //for database



    //this sets up the values for the list view
    ListView list;
    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5",
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

        //----this code sets up an adapter for the list view
        mainActivityListAdapter adapter=new mainActivityListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        //----

        //this tests database stuff
        User myuser = new User("987654321","James","j");
        User newUser;

        FireStoreDatbase dataBase = new FireStoreDatbase();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                int stopint =1;
                            }
                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                            int stopint =1;
                        }
                    }
                });

        //db.writeUser(myuser);

       // newUser =
        dataBase.getUserProfile("123456789");
        int stopint =1;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();



;

        //----
    }


}
