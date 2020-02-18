package com.example.carpoolapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button createAccountBtn, cancelBtn;
    private ProgressDialog mProgressDialog;
    private EditText mEdtEmail, mEdtPassword, mEdtPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEdtEmail = findViewById(R.id.create_edt_email);
        mEdtPassword = findViewById(R.id.crt_password);
        mEdtPassword2 = findViewById(R.id.crt_password2);
        createAccountBtn = findViewById(R.id.crt_create_account_button);
        cancelBtn = findViewById(R.id.create_cancel_button);

        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(mEdtEmail.getText().toString(), mEdtPassword.getText().toString(), mEdtPassword2.getText().toString());
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

    private void createUser(String email, String password, String password2){

        //Email and Password Validity checks
        if(TextUtils.isEmpty(email)){
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

                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
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
