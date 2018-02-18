package com.app.efficiclean;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.app.efficiclean.classes.Supervisor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class CleanApproval extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private String roomNumber;
    private Bundle extras;
    private Supervisor supervisor;
    private DatabaseReference mSuperRef;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CheckBox approve;
    private CheckBox disapprove;
    private EditText comments;
    private Button btApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_cleans_approval);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
            roomNumber = extras.getString("roomNumber");
        }

        TextView header = (TextView) findViewById(R.id.tvRoomNumber);
        header.setText("Room: " + roomNumber);

        approve = (CheckBox) findViewById(R.id.cbApprove);
        disapprove = (CheckBox) findViewById(R.id.cbDisapprove);
        comments = (EditText) findViewById(R.id.etComments);

        btApprove = (Button) findViewById(R.id.btCleansApprovalSubmit);
        btApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approve.isChecked()) {
                    approvedSubmit();
                } else if (disapprove.isChecked()) {
                    disapprovedSubmit();
                } else {
                    Toast.makeText(CleanApproval.this, "You haven't selected an option. Please check one of the boxes and try again.",
                            Toast.LENGTH_LONG).show();
                }
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void approvedSubmit() {

    }

    public void disapprovedSubmit() {

    }
}
