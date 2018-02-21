package com.app.efficiclean;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
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
    private Bundle extras;
    private Supervisor supervisor;
    private DatabaseReference mSuperRef;
    private DatabaseReference mRootRef;
    private DatabaseReference mAppRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CheckBox approve;
    private CheckBox disapprove;
    private EditText comments;
    private Button btApprove;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_cleans_approval);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
            roomNumber = extras.getString("roomNumber");
            approvalKey = extras.getString("approvalKey");
        }

        TextView header = (TextView) findViewById(R.id.tvRoomNumber);
        header.setText("Room: " + roomNumber);

        approve = (CheckBox) findViewById(R.id.cbApprove);
        disapprove = (CheckBox) findViewById(R.id.cbDisapprove);
        comments = (EditText) findViewById(R.id.etComments);

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
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("Completed");
        String uid = supervisor.approvals.get(approvalKey).getJob().getCreatedBy();
        mSuperRef.child("approvals").child(approvalKey).removeValue();
        sendNotification(uid);
        finish();
    }

    public void disapprovedSubmit() {
        String hKeeper = job.getAssignedTo();
        job.setDescription(comments.getText().toString());
        mRootRef.child("staff").child(hKeeper).child("returnedJob").setValue(job);
        mRootRef.child("rooms").child(roomNumber).child("status").setValue("In Progress");
        mAppRef.removeValue();
        finish();
    }

    public void sendNotification(final String uid) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    //This is a Simple Logic to Send Notification different Device Programmatically...

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

                        String strJsonBody = "{"
                                + "\"app_id\": \"ad9055f5-5f63-418d-84bd-ef4e95021177\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"uid\", \"relation\": \"=\", \"value\": \"" + uid + "\"}],"

                                + "\"contents\": {\"en\": \"Your room has been serviced. Thank you for using Efficiclean!\"}"
                                + "}";


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
        });
    }
}
