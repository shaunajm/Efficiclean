package com.app.efficiclean;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import com.google.firebase.database.*;

public class GuestLogin extends AppCompatActivity {

    private EditText forename;
    private EditText surname;
    private String relevantGuestName;
    private String relevantStaffName;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mStaffRef = mRootRef.child("staff").child("name");
    private DatabaseReference mGuestRef = mRootRef.child("guest").child("name");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGuestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                relevantGuestName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mStaffRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                relevantStaffName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loginButtonClick(View view) {
        this.forename = (EditText) findViewById(R.id.etForename);
        this.surname = (EditText) findViewById(R.id.etSurname);
        String name = this.forename.getText().toString() + " " + this.surname.getText().toString();
        if (this.relevantGuestName.equals(name)) {
            Intent guest = new Intent(getApplicationContext(), GuestHome.class);
            startActivity(guest);
        } else if (this.relevantStaffName.equals(name)) {
            Intent staff = new Intent (getApplicationContext(), StaffHome.class);
            startActivity(staff);
        }
    }

}
