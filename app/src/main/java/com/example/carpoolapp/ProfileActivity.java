package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    /*Sets up Toolbar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        Intent intent;
        switch(menuItem) {
            case R.id.action_home:
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.action_settings:
                intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logs:
                // Uncomment once Log Activity is complete
                // intent = new Intent(ProfileActivity.this, LogActivity.class);
                // startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
