package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
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
        Job newJob = new Job(mAuth.getUid(), guest.getRoomNumber(), 0);
        mRootRef.child(hotelID).child("jobs").push().setValue(newJob);
        changePage("service");
    }

    public void doNotDisturbButtonClick(View view) {
        changePage("notDisturb");
    }

    public void checkingOutButtonClick(View view) {
        changePage("checkingOut");
    }

    public void changePage(String choice) {
        Bundle bundle = new Bundle();
        bundle.putString("hotelID", hotelID);
        bundle.putSerializable("thisGuest", guest);

        Intent guestChoicePage = null;

        if (choice.equals("service")) {
            guestChoicePage = new Intent(GuestHome.this, GuestPleaseService.class);
        } else if (choice.equals("notDisturb")) {
            guestChoicePage = new Intent(GuestHome.this, GuestDoNotDisturb.class);
        } else if (choice.equals("checkingOut")) {
            guestChoicePage = new Intent(GuestHome.this, GuestCheckingOut.class);
        }

        if (guestChoicePage != null) {
            guestChoicePage.putExtras(bundle);
            startActivity(guestChoicePage);
            finish();
        } else {
            Toast.makeText(GuestHome.this, "An error has occurred. Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
