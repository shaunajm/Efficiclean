package com.app.efficiclean.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Job;
import com.app.efficiclean.classes.NotificationHandler;
import com.app.efficiclean.classes.Supervisor;
import com.app.efficiclean.classes.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ServiceApproval extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private String roomNumber;
    private String approvalKey;
    private String oneSignalKey;
    private Bundle extras;
    private Supervisor supervisor;
    private DatabaseReference mSuperRef;
    private DatabaseReference mRootRef;
    private DatabaseReference mAppRef;
    private DatabaseReference mTeamRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CheckBox approve;
    private CheckBox disapprove;
    private EditText comments;
    private Button btApprove;
    private int cleans;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.efficiclean.R.layout.activity_supervisor_cleans_approval);

        //Display back button in navbar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Set screen orientation based on layout
        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Extract variables from intent bundle
        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
            roomNumber = extras.getString("roomNumber");
            approvalKey = extras.getString("approvalKey");
        }

        //Display relevant room number and description
        TextView header = (TextView) findViewById(com.app.efficiclean.R.id.tvRoomNumber);
        header.setText("Room: " + roomNumber);

        //Reference display file check boxes
        approve = (CheckBox) findViewById(com.app.efficiclean.R.id.cbApprove);
        disapprove = (CheckBox) findViewById(com.app.efficiclean.R.id.cbDisapprove);

        //Add toggle functionality to block both check boxes being selected
        approve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (approve.isChecked()) {
                    disapprove.setChecked(false);
                }
            }
        });

        disapprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (disapprove.isChecked()) {
                    approve.setChecked(false);
                }
            }
        });

        //Reference other elements from activity layouts
        comments = (EditText) findViewById(com.app.efficiclean.R.id.etComments);

        //Add click listener to submit button
        btApprove = (Button) findViewById(R.id.btCleansApprovalSubmit);
        btApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String team = job.getAssignedTo();
                mTeamRef = mRootRef.child("teams").child(team);
                mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cleans = dataSnapshot.child("cleanCounter").getValue(int.class);
                        cleans++;
                        if (approve.isChecked()) {
                            approvedSubmit();
                        } else if (disapprove.isChecked()) {
                            disapprovedSubmit();
                        } else {
                            Toast.makeText(ServiceApproval.this, "You haven't selected an option. Please check one of the boxes and try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Reference to root of current hotel
        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);

        //Add listener to get current supervisor values
        mSuperRef = FirebaseDatabase.getInstance().getReference(hotelID).child("supervisor").child(supervisorKey);
        mSuperRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supervisor = dataSnapshot.getValue(Supervisor.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Add listener to get datasnapshot of selected approval request
        mAppRef = mSuperRef.child("approvals").child(approvalKey);
        mAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                job = dataSnapshot.child("job").getValue(Job.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Create Firebase authenticator
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ServiceApproval.this, SupervisorApprovals.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public void approvedSubmit() {
        //Get Firebase values for team
        mTeamRef.child("cleanCounter").setValue(cleans);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("Completed");
        mSuperRef.child("approvals").child(approvalKey).removeValue();

        String uid = supervisor.approvals.get(approvalKey).getJob().getCreatedBy();

        if (uid != null) {
            NotificationHandler.sendNotification(hotelID, uid, "Your room has been serviced. Thank you for using Efficiclean!");
        }

        Intent i = new Intent(ServiceApproval.this, SupervisorHome.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public void disapprovedSubmit() {

        //Get Firebase values for team

        job.setDescription(comments.getText().toString());
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                for (String staffKey : team.getMembers()) {
                    NotificationHandler.sendNotification(hotelID, staffKey, "Cleaning job for room number " + job.getRoomNumber() + " has been returned with a description.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Update values in Database
        mTeamRef.child("returnedJob").setValue(job);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("In Progress");
        mAppRef.removeValue();

        Intent i = new Intent(ServiceApproval.this, SupervisorHome.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }
}
