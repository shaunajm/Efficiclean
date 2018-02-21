package com.app.efficiclean.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.app.efficiclean.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.HashMap;

public class MapView extends AppCompatActivity {

    private HashMap<String, String> rooms;
    private DatabaseReference mRoomRef;
    private String hotelID;
    private Bundle extras;
    private VectorMasterView map;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.efficiclean.R.layout.activity_map_view);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
        }

        map = (VectorMasterView) findViewById(R.id.vmMap);

        mRoomRef = FirebaseDatabase.getInstance().getReference(hotelID).child("rooms");
        mRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rooms = new HashMap<String, String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String roomNumber = ds.getKey();
                    String status = ds.child("status").getValue(String.class);
                    if (roomNumber != null && status != null) {
                        rooms.put(roomNumber, status);
                    }
                }
                changeRoomColours();
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

    public void changeRoomColours() {
        for (String roomNumber : rooms.keySet()) {
            String status = rooms.get(roomNumber);
            String pathName = "pRoom" + roomNumber;
            PathModel room = map.getPathModelByName(pathName);
            if (status.equals("Waiting")) {
                room.setFillColor(Color.parseColor("#ffff00"));
            } else if (status.equals("Do Not Disturb")) {
                room.setFillColor(Color.parseColor("#000000"));
            } else if (status.equals("To Be Cleaned")) {
                room.setFillColor(Color.parseColor("#ff0000"));
            } else if (status.equals("Completed")) {
                room.setFillColor(Color.parseColor("#008000"));
            } else if (status.equals("In Process")) {
                room.setFillColor(Color.parseColor("#ff6600"));
            } else {
                room.setFillColor(Color.parseColor("#ffeeaa"));
            }
        }
        map.update();
    }
}
