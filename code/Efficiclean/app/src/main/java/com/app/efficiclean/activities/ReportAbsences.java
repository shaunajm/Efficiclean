package com.app.efficiclean.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Housekeeper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class ReportAbsences extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String hotelID;
    private Bundle extras;
    private String supervisorKey;
    private DatabaseReference mStaffRef;
    private DataSnapshot staff;
    private RadioGroup names;
    private HashMap<String, String> keys;
    private Button markAbsent;
    private String teamID;
    private DatabaseReference mTeamRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_report_absence);
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

        names = (RadioGroup) findViewById(R.id.rgStaffList);

        markAbsent = (Button) findViewById(R.id.btReportAbsenceSubmit);
        markAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (names.getCheckedRadioButtonId() != 0) {
                    reportAbsence();
                } else {
                    Toast.makeText(ReportAbsences.this, "Please select a staff member to mark absent.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff");
        mStaffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staff = dataSnapshot;
                populateGroup();
                keys = populateHashMap();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ReportAbsences.this, SupervisorHome.class);
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    public void populateGroup(){
        names.removeAllViews();
        for (DataSnapshot ds : staff.getChildren()) {
            Housekeeper hs = ds.getValue(Housekeeper.class);
            String username = hs.getUsername();

            if (hs.getTeamID().equals("Absent") == false) {
                RadioButton r1 = new RadioButton(this);
                r1.setText(username);
                r1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                r1.setBackgroundResource(R.drawable.cell_border);
                r1.setLayoutParams(names.getLayoutParams());
                names.addView(r1);
            }
        }
    }

    public HashMap<String, String> populateHashMap(){
        HashMap<String, String> map = new HashMap<String, String>();

        for(DataSnapshot ds : staff.getChildren()){
            String key = ds.getKey();
            String username = ds.child("username").getValue(String.class);
            map.put(username, key);
        }
        return map;
    }

    public void reportAbsence() {
        RadioButton rb = (RadioButton) findViewById(names.getCheckedRadioButtonId());
        if (rb != null) {
            String uname = rb.getText().toString();
            final String key = keys.get(uname);
            mStaffRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    teamID = dataSnapshot.child("teamID").getValue(String.class);
                    mStaffRef.child(key).child("teamID").setValue("Absent");
                    removeFromTeam(key);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void removeFromTeam(final String key) {
        mTeamRef = FirebaseDatabase.getInstance().getReference(hotelID).child("teams").child(teamID).child("members");
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(String.class).equals(key)) {
                        mTeamRef.child(ds.getKey()).removeValue();
                        removeTeam(key);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeTeam(final String key) {
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() == false) {
                    FirebaseDatabase.getInstance().getReference(hotelID).child("teams").child(teamID).removeValue();
                }

                Intent i = new Intent(ReportAbsences.this, SupervisorHome.class);
                i.putExtras(extras);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
