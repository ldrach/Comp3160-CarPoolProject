package com.example.carpoolapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CarpoolSelectActivity extends AppCompatActivity {

    User appUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appUser = (User)getIntent().getSerializableExtra("User");

        int stopint =1;

    }

}
