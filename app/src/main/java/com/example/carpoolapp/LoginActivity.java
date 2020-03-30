package com.example.carpoolapp;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdtEmail = findViewById(R.id.emailEditText);
        mEdtPassword = findViewById(R.id.passwordEditText);
        signInBtn = findViewById(R.id.email_sign_in_button);
        createAccountBtn = findViewById(R.id.login_create_account_button);

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
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // for testing sign out logged in user
        // FirebaseAuth.getInstance().signOut();
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

            }
        }
    }


    //Login with Google Function
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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

        if(user != null){
            hideProgressDialog();
           // startActivity(new Intent(this, MainActivity.class));

            //Starts main activity if there is a current user
            //launches carpool select if there is a user
            launchCarpoolSelect(user);

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

    private void launchCarpoolSelect(FirebaseUser user) {
//        //Clear users carpools and add one (for testing)
//        FireStoreDatbase fsd =new FireStoreDatbase();
//        User use = new User(user.getUid(),"Shane","s");
//        fsd.createCarpool(use);

        getCurentUser(user.getUid(), new getCurentUserCallback() {
            @Override
            public void onCallBack(User user) {
                //create a User object from the FirebaseUser
                appUser = new User(user.id, user.firstName, user.lastName);



                final ArrayList<ArrayList<Object>> totalUserList = new ArrayList<>();

                getUsersCarpoolList(appUser.id, new FirestoreCallback() {
                    @Override
                    public void OnCallback(ArrayList<User> userList) { //userList is a list of the logged in user (list always has length of one)
                        Log.d(TAG, "complete");


                        final int carpoolListLength = userList.get(0).carPools.size() - 1;
                        //if no carpools
                        if(carpoolListLength ==-1)
                        {
                            Intent intent = new Intent(LoginActivity.this, CarpoolSelectActivity.class);
                            intent.putExtra("Carpools", (Serializable) totalUserList);
                            intent.putExtra("user", (Serializable) appUser);
                            LoginActivity.this.startActivity(intent);
                        }
                        for (int i = 0; i <= carpoolListLength; i++) {
                            getUsersInCarpool(carpoolListLength, userList.get(0).carPools.get(i), new FirestoreCallback() {
                                @Override
                                public void OnCallback(ArrayList<User> userList) {

                                }

                                @Override
                                public void OnCallbackTotalCarpoolList(ArrayList<ArrayList<Object>> totalCarpoolList) {

                                    totalUserList.add(totalCarpoolList.get(0));
                                    //all the carpools are in and we can send it back to the listener
                                    if (totalUserList.size() == carpoolListLength + 1) {
                                        Intent intent = new Intent(LoginActivity.this, CarpoolSelectActivity.class);
                                        intent.putExtra("Carpools", (Serializable) totalUserList);
                                        intent.putExtra("user", (Serializable) appUser);
                                        LoginActivity.this.startActivity(intent);
                                    }

                                }
                            });
                        }
                    }

                    @Override
                    public void OnCallbackTotalCarpoolList(ArrayList<ArrayList<Object>> totalCarpoolList) {

                    }
                });
            }
        });






    }

    private void getUsersInCarpool(final int carpoolListLength, String carpoolID, final FirestoreCallback fireCallBack) {
        //get database instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //list containing a carpool with users in them
        final ArrayList<ArrayList<Object>> totalUserList = new ArrayList<>();

        db.collection("CarPools").document(carpoolID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map userIDMap = documentSnapshot.getData();
                        //this is a list of users in the carpool with the carpoolID as index 0
                        ArrayList<String> userIDs = new ArrayList<String>(userIDMap.values());

                        //get the actual user objects
                        //---
                        final ArrayList<Object> userList = new ArrayList<Object>();

                        //adds carpool info (users id and carpools id)
                        Map<String, Object> info = new HashMap<>();
                        info.put("userID",appUser.id);
                        info.put("carpoolID",userIDs.get(0));
                        userList.add(info);

                        final int userIdListLength = userIDs.size();
                        // the first item is the carpoolID not a userID but we still ned it
                        for (int index = 1; index < userIdListLength; index++) {


                            //get the users in the carpool
                            db.collection("CarPools").document(userIDs.get(0)).collection(userIDs.get(index)).document(userIDs.get(index))
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User myuser = documentSnapshot.toObject(User.class);

                                            userList.add(myuser);


                                            int stopint = 1;
                                            //used in carpool select to get all users from all of the appUsers carpools eg. Sam belongs to 3 carpools with 4 people in each.
                                            // Get a list with 3 elements containing 4 User objects each
                                            if (userList.size() == userIdListLength) {
                                                totalUserList.add(userList);
                                                fireCallBack.OnCallbackTotalCarpoolList(totalUserList);
                                            }


                                        }

                                    }
                                    ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    int stopint =1;
                                }
                            });
                        }
                        //---
                    }
                });
    }

    private void getUsersCarpoolList(final String userId, final FirestoreCallback fireCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<User> itemList = new ArrayList<>();

        final User sendUser;
        showProgressDialog();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                              User myuser = documentSnapshot.toObject(User.class);
                                              itemList.add(myuser);
                                              // carPools = myuser.carPools;
                                              // continueBool = true;
                                              fireCallback.OnCallback(itemList);


                                              // LoginActivity.startCarpoolSelect()
                                              int stopint = 1;
                                          }


                                      }
                );
    }

    private interface FirestoreCallback {
        void OnCallback(ArrayList<User> userList);

        void OnCallbackTotalCarpoolList(ArrayList<ArrayList<Object>> totalCarpoolList);


    }

    private void getCurentUser(String id, getCurentUserCallback cb)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User myuser = documentSnapshot.toObject(User.class);
                        cb.onCallBack(myuser);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    private interface getCurentUserCallback
    {
        void onCallBack(User user);
    }

}
