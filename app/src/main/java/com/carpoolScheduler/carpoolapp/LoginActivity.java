package com.carpoolScheduler.carpoolapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button signInBtn, createAccountBtn;
    GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 1;
    private EditText mEdtEmail, mEdtPassword;
    private ProgressDialog mProgressDialog;

    //needed for launching carpool select
    private static User appUser;
    private boolean lock = false;
    private String inviteCarpoolId = "";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null)
        {
            handleDeepLink(intent);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        if(intent!=null)
        {
            handleDeepLink(intent);
        }

        mEdtEmail = findViewById(R.id.emailEditText);
        mEdtPassword = findViewById(R.id.passwordEditText);
        signInBtn = findViewById(R.id.email_sign_in_button);
        createAccountBtn = findViewById(R.id.login_create_account_button);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        // Initializes Google Sign In
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });


        //Regular Login Button onClick
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(mEdtEmail.getText().toString(), mEdtPassword.getText().toString());
            }
        });

        //Launch Create Account Activity
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock = true;
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                intent.putExtra("inviteCarpoolID",inviteCarpoolId);
                startActivity(intent);
            }
        });


    }
public void handleDeepLink(Intent intent)
{
    //Bring new user to carpool app located to Login activity first.
    FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
        @Override
        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
            Log.i("LoginActivity", "Nice one");

            //Get deep Link from result (may be null if no link is found).
            Uri deepLink = null;
            if (pendingDynamicLinkData != null) {
                deepLink = pendingDynamicLinkData.getLink();
                inviteCarpoolId = pendingDynamicLinkData.getLink().getQueryParameter("carpoolID");
            }
        }
    }).addOnFailureListener(this, new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.w("LoginActivity", "getDynamicLink:onFailure", e);
        }
    });
}
    @Override
    public void onStart() {
        super.onStart();
        // for testing sign out logged in user
         //FirebaseAuth.getInstance().signOut();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        hideProgressDialog();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, ":failure", task.getException());
            }
        }
    }


    //Login with Google Function
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();
        lock = true;

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);

                            //create a new user in the database

                            User newUser = new User(task.getResult().getUser().getUid().toString(),
                                    task.getResult().getAdditionalUserInfo().getProfile().values().toArray()[0].toString(),
                                    task.getResult().getAdditionalUserInfo().getProfile().values().toArray()[2].toString());
                            FireStoreDatbase fsdb = new FireStoreDatbase();
                            fsdb.writeUser(newUser, true,false,inviteCarpoolId,LoginActivity.this);

                           // finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            lock = false;
                        }
                        hideProgressDialog();
                    }
                });
    }

    //Login Function for Regular Email/Password Login
    private void loginUser(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password...", Toast.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //signing in triggers updateUI so we dont need this. (i think)
                            
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    //Fetches Google Sign In Intent
    private void googleSignIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    //Checks if there is a current user
    private void updateUI(FirebaseUser user) {

        if(user != null && lock == false){
            //hideProgressDialog();

            showProgressDialog();
            Refresh r = new Refresh();
            r.launchCarpoolSelect(user.getUid(), appUser,inviteCarpoolId, LoginActivity.this);


        }

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

    private void initializeUI() {
        mEdtEmail = findViewById(R.id.emailEditText);
        mEdtPassword = findViewById(R.id.passwordEditText);
        createAccountBtn = findViewById(R.id.login_create_account_button);
        signInBtn = findViewById(R.id.email_sign_in_button);
    }



}
