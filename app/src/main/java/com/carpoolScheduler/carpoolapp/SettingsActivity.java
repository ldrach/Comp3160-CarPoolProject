package com.carpoolScheduler.carpoolapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private EditText mEditTextEmail, mEditTextPassword, mEditTextPassword2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mTextViewProfile;
    private Button updateEmailBtn, updatePasswordBtn, deleteBtn, signOutBtn, shareBtn;
    private ProgressDialog mProgressDialog;

    User appUser;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, intentFilter);

        mEditTextEmail = findViewById(R.id.field_email);
        mEditTextPassword = findViewById(R.id.field_password);
        mEditTextPassword2 = findViewById(R.id.field_password2);
        mTextViewProfile = findViewById(R.id.profile);


        updateEmailBtn = findViewById(R.id.update_email_button);
        updatePasswordBtn = findViewById(R.id.update_password_button);
        deleteBtn = findViewById(R.id.delete_button);
        signOutBtn = findViewById(R.id.sign_out);
        shareBtn = findViewById(R.id.share_button);

        //get user
        Intent intent = getIntent();

        if (intent.hasExtra("User")) {
            appUser = (User) getIntent().getSerializableExtra("User");
        }

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

        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                showProgressDialog();
                if (validateEmail(mEditTextEmail)) {
                    updateEmail(user);
                    updateUI(user);
                }
                hideProgressDialog();
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (validatePassword()) {
                    updatePassword(user);
                    updateUI(user);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
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
                signOut();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClicked();
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
            mTextViewProfile.setText("");
            mTextViewProfile.append("Name: " + user.getDisplayName());
            mTextViewProfile.append("\n");
            mTextViewProfile.append("Email: " + user.getEmail());
            //Issue #46
            /*mTextViewProfile.append("\n");
            mTextViewProfile.append("CarPool Code: " + user.getCarPoolCode());*/

            findViewById(R.id.update_email_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.update_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_field).setVisibility(View.VISIBLE);

        } else {
            mTextViewProfile.setText(R.string.signed_out);
            findViewById(R.id.update_email_fields).setVisibility(View.GONE);
            findViewById(R.id.update_password_fields).setVisibility(View.GONE);
            findViewById(R.id.delete_fields).setVisibility(View.GONE);
            findViewById(R.id.sign_out_field).setVisibility(View.GONE);

        }
        hideProgressDialog();
    }

    private void updateEmail(FirebaseUser user) {
        showProgressDialog();
        user.updateEmail(mEditTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Email Updated",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SettingsActivity.this, "Something Went Wrong!",
                            Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SettingsActivity.this, "Password Updated",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Something Went Wrong!",
                                Toast.LENGTH_SHORT).show();
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
                    updateUI(null);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                    sendBroadcast(broadcastIntent);
                    Toast.makeText(SettingsActivity.this, "Account Deleted",
                            Toast.LENGTH_SHORT).show();
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
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
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
        } else {
            edt.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String password = mEditTextPassword.getText().toString();
        String password2 = mEditTextPassword2.getText().toString();

        if (TextUtils.isEmpty(password)) {
            mEditTextPassword.setError("Please Enter a Valid Password");
            return false;
        } else if (TextUtils.isEmpty(password2)) {
            mEditTextPassword2.setError("Please Enter a Valid Password");
            return false;
        } else if (password.length() < 8) {
            mEditTextPassword.setError("Password Must Be Longer Than 8 Characters");
            return false;
        } else if (!(password).equals(password2)) {
            mEditTextPassword.setError("Passwords Do Not Match!");
            return false;
        } else {
            return true;
        }
    }

    private void initializeUI() {
        mEditTextEmail = findViewById(R.id.field_email);
        mEditTextPassword = findViewById(R.id.field_password);
        mEditTextPassword2 = findViewById(R.id.field_password2);
        mTextViewProfile = findViewById(R.id.profile);

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

    /*Set share button clickable*/
    public class DynamicLinksUtil {


        /*
        public static InviteContent generateInviteContent() {
            return new InviteContent(
                    "Hey check out my great app!",
                    "It's like the best app ever.",
                    generateContentLink());
        }
        */



    }
    public  Uri generateContentLink() {
        Uri baseUrl = Uri.parse("https://www.facebook.com/CarpoolScheduler/?carpoolID="+appUser.carPools.get(0));
        //Uri baseUrl = Uri.parse("https://carpoolfirestore.page.link/jTpt");
        //String domain = "https://www.facebook.com/CarpoolScheduler";
        String domain = "https://carpoolfirestore.page.link/?testdata=1";

        DynamicLink link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.carpoolapp")
                        .build())
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.carpoolapp").build())

                .buildDynamicLink();




        return link.getUri();
    }

    private void onShareClicked() {
        Uri link = generateContentLink();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Click the link below to join the Carpool\n\n" + link.toString());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Carpool invite from "+appUser.firstName + " " + appUser.lastName);

        startActivity(Intent.createChooser(intent, "Share Link"));
    }


/*Sets up Toolbar*/
    /*Sets up Toolbar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.action_home:
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logs:
                // Uncomment once Log Activity is complete
                // Intent intent = new Intent(SettingsActivity.this, LogActivity.class);
                // startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
