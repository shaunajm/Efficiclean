package com.app.efficiclean;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GuestHome extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef;
    private Guest guest;
    private String hotelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            guest = (Guest) extras.getSerializable("thisGuest");
        }

        //Get instance of Firebase database
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //Create Firebase authenticator
        mAuth = FirebaseAuth.getInstance();

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

    public void pleaseServiceButtonClick(View view) {
        mRootRef.child(hotelID).child("jobs").push().setValue(guest);
        Toast.makeText(GuestHome.this, "Thank you! Your room has been added to the queue and will be serviced shortly.",
                Toast.LENGTH_LONG).show();
    }

    public void doNotDisturbButtonClick(View view) {
        Toast.makeText(GuestHome.this, "Your room has been marked 'Do not disturb'. If you would like your room to be cleaned, click 'Please service my room'.",
                Toast.LENGTH_LONG).show();
    }

    public void checkingOutButtonClick(View view) {
        Toast.makeText(GuestHome.this, "Thank you for using EfficiClean! We hope you enjoyed your stay.",
                Toast.LENGTH_LONG).show();
    }
}
