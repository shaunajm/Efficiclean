package com.app.efficiclean.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class StaffMarkRoom extends AppCompatActivity {

    private String staffKey;
    private String hotelID;
    private String teamKey;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef;
    private DatabaseReference mRoomRef;
    private DatabaseReference mJobRef;
    private RadioGroup status;
    private EditText roomNumber;
    private Button markRoom;
    private Button finishCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_markroom);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

        roomNumber = (EditText) findViewById(R.id.etRoomNumber);
        status = (RadioGroup) findViewById(R.id.rgStatus);

        markRoom = (Button) findViewById(R.id.btMarkRoom);
        markRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });

        finishCheck = (Button) findViewById(R.id.btFinishCheck);
        finishCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCheck();
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teamKey = dataSnapshot.child("staff").child(staffKey).child("teamID").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRoomRef = mRootRef.child("rooms");
        mJobRef = mRootRef.child("jobs");

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

    public void addJob() {
        RadioButton rb = (RadioButton) findViewById(status.getCheckedRadioButtonId());
        String roomStatus = rb.getText().toString();
        String rNumber = roomNumber.getText().toString();
        if (roomStatus.equals("Do not Disturb")) {
            mRoomRef.child(rNumber).child("status").setValue("Do Not Disturb");
        } else {
            Job job = new Job();
            job.setRoomNumber(rNumber);
            job.setPriority(0);
            mRoomRef.child(rNumber).child("status").setValue("To Be Cleaned");
            mJobRef.push().setValue(job);
        }

        Intent i = new Intent(StaffMarkRoom.this, StaffHome.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public void finishCheck() {
        mRootRef.child("teams").child(teamKey).child("status").setValue("Waiting");

        Intent i = new Intent(StaffMarkRoom.this, StaffHome.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }
}
