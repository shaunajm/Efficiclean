package com.app.efficiclean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class GuestLogin extends AppCompatActivity {

    private static final String TAG = "Efficiclean";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef;
    private EditText hotelID;
    private EditText roomNumber;
    private EditText forename;
    private EditText surname;
    private Button loginBtn;
    private String guestKey;
    private String guestForename;
    private String guestSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        //Map EditTexts to their xml elements
        hotelID = (EditText) findViewById(R.id.etHotelID);
        roomNumber = (EditText) findViewById(R.id.etRoomNumber);
        forename = (EditText) findViewById(R.id.etForename);
        surname = (EditText) findViewById(R.id.etSurname);

        //Map to xml button and set listener
        loginBtn = (Button) findViewById(R.id.btLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClick();
            }
        });

        //Get instance of Firebase database
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //Create Firebase authenticator
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

        //Create Firebase authentication listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth fbAuth) {
                //Get current user
                FirebaseUser user = fbAuth.getCurrentUser();

                //Login user if not null
                if (user != null) {
                    Intent guestHomePage = new Intent(GuestLogin.this, GuestHome.class);
                    startActivity(guestHomePage);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Add authentication listener
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void loginButtonClick() {
        //Get input values from EditText boxes
        String hNumber = hotelID.getText().toString().trim();
        String rNumber = roomNumber.getText().toString().trim();
        String fString = forename.getText().toString().trim();
        String sString = surname.getText().toString().trim();

        if (!fString.equals("") && fString.equals("staff1")) {                                                      //Condition to pass to staff login page
            Intent staffPage = new Intent(getApplicationContext(), StaffLogin.class);
            startActivity(staffPage);
        } else if (!hNumber.equals("") && !rNumber.equals("") && !fString.equals("") && !sString.equals("")) {      //Check for no null values before we search database
            setValidationValues(hNumber, rNumber, fString, sString);
        } else {                                                                                                    //Display error message if incorrect user input
            Toast.makeText(GuestLogin.this, "Please complete all fields and try again.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setValidationValues(final String hNumber, final String rNumber, final String fString, final String sString) {
        //Create DatabaseReference to specified hotel room
        DatabaseReference mRoomRef = mRootRef.child(hNumber).child("rooms").child(rNumber).child("currentGuest");

        //Create ValueEventListener to read data from reference
        mRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get guest unique id key
                guestKey = dataSnapshot.getValue(String.class);

                if (guestKey != null) {
                    //Create DatabaseReference to specified guest
                    DatabaseReference mGuestRef = mRootRef.child(hNumber).child("guest").child(guestKey);

                    //Create ValueEventListener to read data from reference
                    mGuestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get guests values from Firebase
                            guestForename = dataSnapshot.child("forename").getValue(String.class);
                            guestSurname = dataSnapshot.child("surname").getValue(String.class);

                            //Start validation process
                            validateValues(rNumber, fString, sString);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(GuestLogin.this, "Your details seem to be incorrect. Please try again.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validateValues(String rNumber, String fString, String sString) {
        if (guestForename != null && guestSurname != null && guestForename.equals(fString) && guestSurname.equals(sString)){        //Validates that input data matches values from database
            //Create user email and password for authentication
            String pString = fString.toLowerCase() + sString.toLowerCase() + rNumber;
            String eString = pString + "@efficiclean.com";

            //Authorise user with Firebase
            mAuth.signInWithEmailAndPassword(eString, pString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(GuestLogin.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(GuestLogin.this, "Your details seem to be incorrect. Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
