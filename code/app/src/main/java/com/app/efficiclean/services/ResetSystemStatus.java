package com.app.efficiclean.services;

import android.os.Bundle;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.*;

/**
 * Created by shaum on 20/02/18.
 */

public class ResetSystemStatus extends JobService {

    private String hid;
    private DatabaseReference mRootRef;
    private DatabaseReference mJobRef;
    private DatabaseReference mRoomRef;
    private DatabaseReference mStaffRef;
    private DatabaseReference mSuperRef;
    private DataSnapshot rooms;
    private DataSnapshot staff;
    private DataSnapshot supervisor;
    private Bundle extras;

    @Override
    public boolean onStartJob (JobParameters job) {
        extras = job.getExtras();
        hid = extras.getString("hid");

        mRootRef = FirebaseDatabase.getInstance().getReference(hid);
        mJobRef = mRootRef.child("jobs");
        mRoomRef = mRootRef.child("rooms");
        mStaffRef = mRootRef.child("staff");
        mSuperRef = mRootRef.child("supervisor");

        mJobRef.removeValue();

        mRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rooms = dataSnapshot;
                editRooms();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mStaffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staff = dataSnapshot;
                editStaff();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSuperRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supervisor = dataSnapshot;
                editSupervisor();
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

    public void editRooms() {
        for (DataSnapshot room : rooms.getChildren()) {
            String key = room.getKey();
            mRoomRef.child(key).child("status").setValue("Idle");
        }
    }

    public void editStaff() {
        for (DataSnapshot housekeeper : staff.getChildren()) {
            String key = housekeeper.getKey();
            mStaffRef.child(key).child("currentJob").removeValue();
            mStaffRef.child(key).child("returnedJob").removeValue();
            mStaffRef.child(key).child("status").setValue("Waiting");
            mStaffRef.child(key).child("priorityCounter").setValue(0);
        }
    }

    public void editSupervisor() {
        for (DataSnapshot sVisor : supervisor.getChildren()) {
            String key = sVisor.getKey();
            mSuperRef.child(key).child("approvals").removeValue();
        }
    }
}