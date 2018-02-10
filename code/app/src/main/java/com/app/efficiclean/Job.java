package com.app.efficiclean;

import java.sql.Timestamp;

public class Job {

    private String createdBy;
    private String roomNumber;
    private int timestamp;
    private int priorityCounter;
    private boolean isCompleted;
    private Housekeeper assignedTo;

    public Job(String guestID, String rNumber, int pCounter) {
        createdBy = guestID;
        roomNumber = rNumber;
        timestamp = (int) new Timestamp(System.currentTimeMillis()).getTime();
        priorityCounter = pCounter;
        isCompleted = false;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getPriority() {
        return priorityCounter;
    }

    public void setPriority(int pCounter) {
        priorityCounter = pCounter;
    }

    public Housekeeper getAssignedTo() {
        return assignedTo;
    }

    public void assignTo(Housekeeper hk) {
        assignedTo = hk;
    }

    public boolean getStatus() {
        return isCompleted;
    }

    public void markCompleted(boolean completed) {
        isCompleted = completed;
    }
}
