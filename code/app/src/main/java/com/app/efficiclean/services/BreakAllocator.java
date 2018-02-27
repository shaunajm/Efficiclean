package com.app.efficiclean.services;

import android.os.Bundle;
import com.app.efficiclean.classes.Break;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;

public class BreakAllocator extends JobService {

    private final int ONE_MINUTE = 60000;
    private Bundle extras;
    private String hid;
    private DatabaseReference mRootRef;
    private DatabaseReference mBreakRef;
    private DatabaseReference mTeamRef;
    private DataSnapshot teams;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        extras = jobParameters.getExtras();
        hid = extras.getString("hid");

        mRootRef = FirebaseDatabase.getInstance().getReference(hid);
        mBreakRef = mRootRef.child("breakRequests");
        mTeamRef = mRootRef.child("teams");

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams = dataSnapshot.child("teams");
                allocateBreaks();
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

    public void allocateBreaks() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 12);
        now.set(Calendar.MINUTE, 30);
        now.set(Calendar.SECOND, 0);

        Date time = new Date(now.getTimeInMillis());

        for (DataSnapshot team : teams.getChildren()) {
            String key = team.getKey();
            int breakRemaining = team.child("breakRemaining").getValue(int.class);

            if (breakRemaining > 0) {
                Break newBreak = new Break();
                newBreak.setBreakLength(breakRemaining);
                newBreak.setBreakTimeDate(time);
                newBreak.setTeamID(key);
                newBreak.setAccepted(true);

                time = new Date(time.getTime() + (breakRemaining * ONE_MINUTE));

                mBreakRef.push().setValue(newBreak);
                mTeamRef.child(key).child("breakRemaining").setValue(0);
            }
        }
    }
}
