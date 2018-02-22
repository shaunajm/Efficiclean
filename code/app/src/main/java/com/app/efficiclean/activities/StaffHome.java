package com.app.efficiclean.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Team;
import com.app.efficiclean.classes.Job;
import com.app.efficiclean.classes.QueueHandler;
import com.app.efficiclean.classes.QueueHandlerCreater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class StaffHome extends AppCompatActivity {

    private Button btRequestBreak;
    private Button btCurrentRoom;
    private Button btViewMap;
    private String staffKey;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mTeamRef;
    private DatabaseReference mJobRef;
    private DatabaseReference mStaffRef;
    private DataSnapshot staff;
    private DataSnapshot jobs;
    private DataSnapshot teams;
    private TableLayout tb1;
    private TableLayout tb2;
    private QueueHandler qHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.efficiclean.R.layout.activity_staff_home);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

        tb1 = (TableLayout) findViewById(com.app.efficiclean.R.id.tbTeams);
        tb2 = (TableLayout) findViewById(com.app.efficiclean.R.id.tbQueue);

        btViewMap = (Button) findViewById(com.app.efficiclean.R.id.btViewMap);
        btViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, MapView.class);
                i.putExtras(extras);
                startActivity(i);
                onStop();
            }
        });

        btRequestBreak = (Button) findViewById(com.app.efficiclean.R.id.btRequestBreak);
        btRequestBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, StaffRequestBreak.class);
                i.putExtras(extras);
                startActivity(i);
                onStop();
            }
        });

        btCurrentRoom = (Button) findViewById(com.app.efficiclean.R.id.btCurrentRoom);
        btCurrentRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, StaffCurrentRoom.class);
                i.putExtras(extras);
                startActivity(i);
                onStop();
            }
        });

        qHandler = QueueHandlerCreater.createHandler(hotelID);

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
        mStaffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staff = dataSnapshot;
                getTeams();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mJobRef = FirebaseDatabase.getInstance().getReference(hotelID).child("jobs");
        mJobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobs = dataSnapshot;
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
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    public void todaysTeamsButtonClick(View v) {
        Intent i = new Intent(StaffHome.this, TodaysTeams.class);
        i.putExtras(extras);
        startActivity(i);
        onStop();
    }

    public void getTeams() {
        mTeamRef = FirebaseDatabase.getInstance().getReference(hotelID).child("teams");
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams = dataSnapshot;
                setTeams();
                if (tb1 != null) {
                    setQueue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setQueue() {
        TextView template = (TextView) findViewById(com.app.efficiclean.R.id.tvTeamsRow1);

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
            roomNumber.setBackground(template.getBackground());
            roomNumber.setGravity(template.getGravity());

            tr.addView(roomNumber);
            tb1.addView(tr);

        }
    }

    public void setTeams() {
        TextView template = (TextView) findViewById(R.id.tvQueueRow1);

        tb2.removeViews(1, tb2.getChildCount() - 1);
        for(DataSnapshot ds : teams.getChildren()) {
            Team team = ds.getValue(Team.class);
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            String text = "";

            for (String staffKey : team.getMembers()) {
                if (text.equals("")) {
                    text += staff.child(staffKey).child("username").getValue(String.class);
                } else {
                    text += " & " + staff.child(staffKey).child("username").getValue(String.class);
                }
            }

            TextView roomNumber = new TextView(this);
            roomNumber.setText(text);
            roomNumber.setTextSize(template.getTextSize() / 2);
            roomNumber.setWidth(template.getWidth());
            roomNumber.setHeight(template.getHeight());
            roomNumber.setPadding(
                    template.getPaddingLeft(),
                    template.getPaddingTop() - 5,
                    template.getPaddingRight(),
                    template.getPaddingBottom());
            roomNumber.setBackground(template.getBackground());
            roomNumber.setGravity(template.getGravity());

            tr.addView(roomNumber);
            tb2.addView(tr);
        }
    }
}
