package com.app.efficiclean.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Guest;
import com.app.efficiclean.classes.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GuestHome extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef;
    private Guest guest;
    private String guestKey;
    private String hotelID;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            guest = (Guest) extras.getSerializable("thisGuest");
        }

        //Get instance of Firebase database
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //Create Firebase authenticator
        mAuth = FirebaseAuth.getInstance();
        guestKey = mAuth.getUid();
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

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    public void pleaseServiceButtonClick(View view) {
        Job newJob = new Job();
        newJob.setCreatedBy(guestKey);
        newJob.setRoomNumber(guest.getRoomNumber());
        newJob.setPriority(0);
        mRootRef.child(hotelID).child("jobs").push().setValue(newJob);
        mRootRef.child(hotelID).child("rooms").child(guest.getRoomNumber()).child("status").setValue("To Be Cleaned");
        changePage("service");
    }

    public void doNotDisturbButtonClick(View view) {
        mRootRef.child(hotelID).child("rooms").child(guest.getRoomNumber()).child("status").setValue("Do Not Disturb");
        changePage("notDisturb");
    }

    public void checkingOutButtonClick(View view) {
        changePage("checkingOut");
    }

    public void changePage(String choice) {
        Intent guestChoicePage = null;

        if (choice.equals("service")) {
            guestChoicePage = new Intent(GuestHome.this, GuestPleaseService.class);
        } else if (choice.equals("notDisturb")) {
            guestChoicePage = new Intent(GuestHome.this, GuestDoNotDisturb.class);
        } else if (choice.equals("checkingOut")) {
            guestChoicePage = new Intent(GuestHome.this, GuestCheckingOut.class);
        }

        if (guestChoicePage != null) {
            guestChoicePage.putExtras(extras);
            startActivity(guestChoicePage);
            onStop();
        } else {
            Toast.makeText(GuestHome.this, "An error has occurred. Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
