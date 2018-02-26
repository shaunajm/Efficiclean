package com.app.efficiclean.services;

import android.os.Bundle;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;

public class UpdateBreakStatus extends JobService {

    private final int ONE_MINUTE = 60000;
    private String hid;
    private Bundle extras;
    private DatabaseReference mRootRef;
    private DataSnapshot breaks;
    private DataSnapshot teams;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        extras = jobParameters.getExtras();
        hid = extras.getString("hid");

        mRootRef = FirebaseDatabase.getInstance().getReference(hid);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                breaks = dataSnapshot.child("breakRequests");
                teams = dataSnapshot.child("teams");
                checkForBreak();
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

    public void checkForBreak() {
        for (DataSnapshot currentBreak : breaks.getChildren()) {
            String key = currentBreak.getKey();
            if (currentBreak.child("accepted").getValue(boolean.class)) {
                Date breakTime = currentBreak.child("breakTime").getValue(Date.class);
                int breakLength = currentBreak.child("breakLength").getValue(int.class);
                String teamKey = currentBreak.child("teamID").getValue(String.class);

                Date endBreak = new Date(breakTime.getTime() + (breakLength * ONE_MINUTE));

                Calendar now = Calendar.getInstance();

                if (now.getTimeInMillis() >= breakTime.getTime() && now.getTimeInMillis() <= endBreak.getTime()) {
                    mRootRef.child("teams").child(teamKey).child("status").setValue("On Break");
                } else if (now.getTimeInMillis() > endBreak.getTime()) {
                    mRootRef.child("teams").child(teamKey).child("status").setValue("Waiting");
                    mRootRef.child("teams").child(teamKey).child("priorityCounter").setValue(0);
                    mRootRef.child("breakRequests").child(key).removeValue();
                }
            }
        }
    }
}
