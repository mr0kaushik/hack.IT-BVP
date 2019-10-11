package com.mr0kaushik.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mr0kaushik.hackathon.Model.User;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private static final int DEFAULT_REWARD_POINT = 50;

    private static final int RC_GOOGLE_SIGN_IN = 100;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.btnSignGoogle);
        signInButton.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        nextPage(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignGoogle:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    fireBaseAuthWithGoogle(account);
                } else{
                    showSnackBar("Not able to login, please try again later!");
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                // ...
                showSnackBar("Not able to login, please try again later!");
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "fireBaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
                            sharedPrefManager.setUserID(user.getUid());
                            setUserData(user);


                            Log.d(TAG, "onComplete: getid " + user.getProviderId());
                            nextPage(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showSnackBar("Authentication Failed!");
                        }
                    }
                });

    }

    private void setUserData(FirebaseUser firebaseUser) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        User user = new User(firebaseUser.getEmail(),
                firebaseUser.getDisplayName(),
                DEFAULT_REWARD_POINT,
                0, 0, 0, "default");

        reference.child("Users").child(firebaseUser.getUid()).setValue(user);
    }


    public void showSnackBar(String msg){
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    private void nextPage(FirebaseUser account) {
        if(account != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
