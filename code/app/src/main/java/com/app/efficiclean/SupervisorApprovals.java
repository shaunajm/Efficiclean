package com.app.efficiclean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SupervisorApprovals extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_approvals);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth fbAuth) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Add authentication listener
        mAuth.addAuthStateListener(mAuthListener);
    }
}

