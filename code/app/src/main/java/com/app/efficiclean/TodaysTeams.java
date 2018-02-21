package com.app.efficiclean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.efficiclean.classes.Approval;
import com.app.efficiclean.classes.Supervisor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TodaysTeams extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private Bundle extras;
    private Supervisor supervisor;
    private DatabaseReference mSuperRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_todays_teams);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
        }

        mSuperRef = FirebaseDatabase.getInstance().getReference(hotelID).child("supervisor").child(supervisorKey);
        mSuperRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supervisor = dataSnapshot.getValue(Supervisor.class);
                setRoomApprovals();
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

    public void setRoomApprovals(){
        TableLayout table = (TableLayout) findViewById(R.id.tbToBeApproved);
        TextView template = (TextView) findViewById(R.id.tvRow1);

        table.removeViews(1, table.getChildCount() - 1);
        for(Approval approval : supervisor.approvals.values()) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView roomNumber = new TextView(this);
            roomNumber.setText(approval.getJob().getRoomNumber());
            roomNumber.setTextSize(template.getTextSize() / 2);
            roomNumber.setWidth(template.getWidth());
            roomNumber.setHeight(template.getHeight());
            roomNumber.setPadding(
                    template.getPaddingLeft(),
                    template.getPaddingTop() - 5,
                    template.getPaddingRight(),
                    template.getPaddingBottom());
            roomNumber.setGravity(template.getGravity());

            tr.addView(roomNumber);
            table.addView(tr);

        }
    }

}

