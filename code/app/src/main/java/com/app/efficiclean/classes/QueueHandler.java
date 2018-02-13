package com.app.efficiclean.classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Observable;
import java.util.Observer;

public class QueueHandler implements Observer {

    private String hotelID;
    private JobQueue jQueue;
    private StaffQueue sQueue;

    public QueueHandler(String hid, JobQueue jq, StaffQueue sq) {
        hotelID = hid;
        jQueue = jq;
        sQueue = sq;
    }

    public void update(Observable obj, Object queue) {
        if (!jQueue.isEmpty() && !sQueue.isEmpty()) {
            Job job = jQueue.dequeue();
            Housekeeper hk = sQueue.dequeue();
            hk.setCurrentJob(job);
            DatabaseReference mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff");
            mStaffRef.child(hk.key).setValue(hk);
        }
    }
}
