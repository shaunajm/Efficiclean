package com.app.efficiclean.classes;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Observable;

public class TeamQueue extends Observable {

    private ArrayList<Team> tQueue = new ArrayList<Team>();
    private String hotelID;
    private DatabaseReference mTeamRef;

    public TeamQueue(String hid) {
        hotelID = hid;
        mTeamRef = FirebaseDatabase.getInstance().getReference(hotelID).child("teams");
        mTeamRef.orderByChild("priorityCounter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!tQueue.isEmpty()) {
                    tQueue = new ArrayList<Team>();
                }
                for (DataSnapshot team : dataSnapshot.getChildren()) {
                    if (team.child("status").getValue().equals("Waiting")) {
                        Team newTeam = team.getValue(Team.class);
                        newTeam.key = team.getKey();
                        checkReturned(newTeam);
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

    public Team dequeue() {
        Team nextTeam = tQueue.remove(tQueue.size() - 1);
        nextTeam.setStatus("Cleaning");
        mTeamRef.child(nextTeam.key).setValue(nextTeam);
        return nextTeam;
    }

    public Boolean isEmpty() {
        return tQueue.size() == 0;
    }

    public void checkReturned(Team newTeam) {
        if (!newTeam.getStatus().equals("Checking Rooms") && newTeam.getCurrentJob() == null && newTeam.getReturnedJob() != null) {
            mTeamRef.child(newTeam.key).child("currentJob").setValue(newTeam.getReturnedJob());
            mTeamRef.child(newTeam.key).child("status").setValue("In Progress");
            mTeamRef.child(newTeam.key).child("returnedJob").removeValue();
        } else {
            tQueue.add(newTeam);
        }
    }
}
