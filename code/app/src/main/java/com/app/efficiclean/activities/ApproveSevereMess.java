package com.app.efficiclean.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.*;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Job;
import com.app.efficiclean.classes.NotificationHandler;
import com.app.efficiclean.classes.Supervisor;
import com.app.efficiclean.classes.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ApproveSevereMess extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private String roomNumber;
    private String approvalKey;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mSuperRef;
    private DatabaseReference mRootRef;
    private DatabaseReference mAppRef;
    private DatabaseReference mTeamRef;
    private Supervisor supervisor;
    private CheckBox approve;
    private CheckBox disapprove;
    private TextView description;
    private EditText comments;
    private Button btApprove;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.efficiclean.R.layout.activity_supervisor_severe_mess_approval);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
            roomNumber = extras.getString("roomNumber");
            approvalKey = extras.getString("approvalKey");
        }

        TextView header = (TextView) findViewById(com.app.efficiclean.R.id.tvRoomNumber);
        header.setText("Room " + roomNumber + " description:\n");

        approve = (CheckBox) findViewById(com.app.efficiclean.R.id.cbApprove);
        disapprove = (CheckBox) findViewById(com.app.efficiclean.R.id.cbDisapprove);
        comments = (EditText) findViewById(com.app.efficiclean.R.id.etReason);
        description = (TextView) findViewById(com.app.efficiclean.R.id.tvDescriptionBox);

        btApprove = (Button) findViewById(R.id.btSevereMessApprovalSubmit);
        btApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String team = job.getAssignedTo();
                mTeamRef = mRootRef.child("teams").child(team);
                mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (approve.isChecked()) {
                            approvedSubmit();
                        } else if (disapprove.isChecked()) {
                            disapprovedSubmit();
                        } else {
                            Toast.makeText(ApproveSevereMess.this, "You haven't selected an option. Please check one of the boxes and try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);

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

        mAppRef = mSuperRef.child("approvals").child(approvalKey);
        mAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                job = dataSnapshot.child("job").getValue(Job.class);
                String info = dataSnapshot.child("description").getValue(String.class);
                description.setText(info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                for (String staffKey : team.getMembers()) {
                    NotificationHandler.sendNotification(hotelID, staffKey,
                            "Severe mess approval for room number "
                            + job.getRoomNumber() +
                            " has been accepted. The job has been returned and your priority updated on the queue.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTeamRef.child("returnedJob").setValue(job);
        mTeamRef.child("priorityCounter").setValue(1);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("In Progress");
        mAppRef.removeValue();
        finish();
    }

    public void disapprovedSubmit() {
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Team team = dataSnapshot.getValue(Team.class);
                for (String staffKey : team.getMembers()) {
                    NotificationHandler.sendNotification(hotelID, staffKey,
                            "Severe mess approval for room number "
                            + job.getRoomNumber() +
                            " has been denied and returned with a description.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        job.setDescription(comments.getText().toString());
        mTeamRef.child("returnedJob").setValue(job);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("In Progress");
        mAppRef.removeValue();
        finish();
    }
}
