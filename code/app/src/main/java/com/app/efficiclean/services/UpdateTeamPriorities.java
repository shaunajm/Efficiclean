package com.app.efficiclean.services;

import android.os.Bundle;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.*;

public class UpdateTeamPriorities extends JobService {

    private String hid;
    private DatabaseReference mTeamRef;
    private DataSnapshot teams;
    private Bundle extras;

    @Override
    public boolean onStartJob(final JobParameters job) {
        extras = job.getExtras();
        hid = extras.getString("hid");

        mTeamRef = FirebaseDatabase.getInstance().getReference(hid).child("teams");
        mTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teams = dataSnapshot;
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
        if (teams.hasChildren()) {
            for (DataSnapshot team : teams.getChildren()) {
                String key = team.getKey();
                int priority = team.child("priorityCounter").getValue(int.class);
                String status = team.child("status").getValue(String.class);
                if (status.equals("Waiting")) {
                    priority++;
                    mTeamRef.child(key).child("priorityCounter").setValue(priority);
                }
            }
        }
    }
}
