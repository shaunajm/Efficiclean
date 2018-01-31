package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class StaffLogin extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference mStaffRef = db.getReference().child("staff");
    private ArrayList<Staff> staffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    public void staffLoginButtonClick(View view) {
        EditText username = (EditText) findViewById(R.id.etUsername);
        EditText password = (EditText) findViewById(R.id.etPassword);
        String uString = username.getText().toString().trim();
        String pString = password.getText().toString().trim();
        staffLogin(uString, pString);
    }

    private void staffLogin(String uString, String pString) {
        for (Staff staff : this.staffList) {
            if (staff.getUsername().equals(uString) && staff.getPassword().equals(pString)) {
                Intent staffPage = new Intent(getApplicationContext(), StaffHome.class);
                startActivity(staffPage);
            }
        }
    }

}
