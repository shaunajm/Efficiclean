package com.app.efficiclean;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.efficiclean.classes.HazardApproval;
import com.app.efficiclean.classes.Housekeeper;
import com.app.efficiclean.classes.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ReportHazard extends AppCompatActivity {

    private EditText description;
    private Button reportHazard;
    private DatabaseReference mStaffRef;
    private DatabaseReference mSupervisorRef;
    private Housekeeper hKeeper;
    private String staffKey;
    private String hotelID;
    private String supervisorKey;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_report_hazard);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

        supervisorKey = getIntent().getStringExtra("supervisorKey");

        description = (EditText) findViewById(R.id.etDescription);

        reportHazard = (Button) findViewById(R.id.btHazardSubmit);
        reportHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignToSupervisor();
            }
        });

        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff").child(staffKey);
        mStaffRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hKeeper = dataSnapshot.getValue(Housekeeper.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSupervisorRef = FirebaseDatabase.getInstance().getReference(hotelID).child("supervisor");

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
        HazardApproval approval = new HazardApproval();
        approval.setJob(job);
        approval.setCreatedBy(mAuth.getUid());
        approval.setDescription(description.getText().toString());
        DatabaseReference mRoomRef = FirebaseDatabase.getInstance().getReference(hotelID).child("rooms");
        mRoomRef.child(job.getRoomNumber()).child("status").setValue("Waiting");
        mSupervisorRef.child(supervisorKey).child("approvals").push().setValue(approval);
        mStaffRef.child("status").setValue("Waiting");
        mStaffRef.child("currentJob").removeValue();
        Toast.makeText(ReportHazard.this, "This room has been marked hazardous and an approval request has been sent to the supervisor.",
                Toast.LENGTH_LONG).show();
    }
}

