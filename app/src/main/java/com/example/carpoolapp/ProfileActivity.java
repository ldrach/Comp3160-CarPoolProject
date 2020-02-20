package com.example.carpoolapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private EditText mEditTextEmail, mEditTextEmail2, mEditTextPassword, mEditTextPassword2, mEditTextEmailReset;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mTextViewProfile;
    private Button updateCarpoolBtn, updateEmailBtn, updatePasswordBtn, deleteBtn, signOutBtn;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEditTextEmail = findViewById(R.id.field_email);
        mEditTextEmail2 = findViewById(R.id.field_email2);
        mEditTextPassword = findViewById(R.id.field_password);
        mEditTextPassword2 = findViewById(R.id.field_password2);
        mTextViewProfile = findViewById(R.id.profile);

        updateCarpoolBtn = findViewById(R.id.update_carpool_button);
        updateEmailBtn = findViewById(R.id.update_email_button);
        updatePasswordBtn = findViewById(R.id.update_password_button);
        deleteBtn = findViewById(R.id.delete_button);
        signOutBtn = findViewById(R.id.sign_out);

        initializeUI();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };

        updateCarpoolBtn.setOnClickListener(new View.OnClickListener() { //Issue #46
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();

            }
        });

        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (validateEmail(mEditTextEmail)) {
                    updateEmail(user);
                }
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (validatePassword()) {
                    updatePassword(user);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                alert.setMessage("Delete " + user.getEmail() + "?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUser(user);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User's profile
            mTextViewProfile.setText("Firebase ID: " + user.getUid());
            mTextViewProfile.append("\n");
            mTextViewProfile.append("DisplayName: " + user.getDisplayName());
            mTextViewProfile.append("\n");
            mTextViewProfile.append("Email: " + user.getEmail());
                                                                                //Issue #46
            /*mTextViewProfile.append("\n");
            mTextViewProfile.append("CarPool Code: " + user.getCarPoolCode());*/

            findViewById(R.id.update_email_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.update_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_field).setVisibility(View.VISIBLE);
            findViewById(R.id.update_carpool_field).setVisibility(View.VISIBLE);
        } else {
            mTextViewProfile.setText(R.string.signed_out);
            findViewById(R.id.update_email_fields).setVisibility(View.GONE);
            findViewById(R.id.update_password_fields).setVisibility(View.GONE);
            findViewById(R.id.delete_fields).setVisibility(View.GONE);
            findViewById(R.id.sign_out_field).setVisibility(View.GONE);
            findViewById(R.id.update_carpool_field).setVisibility(View.GONE);
        }
        hideProgressDialog();
    }

    private void updateEmail(FirebaseUser user) {
        showProgressDialog();
        user.updateEmail(mEditTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mTextViewProfile.setTextColor(Color.DKGRAY);
                    mTextViewProfile.setText(getString(R.string.updated, "User email"));
                } else {
                    mTextViewProfile.setTextColor(Color.RED);
                    mTextViewProfile.setText(task.getException().getMessage());
                }
                hideProgressDialog();
            }
        });
    }

    private void updatePassword(FirebaseUser user) {
        showProgressDialog();
        user.updatePassword(mEditTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        mTextViewProfile.setTextColor(Color.DKGRAY);
                        mTextViewProfile.setText(getString(R.string.updated, "User password"));
                    } else {
                        mTextViewProfile.setTextColor(Color.RED);
                        mTextViewProfile.setText(task.getException().getMessage());
                    }
                    hideProgressDialog();
                }
            }
        });
    }

    private void deleteUser(FirebaseUser user) {
        showProgressDialog();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mTextViewProfile.setTextColor(Color.DKGRAY);
                    mTextViewProfile.setText("User account deleted.");
                } else {
                    mTextViewProfile.setTextColor(Color.RED);
                    mTextViewProfile.setText(task.getException().getMessage());
                }
                hideProgressDialog();
            }
        });
    }

    private void signOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.logout);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                updateUI(null);
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    private boolean validateEmail(EditText edt) {
        String email = edt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edt.setError("Required.");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt.setError("Invalid.");
            return false;
        } else if(!mEditTextEmail.equals(mEditTextEmail2)){
            edt.setError("Emails must match.");
            return false;
        } else {
            edt.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        if (TextUtils.isEmpty(mEditTextPassword.toString())) {
            mEditTextPassword.setError("Please Enter a Valid Password");
            return false;
        }
        if (TextUtils.isEmpty(mEditTextPassword2.toString())) {
            mEditTextPassword2.setError("Please Enter a Valid Password");
            return false;
        }
        if (mEditTextPassword.length()<8){
            mEditTextPassword.setError("Password Must Be Longer Than 8 Characters");
            return false;
        }
        if (!mEditTextPassword.equals(mEditTextPassword2)){
            mEditTextPassword.setError("Passwords Do Not Match!");
            return false;
        }
        else {
            mEditTextPassword.setError(null);
            return true;
        }
    }

    private void initializeUI(){
        mEditTextEmail = findViewById(R.id.field_email);
        mEditTextEmail2 = findViewById(R.id.field_email2);
        mEditTextPassword = findViewById(R.id.field_password);
        mEditTextPassword2 = findViewById(R.id.field_password2);
        mTextViewProfile = findViewById(R.id.profile);

        updateCarpoolBtn = findViewById(R.id.update_carpool_button);
        updateEmailBtn = findViewById(R.id.update_email_button);
        updatePasswordBtn = findViewById(R.id.update_password_button);
        deleteBtn = findViewById(R.id.delete_button);
        signOutBtn = findViewById(R.id.sign_out);
    }

    //Creates Progress Dialog
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
}
