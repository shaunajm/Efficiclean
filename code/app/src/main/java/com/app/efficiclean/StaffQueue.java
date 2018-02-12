package com.app.efficiclean;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Observable;

public class StaffQueue extends Observable {

    private ArrayList<Housekeeper> sQueue = new ArrayList<Housekeeper>();
    private String hotelID;
    private DatabaseReference mStaffRef;

    public StaffQueue(String hid) {
        hotelID = hid;
        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff");
        mStaffRef.orderByChild("priority").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!sQueue.isEmpty()) {
                    sQueue = new ArrayList<Housekeeper>();
                }
                for (DataSnapshot hk : dataSnapshot.getChildren()) {
                    if (hk.child("status").getValue().equals("Waiting")) {
                        Housekeeper newHousekeeper = hk.getValue(Housekeeper.class);
                        newHousekeeper.key = hk.getKey();
                        sQueue.add(newHousekeeper);
                    }
                }
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Housekeeper dequeue() {
        Housekeeper nextHousekeeper = sQueue.remove(sQueue.size() - 1);
        nextHousekeeper.setStatus("Cleaning");
        mStaffRef.child(nextHousekeeper.key).setValue(nextHousekeeper);
        return nextHousekeeper;
    }

    public Boolean isEmpty() {
        return sQueue.size() == 0;
    }
}
