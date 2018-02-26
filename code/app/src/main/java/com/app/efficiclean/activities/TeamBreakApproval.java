package com.app.efficiclean.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.NotificationHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class TeamBreakApproval extends AppCompatActivity {

    private Bundle extras;
    private String hotelID;
    private String teamID;
    private String breakKey;
    private String membersText;
    private String timeText;
    private int breakLength;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef;
    private DataSnapshot breaks;
    private DataSnapshot teams;
    private CheckBox approve;
    private CheckBox disapprove;
    private EditText reason;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_approve_team_break);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            teamID = extras.getString("teamID");
            breakKey = extras.getString("breakKey");
            membersText = extras.getString("membersText");
            timeText = extras.getString("timeText");
            breakLength = extras.getInt("breakLength");
        }

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        TextView header = (TextView) findViewById(R.id.tvTeam);
        header.setText("Team: " + membersText);

        TextView time = (TextView) findViewById(R.id.tvTime);
        time.setText("Time: " + timeText);

        TextView length = (TextView) findViewById(R.id.tvLength);
        length.setText("Length: " + breakLength + " minutes");

        approve = (CheckBox) findViewById(R.id.cbApprove);
        disapprove = (CheckBox) findViewById(R.id.cbDisapprove);
        reason = (EditText) findViewById(R.id.etReason);

        submit = (Button) findViewById(R.id.btTeamBreakSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approve.isChecked()) {
                    approvedSubmit();
                } else if (disapprove.isChecked()) {
                    disapprovedSubmit();
                } else {
                    Toast.makeText(TeamBreakApproval.this, "You haven't selected an option. Please check one of the boxes and try again.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams = dataSnapshot.child("teams");
                breaks = dataSnapshot.child("breakRequests");
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void approvedSubmit() {
        mRootRef.child("breakRequests").child(breakKey).child("accepted").setValue(true);
        for (DataSnapshot member : teams.child(teamID).child("members").getChildren()) {
            String staffKey = member.getValue(String.class);
            NotificationHandler.sendNotification(hotelID,
                                staffKey,
                                "Your requested break starting at " +
                                        timeText +
                                        "has been accepted.");
        }
        Intent i = new Intent(TeamBreakApproval.this, SupervisorHome.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public void disapprovedSubmit() {
        mRootRef.child("breakRequests").child(breakKey).removeValue();
        for (DataSnapshot member : teams.child(teamID).child("members").getChildren()) {
            String staffKey = member.getValue(String.class);
            NotificationHandler.sendNotification(hotelID,
                    staffKey,
                    "Your requested break starting at " +
                            timeText +
                            "has been denied. Reason: " +
                            reason.getText().toString());
        }
        finish();
    }
}

