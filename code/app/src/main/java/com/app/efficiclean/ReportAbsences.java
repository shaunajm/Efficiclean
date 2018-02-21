package com.app.efficiclean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.efficiclean.classes.Housekeeper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_report_absence);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                reportAbsence();
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

    public void populateGroup(){
        names.removeAllViews();
        for (DataSnapshot ds : staff.getChildren()) {
            Housekeeper hs = ds.getValue(Housekeeper.class);
            String username = hs.getUsername();

            RadioButton r1 = new RadioButton(this);
            r1.setText(username);
            r1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            r1.setLayoutParams(names.getLayoutParams());
            names.addView(r1);
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
        String uname = rb.getText().toString();
        String key = keys.get(uname);
        mStaffRef.child(key).child("status").setValue("Absent");
    }
}

