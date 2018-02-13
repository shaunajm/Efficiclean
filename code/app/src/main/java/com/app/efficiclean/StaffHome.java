package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.app.efficiclean.classes.Housekeeper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class StaffHome extends AppCompatActivity {

    private Button btRequestBreak;
    private Button btCurrentRoom;
    private Housekeeper hKeeper;
    private String staffKey;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mStaffRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            staffKey = extras.getString("staffKey");
        }

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Add authentication listener
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void todaysTeamsButtonClick(View v) {
        Intent i = new Intent(StaffHome.this, TodaysTeams.class);
        i.putExtras(extras);
        startActivity(i);
    }
}
