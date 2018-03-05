package com.app.efficiclean.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Guest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;

public class GuestPleaseService extends AppCompatActivity {

    private final int THIRTY_MINUTE = 1800000;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Guest guest;
    private String hotelID;
    private Bundle extras;
    private DatabaseReference mRootRef;
    private DataSnapshot guestRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_please_service);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            guest = (Guest) extras.getSerializable("thisGuest");
        }

        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double jobCount = (double) dataSnapshot.child("jobs").getChildrenCount();
                double teamCount = (double) dataSnapshot.child("teams").getChildrenCount();
                guestRoom = dataSnapshot.child("rooms").child(guest.getRoomNumber());
                if (guestRoom.hasChild("cleanTime")) {
                    int hour = guestRoom.child("cleanTime").child("hours").getValue(int.class);
                    int minutes = guestRoom.child("cleanTime").child("minutes").getValue(int.class);
                    displayTime(hour, minutes);
                } else {
                    getEstimatedTime(jobCount, teamCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void getEstimatedTime(double jobCount, double teamCount) {
        double timeRatio = 1 + (jobCount / teamCount);
        Calendar currentTime = Calendar.getInstance();

        Date time = new Date(Math.round(currentTime.getTimeInMillis() + (timeRatio * THIRTY_MINUTE)));
        mRootRef.child("rooms").child(guest.getRoomNumber()).child("cleanTime").setValue(time);

        int hour = time.getHours();
        int minute = time.getMinutes();

        displayTime(hour, minute);
    }

    public void displayTime(int hours, int minutes) {
        String hourString = Integer.toString(hours);
        String minuteString = Integer.toString(minutes);

        if (hourString.length() == 1) {
            hourString = "0" + hourString;
        }

        if (minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }

        String timeString = hourString + ":" + minuteString;
        TextView timeView = (TextView) findViewById(R.id.tvEstimate);
        timeView.setText("We estimate that your room will be serviced by: " + timeString);
    }
}

