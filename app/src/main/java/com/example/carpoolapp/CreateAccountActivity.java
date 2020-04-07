package com.example.carpoolapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button createAccountBtn, cancelBtn;
    private ProgressDialog mProgressDialog;
    private EditText mEdtEmail, mEdtPassword, mEdtPassword2, mEdtFName, mEdtLName;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mEdtFName = findViewById(R.id.create_edt_fname);
        mEdtLName = findViewById(R.id.create_edt_lname);
        mEdtEmail = findViewById(R.id.create_edt_email);
        mEdtPassword = findViewById(R.id.crt_password);
        mEdtPassword2 = findViewById(R.id.crt_password2);
        createAccountBtn = findViewById(R.id.crt_create_account_button);
        cancelBtn = findViewById(R.id.create_cancel_button);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = mEdtFName.getText().toString();
                String lname = mEdtLName.getText().toString();
                String email = mEdtEmail.getText().toString();
                String password = mEdtPassword.getText().toString();
                String password2 = mEdtPassword2.getText().toString();

                createUser(fname, lname, email, password, password2);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createUser(final String fname, final String lname, String email, String password, String password2){

        //Validity checks
        if(TextUtils.isEmpty(fname)) {
            mEdtFName.setError("Please Enter a First Name");
            return;
        }
        if(TextUtils.isEmpty(lname)){
            mEdtLName.setError("Please Enter a Last Name");
            return;
        }
        if(TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEdtEmail.setError("Please Enter a Valid Email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mEdtPassword.setError("Please Enter a Valid Password");
            return;
        }
        if (TextUtils.isEmpty(password2)) {
            mEdtPassword2.setError("Please Enter a Valid Password");
            return;
        }
        if (password.length()<8){
            mEdtPassword.setError("Password Must Be Longer Than 8 Characters");
            return;
        }
        if (!password.equals(password2)){
            mEdtPassword.setError("Passwords Do Not Match!");
            return;
        }

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                            //create a new user in the database
                            User newUser = new User(task.getResult().getUser().getUid().toString(),fname,lname);
                            FireStoreDatbase fsdb = new FireStoreDatbase();
                            fsdb.writeUser(newUser, true,false,CreateAccountActivity.this);

//                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
//                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initializeUI(){
        mEdtEmail = findViewById(R.id.create_edt_email);
        mEdtPassword = findViewById(R.id.crt_password);
        mEdtPassword2 = findViewById(R.id.crt_password2);
        createAccountBtn = findViewById(R.id.crt_create_account_button);
        cancelBtn = findViewById(R.id.create_cancel_button);
    }
}
