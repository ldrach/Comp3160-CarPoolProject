package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailTextBox, passwordTextBox;
    Button login;
    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        //Launch Create Account Activity
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });


        //Sign in user
        /*mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign In", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign In", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                              //      Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });*/

        //Get user info
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email1 = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void loginUser(){

        String email, password;
        email = emailTextBox.getText().toString().trim();
        password = passwordTextBox.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Please enter password...", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {

        if(currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
        }
        else {

        }
    }

    private void initializeUI(){
        emailTextBox = findViewById(R.id.emailEditText);
        passwordTextBox = findViewById(R.id.passwordEditText);
        login = findViewById(R.id.loginButton);
        createAccount = findViewById(R.id.createAccount);
    }

}
