package com.app.efficiclean;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class JobQueue {

    private ArrayList<Job> jQueue = new ArrayList<Job>();
    private String hotelID;
    private DatabaseReference mJobRef;

    public JobQueue(String hid) {
        hotelID = hid;
        mJobRef = FirebaseDatabase.getInstance().getReference(hotelID).child("jobs");
        mJobRef.orderByChild("priority").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!jQueue.isEmpty()) {
                    jQueue = new ArrayList<Job>();
                }
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    Job newJob = job.getValue(Job.class);
                    newJob.key = job.getKey();
                    jQueue.add(newJob);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Job dequeue() {
        Job nextJob = jQueue.remove(jQueue.size() - 1);
        mJobRef.child(nextJob.key).removeValue();
        return nextJob;
    }

}
