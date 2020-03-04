package com.example.carpoolapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class CarPoolLogActivity extends AppCompatActivity {
    carPoolLogDatabase carPoolLogDatabase;
    carPoolLogDocDao carPoolLogDocDao;
    TextView logDoctextView;
    LiveData<List<carPoolLogDoc>> allLogDocLive;
    RecyclerView logDocrecyclerView;
    CarPoolLogAdapter carPoolLogAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.id.);
        carPoolLogDatabase = Room.databaseBuilder(this, carPoolLogDatabase.getClass(), "CarPoolLogDatabase").build();
        carPoolLogDocDao = carPoolLogDatabase.getCarPoolLogDao();
        allLogDocLive = carPoolLogDocDao.getAllcarPoolLogDoc();
        logDocrecyclerView = findViewById(R.id.log_doc_recyclerView);
        carPoolLogAdapter = new CarPoolLogAdapter();
        logDocrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logDocrecyclerView.setAdapter(carPoolLogAdapter);
        //logDoctextView
        allLogDocLive.observe(this, new Observer<List<carPoolLogDoc>>() {
            @Override
            public void onChanged(List<carPoolLogDoc> carPoolLogDocs) {
                carPoolLogAdapter.setAllLogDoc(carPoolLogDocs);
                carPoolLogAdapter.notifyDataSetChanged();
            }
        });
}
}
