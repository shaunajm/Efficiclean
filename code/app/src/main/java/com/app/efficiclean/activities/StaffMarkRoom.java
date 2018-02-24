package com.app.efficiclean.activities;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.app.efficiclean.R;
import com.app.efficiclean.classes.Job;
import com.app.efficiclean.classes.QueueHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StaffMarkRoom extends AppCompatActivity {

    private String staffKey;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef;
    private DatabaseReference mRoomRef;
    private DatabaseReference mJobRef;
    private RadioGroup status;
    private EditText roomNumber;
    private Button markRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_markroom);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

        roomNumber = (EditText) findViewById(R.id.etRoomNumber);
        status = (RadioGroup) findViewById(R.id.rgStatus);

        markRoom = (Button) findViewById(R.id.btMarkRoomSubmit);
        markRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);
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
        if (roomStatus.equals("Do not disturb")) {
            mRoomRef.child(rNumber).child("status").setValue("Do Not Disturb");
        } else {
            Job job = new Job();
            job.setRoomNumber(rNumber);
            job.setPriority(0);
            mRoomRef.child(rNumber).child("status").setValue("To Be Cleaned");
            mJobRef.push().setValue(job);
        }
    }
}
