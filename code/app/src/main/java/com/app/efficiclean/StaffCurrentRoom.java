package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.app.efficiclean.classes.Approval;
import com.app.efficiclean.classes.Housekeeper;
import com.app.efficiclean.classes.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class StaffCurrentRoom extends AppCompatActivity {

    private Button btMarkClean;
    private Button btReportHazard;
    private Button btReportSevereMess;
    private DatabaseReference mStaffRef;
    private DatabaseReference mSupervisorRef;
    private Housekeeper hKeeper;
    private String staffKey;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String supervisorKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_current_room);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff").child(staffKey);
        mStaffRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hKeeper = dataSnapshot.getValue(Housekeeper.class);
                TextView tvRoom = (TextView) findViewById(R.id.tvCurrentRoom);
                String roomText;

                if (hKeeper.getCurrentJob() == null) {
                    roomText = "You have no current room.";
                } else if (hKeeper.getCurrentJob().getDescription() == null){
                    roomText = "Your current room is: " + hKeeper.getCurrentJob().getRoomNumber();
                } else {
                    roomText = "Room " + hKeeper.getCurrentJob().getRoomNumber() + " feedback: " + hKeeper.getCurrentJob().getDescription();
                }

                tvRoom.setText(roomText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSupervisorRef = FirebaseDatabase.getInstance().getReference(hotelID).child("supervisor");
        mSupervisorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("onDuty").getValue(Boolean.class) == true) {
                        supervisorKey = ds.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btMarkClean = (Button) findViewById(R.id.btMarkClean);
        btMarkClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignToSupervisor();
            }
        });

        btReportHazard = (Button) findViewById(R.id.btReportHazard);
        btReportHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffCurrentRoom.this, ReportHazard.class);
                i.putExtras(extras);
                i.putExtra("supervisorKey", supervisorKey);
                startActivity(i);
                finish();
            }
        });

        btReportSevereMess = (Button) findViewById(R.id.btReportSevereMess);
        btReportSevereMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffCurrentRoom.this, ReportSevereMess.class);
                i.putExtras(extras);
                i.putExtra("supervisorKey", supervisorKey);
                startActivity(i);
                finish();
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

    public void assignToSupervisor() {
        Job job = hKeeper.getCurrentJob();
        Approval approval = new Approval();
        approval.setJob(job);
        approval.setCreatedBy(mAuth.getUid());
        DatabaseReference mRoomRef = FirebaseDatabase.getInstance().getReference(hotelID).child("rooms");
        mRoomRef.child(job.getRoomNumber()).child("status").setValue("Waiting");
        mSupervisorRef.child(supervisorKey).child("approvals").push().setValue(approval);
        mStaffRef.child("status").setValue("Waiting");
        mStaffRef.child("currentJob").removeValue();
        Toast.makeText(StaffCurrentRoom.this, "This room has been marked clean and an approval request has been sent to the supervisor.",
                Toast.LENGTH_LONG).show();
        finish();
    }
}
