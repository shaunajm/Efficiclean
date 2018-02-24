package com.app.efficiclean.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class TodaysTeams extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private Bundle extras;
    private DataSnapshot staff;
    private DataSnapshot teams;
    private DatabaseReference mTeamRef;
    private DatabaseReference mStaffRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_todays_teams);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
        }

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

    public void getTeams() {
        mTeamRef = FirebaseDatabase.getInstance().getReference(hotelID).child("teams");
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams = dataSnapshot;
                setTeams();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setTeams() {
        TableLayout table = (TableLayout) findViewById(R.id.tbTodaysTeams);
        TextView template = (TextView) findViewById(R.id.tvTeamRow);

        table.removeViews(1, table.getChildCount() - 1);
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
            roomNumber.setMinHeight(template.getHeight());
            roomNumber.setPadding(
                    template.getPaddingLeft(),
                    template.getPaddingTop() - 5,
                    template.getPaddingRight(),
                    template.getPaddingBottom());
            roomNumber.setBackground(template.getBackground());
            roomNumber.setGravity(template.getGravity());

            tr.addView(roomNumber);
            table.addView(tr);
        }
    }

}

