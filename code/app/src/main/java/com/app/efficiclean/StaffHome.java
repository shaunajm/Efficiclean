package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.efficiclean.classes.Housekeeper;
import com.app.efficiclean.classes.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class StaffHome extends AppCompatActivity {

    private Button btRequestBreak;
    private Button btCurrentRoom;
    private Button btViewMap;
    private Housekeeper hKeeper;
    private String staffKey;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mStaffRef;
    private DatabaseReference mJobRef;
    private DataSnapshot jobs;
    private DataSnapshot staff;
    private TableLayout tb1;
    private TableLayout tb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

        tb1 = (TableLayout) findViewById(R.id.tbTeams);
        tb2 = (TableLayout) findViewById(R.id.tbQueue);

        btViewMap = (Button) findViewById(R.id.btViewMap);
        btViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, MapView.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        btRequestBreak = (Button) findViewById(R.id.btRequestBreak);
        btRequestBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, StaffRequestBreak.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        btCurrentRoom = (Button) findViewById(R.id.btCurrentRoom);
        btCurrentRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, StaffCurrentRoom.class);
                i.putExtras(extras);
                startActivity(i);
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

        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff");
        mStaffRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staff = dataSnapshot;
                hKeeper = dataSnapshot.child(staffKey).getValue(Housekeeper.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mJobRef = FirebaseDatabase.getInstance().getReference(hotelID).child("jobs");
        mJobRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobs = dataSnapshot;
                setQueue();
                if (tb1 != null) {
                    setTeams();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    @Override
    public void onBackPressed() {
        mAuth.signOut();
        finish();
        Intent i = new Intent(StaffHome.this, StaffLogin.class);
        startActivity(i);
    }

    public void todaysTeamsButtonClick(View v) {
        Intent i = new Intent(StaffHome.this, TodaysTeams.class);
        i.putExtras(extras);
        startActivity(i);
    }

    public void setTeams() {
        TextView template = (TextView) findViewById(R.id.tvTeamsRow1);

        tb1.removeViews(1, tb1.getChildCount() - 1);
        for(DataSnapshot ds : jobs.getChildren()) {
            Job job = ds.getValue(Job.class);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView roomNumber = new TextView(this);
            roomNumber.setText(job.getRoomNumber());
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
            tb1.addView(tr);

        }
    }

    public void setQueue() {
        TextView template = (TextView) findViewById(R.id.tvQueueRow1);

        tb2.removeViews(1, tb2.getChildCount() - 1);
        for(DataSnapshot ds : staff.getChildren()) {
            Housekeeper hs = ds.getValue(Housekeeper.class);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView roomNumber = new TextView(this);
            roomNumber.setText(hs.getUsername());
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
            tb2.addView(tr);
        }
    }
}
