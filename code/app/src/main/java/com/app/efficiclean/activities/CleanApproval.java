package com.app.efficiclean.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Job;
import com.app.efficiclean.classes.Supervisor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CleanApproval extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private String roomNumber;
    private String approvalKey;
    private String oneSignalKey;
    private Bundle extras;
    private Supervisor supervisor;
    private DatabaseReference mSuperRef;
    private DatabaseReference mRootRef;
    private DatabaseReference mAppRef;
    private DatabaseReference mTeamRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CheckBox approve;
    private CheckBox disapprove;
    private EditText comments;
    private Button btApprove;
    private int cleans;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.efficiclean.R.layout.activity_supervisor_cleans_approval);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
            roomNumber = extras.getString("roomNumber");
            approvalKey = extras.getString("approvalKey");
        }

        TextView header = (TextView) findViewById(com.app.efficiclean.R.id.tvRoomNumber);
        header.setText("Room: " + roomNumber);

        approve = (CheckBox) findViewById(com.app.efficiclean.R.id.cbApprove);
        disapprove = (CheckBox) findViewById(com.app.efficiclean.R.id.cbDisapprove);
        comments = (EditText) findViewById(com.app.efficiclean.R.id.etComments);

        btApprove = (Button) findViewById(R.id.btCleansApprovalSubmit);
        btApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approve.isChecked()) {
                    approvedSubmit();
                } else if (disapprove.isChecked()) {
                    disapprovedSubmit();
                } else {
                    Toast.makeText(CleanApproval.this, "You haven't selected an option. Please check one of the boxes and try again.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference(hotelID);

        mSuperRef = FirebaseDatabase.getInstance().getReference(hotelID).child("supervisor").child(supervisorKey);
        mSuperRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supervisor = dataSnapshot.getValue(Supervisor.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAppRef = mSuperRef.child("approvals").child(approvalKey);
        mAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                job = dataSnapshot.child("job").getValue(Job.class);
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

    public void approvedSubmit() {
        String team = job.getAssignedTo();
        mTeamRef = mRootRef.child("teams").child(team);
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cleans = dataSnapshot.child("cleanCounter").getValue(int.class);
                cleans++;
                updateData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateData() {
        mTeamRef.child("cleanCounter").setValue(cleans);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("Completed");
        String uid = supervisor.approvals.get(approvalKey).getJob().getCreatedBy();
        mSuperRef.child("approvals").child(approvalKey).removeValue();
        sendNotification(uid);
        finish();
    }

    public void disapprovedSubmit() {
        String team = job.getAssignedTo();
        job.setDescription(comments.getText().toString());
        mRootRef.child("teams").child(team).child("returnedJob").setValue(job);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("In Progress");
        mAppRef.removeValue();
        finish();
    }

    public void sendNotification(final String uid) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    DatabaseReference mGuestRef = mRootRef.child("guest").child(uid);
                    mGuestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            oneSignalKey = dataSnapshot.child("oneSignalKey").getValue(String.class);
                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                try {
                                    String jsonResponse;

                                    URL url = new URL("https://onesignal.com/api/v1/notifications");
                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setUseCaches(false);
                                    con.setDoOutput(true);
                                    con.setDoInput(true);

                                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                    con.setRequestProperty("Authorization", "Basic ZWU2MTZiZWYtYmQyZi00M2E2LWFhZGYtMWM3MmQwMmUwZGY1");
                                    con.setRequestMethod("POST");

                                    //OneSignal.postNotification(new JSONObject("{'app_id': 'ad9055f5-5f63-418d-84bd-ef4e95021177', 'contents': {'en':'Your room has been serviced. Thank you for using Efficiclean!'}, 'filters': {'field': 'tag', 'key': 'uid', 'relation': '=', 'value': '" + uid + "'}}"), null);

                                    String strJsonBody = String.format("{"
                                            + "\"app_id\": \"ad9055f5-5f63-418d-84bd-ef4e95021177\","
                                            + "\"filters\": [{\"field\": \"tag\", \"key\": \"uid\", \"relation\": \"=\", \"value\": \"%s\"}],"
                                            + "\"data\": {\"foo\": \"bar\"},"
                                            + "\"contents\": {\"en\": \"Your room has been serviced. Thank you for using Efficiclean!\"}"
                                            + "}", uid);


                                    System.out.println("strJsonBody:\n" + strJsonBody);

                                    byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                    con.setFixedLengthStreamingMode(sendBytes.length);

                                    OutputStream outputStream = con.getOutputStream();
                                    outputStream.write(sendBytes);

                                    int httpResponse = con.getResponseCode();
                                    System.out.println("httpResponse: " + httpResponse);

                                    if (httpResponse >= HttpURLConnection.HTTP_OK
                                            && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                        Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                        scanner.close();
                                    } else {
                                        Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                        scanner.close();
                                    }
                                    System.out.println("jsonResponse:\n" + jsonResponse);
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }
        });
    }
}
