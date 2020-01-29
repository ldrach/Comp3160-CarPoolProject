package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.InputConnectionCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String email = emailTextBox.getText().toString().trim();
    String password = passwordTextBox.getText().toString().trim();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("Create", "Success")  ;
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        }
                        else{
                            Log.w("Create", "Failed");
                            Toast.makeText(CreateAccountActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }
                    }
                }
        )


}
