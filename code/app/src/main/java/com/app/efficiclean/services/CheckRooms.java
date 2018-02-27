package com.app.efficiclean.services;

import android.os.Bundle;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.*;

public class CheckRooms extends JobService {

    private String hid;
    private Bundle extras;
    private DatabaseReference mRootRef;
    private DataSnapshot teams;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        extras = jobParameters.getExtras();
        hid = extras.getString("hid");

        mRootRef = FirebaseDatabase.getInstance().getReference(hid);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams = dataSnapshot.child("teams");
                allocateMarkingTask();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public void allocateMarkingTask() {
        for (DataSnapshot team : teams.getChildren()) {
            String status = team.child("status").getValue(String.class);
            if (!status.equals("Checking Rooms")) {
                mRootRef.child("teams").child(team.getKey()).child("status").setValue("Checking Rooms");
                break;
            }
        }
    }
}
