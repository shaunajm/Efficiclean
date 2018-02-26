package com.app.efficiclean.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.*;
import com.app.efficiclean.services.ResetSystemStatus;
import com.app.efficiclean.services.TeamAllocator;
import com.app.efficiclean.services.UpdateJobPriorities;
import com.app.efficiclean.services.UpdateTeamPriorities;
import com.firebase.jobdispatcher.*;
import com.firebase.jobdispatcher.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.util.Calendar;

public class GuestLogin extends AppCompatActivity {
    
    public String hid = "";
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference mRootRef;
    public EditText hotelID;
    public EditText roomNumber;
    public EditText forename;
    public EditText surname;
    public Button loginBtn;
    public ProgressBar spinner;
    public Guest guest;
    public QueueHandler qHandler;
    private OSPermissionSubscriptionState status;
    private FirebaseJobDispatcher jobDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        status = OneSignal.getPermissionSubscriptionState();

        //Map EditTexts to their xml elements
        hotelID = (EditText) findViewById(R.id.etHotelID);
        roomNumber = (EditText) findViewById(R.id.etRoomNumber);
        forename = (EditText) findViewById(R.id.etForename);
        surname = (EditText) findViewById(R.id.etSurname);

        //Set progress spinner
        spinner = (ProgressBar) findViewById(R.id.guestLoginProgress);
        spinner.setVisibility(View.GONE);

        //Map to xml button and set listener
        loginBtn = (Button) findViewById(R.id.btLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.bringToFront();
                spinner.invalidate();
                spinner.setVisibility(View.VISIBLE);
                loginButtonClick();
            }
        });

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

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
                    scheduleReset();
                    allocateTeams();
                    updateJobPriorities();
                    updateTeamPriorities();
                    OneSignal.sendTag("uid", user.getUid());

                    //Create Bundle to pass information to next activity
                    Bundle bundle = new Bundle();
                    bundle.putString("hotelID", hotelID.getText().toString().trim());
                    bundle.putSerializable("thisGuest", guest);

                    spinner.setVisibility(View.GONE);
                    Intent guestHomePage = new Intent(GuestLogin.this, GuestHome.class);
                    guestHomePage.putExtras(bundle);
                    startActivity(guestHomePage);
                    onStop();
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

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void loginButtonClick() {
        //Get input values from EditText boxes
        String hNumber = hotelID.getText().toString().trim();
        String rNumber = roomNumber.getText().toString().trim();
        String fString = forename.getText().toString().trim();
        String sString = surname.getText().toString().trim();

        hid = hNumber;

        qHandler = QueueHandlerCreater.createHandler(hNumber);

        if (!fString.equals("") && fString.equals("staff1")) {
            //Condition to pass to staff login page
            spinner.setVisibility(View.GONE);
            Intent staffPage = new Intent(getApplicationContext(), StaffLogin.class);
            startActivity(staffPage);
        } else if (!hNumber.equals("") && !rNumber.equals("") && !fString.equals("") && !sString.equals("")) {      //Check for no null values before we search database
            setValidationValues(hNumber, rNumber, fString, sString);
        } else {
            //Display error message if incorrect user input
            spinner.setVisibility(View.GONE);
            Toast.makeText(GuestLogin.this, "Please complete all fields and try again.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void setValidationValues(final String hNumber, final String rNumber, final String fString, final String sString) {
        //Create DatabaseReference to specified hotel room
        DatabaseReference mRoomRef = mRootRef.child(hNumber).child("rooms").child(rNumber).child("currentGuest");

        //Create ValueEventListener to read data from reference
        mRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get guest unique id key
                String guestKey = dataSnapshot.getValue(String.class);

                if (guestKey != null) {
                    //Create DatabaseReference to specified guest
                    DatabaseReference mGuestRef = mRootRef.child(hNumber).child("guest").child(guestKey);

                    //Create ValueEventListener to read data from reference
                    mGuestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get guests values from Firebase
                            guest = dataSnapshot.getValue(Guest.class);

                            //Start validation process
                            validateValues(rNumber, fString, sString);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(GuestLogin.this, "Your details seem to be incorrect. Please try again.",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void validateValues(String rNumber, String fString, String sString) {
        if (guest != null && guest.getForename().equals(fString) && guest.getSurname().equals(sString)){        //Validates that input data matches values from database
            //Create user email and password for authentication
            String pString = fString.toLowerCase() + sString.toLowerCase() + rNumber;
            final String eString = pString + "@efficiclean.com";

            //Authorise user with Firebase
            mAuth.signInWithEmailAndPassword(eString, pString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(GuestLogin.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                OneSignal.syncHashedEmail(eString);
                            }
                        }
                    });
        } else {
            spinner.setVisibility(View.GONE);
            Toast.makeText(GuestLogin.this, "Your details seem to be incorrect. Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public int[] getTimes(int hour, int minute) {
        Calendar currentTime = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, hour);
        midnight.set(Calendar.MINUTE, minute);

        long difference = midnight.getTimeInMillis() - currentTime.getTimeInMillis();

        int startSeconds = (int) (difference / 1000);
        int endSeconds = startSeconds + 86400;

        int[] times = {startSeconds, endSeconds};
        return times;
    }

    public void scheduleReset() {
        int[] times = getTimes(23, 45);

        Bundle extras = new Bundle();
        extras.putString("hid", hid);

        Job job = jobDispatcher.newJobBuilder()
                .setService(ResetSystemStatus.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(hid + " RESET SERVICE")
                .setTrigger(Trigger.executionWindow(times[0], times[1]))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(extras)
                .build();

        jobDispatcher.mustSchedule(job);
        Log.v(hid + " RESET SERVICE", "Reset system data for next day");
    }

    public void allocateTeams() {
        int[] times = getTimes(23, 55);

        Bundle extras = new Bundle();
        extras.putString("hid", hid);

        Job job = jobDispatcher.newJobBuilder()
                .setService(TeamAllocator.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(hid + " TEAM SERVICE")
                .setTrigger(Trigger.executionWindow(times[0], times[1]))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(extras)
                .build();

        jobDispatcher.mustSchedule(job);
        Log.v(hid + " TEAM SERVICE", "Allocate housekeeper teams for next day");
    }

    public void updateJobPriorities() {
        Bundle extras = new Bundle();
        extras.putString("hid", hid);

        Job job = jobDispatcher.newJobBuilder()
                .setService(UpdateJobPriorities.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(hid + " JOB SERVICE")
                .setTrigger(Trigger.executionWindow(300, 300))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(extras)
                .build();

        jobDispatcher.mustSchedule(job);
        Log.v(hid + " JOB SERVICE", "Update priority of jobs on the queue");
    }

    public void updateTeamPriorities() {
        Bundle extras = new Bundle();
        extras.putString("hid", hid);

        Job job = jobDispatcher.newJobBuilder()
                .setService(UpdateTeamPriorities.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(hid + " TEAM PRIORITY SERVICE")
                .setTrigger(Trigger.executionWindow(600, 600))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(extras)
                .build();

        jobDispatcher.mustSchedule(job);
        Log.v(hid + " TEAM PRIORITY SERVICE", "Update priority of teams on the queue");
    }
}
