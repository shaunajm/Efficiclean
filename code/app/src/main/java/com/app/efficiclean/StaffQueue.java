package com.app.efficiclean;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class StaffQueue {

    private ArrayList<Housekeeper> sQueue = new ArrayList<Housekeeper>();
    private String hotelID;
    private DatabaseReference mStaffRef;

    public StaffQueue(String hid) {
        hotelID = hid;
        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("jobs");
        mStaffRef.orderByChild("priority").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!sQueue.isEmpty()) {
                    sQueue = new ArrayList<Housekeeper>();
                }
                for (DataSnapshot hk : dataSnapshot.getChildren()) {
                    Housekeeper newHousekeeper = hk.getValue(Housekeeper.class);
                    newHousekeeper.key = hk.getKey();
                    sQueue.add(newHousekeeper);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Housekeeper dequeue() {
        Housekeeper nextHousekeeper = sQueue.remove(sQueue.size() - 1);
        nextHousekeeper.setStatus("Cleaning");
        return nextHousekeeper;
    }
}
