package com.app.efficiclean;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class GuestLogin extends AppCompatActivity {

    private AlertDialog alertDialog;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mGuestRef = db.getReference().child("guests");
    private DatabaseReference mStaffRef = db.getReference().child("staff");
    private ArrayList<Guest> guestList;
    private ArrayList<Staff> staffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
        this.alertDialog = new AlertDialog.Builder(GuestLogin.this).create();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGuestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                guestList = new ArrayList<Guest>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Guest guest = ds.getValue(Guest.class);
                    guestList.add(guest);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mStaffRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staffList = new ArrayList<Staff>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Staff staff = ds.getValue(Staff.class);
                    staffList.add(staff);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loginButtonClick(View view) {
        EditText forename = (EditText) findViewById(R.id.etForename);
        EditText surname = (EditText) findViewById(R.id.etSurname);
        String fString = forename.getText().toString().trim();
        String sString = surname.getText().toString().trim();
        if (fString.equals("staff")) {
            staffLogin(fString, sString);
        } else {
            guestLogin(fString, sString);
        }
    }

    private void staffLogin(String fString, String sString) {
        for (Staff staff : this.staffList) {
            if (staff.getUsername().equals(sString)) {
                Intent staffPage = new Intent(getApplicationContext(), StaffHome.class);
                startActivity(staffPage);
            }
        }
    }

    private void guestLogin(String fString, String sString) {
        for (Guest guest : this.guestList) {
            if (guest.getForename().equals(fString) && guest.getSurname().equals(sString)) {
                Intent guestPage = new Intent(getApplicationContext(), GuestHome.class);
                startActivity(guestPage);
            }
        }
    }

    private void errorMessage() {
        this.alertDialog.setMessage("Could not find details. Please try again.");
        this.alertDialog.show();
    }

}
