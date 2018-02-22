package com.app.efficiclean.services;

import android.os.Bundle;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.*;

public class UpdateJobPriorities extends JobService {

    private String hid;
    private DatabaseReference mJobRef;
    private DataSnapshot jobs;
    private Bundle extras;

    @Override
    public boolean onStartJob(final JobParameters job) {
        extras = job.getExtras();
        hid = extras.getString("hid");

        mJobRef = FirebaseDatabase.getInstance().getReference(hid).child("jobs");
        mJobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobs = dataSnapshot;
                updatePriorities();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public void updatePriorities() {
        if (jobs.hasChildren()) {
            for (DataSnapshot job : jobs.getChildren()) {
                String key = job.getKey();
                int priority = job.child("priority").getValue(int.class);
                priority++;
                mJobRef.child(key).child("priority").setValue(priority);
            }
        }
    }
}
