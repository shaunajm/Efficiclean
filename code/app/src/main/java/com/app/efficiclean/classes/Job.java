package com.app.efficiclean.classes;

import java.sql.Timestamp;

public class Job {

    private String createdBy;
    private String roomNumber;
    private int timestamp;
    private int priorityCounter;
    private boolean isCompleted;
    private String assignedTo;
    private String description;
    public String key;

    public Job() {
        timestamp = (int) new Timestamp(System.currentTimeMillis()).getTime();
        isCompleted = false;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void assignTo(String hk) {
        assignedTo = hk;
    }

    public boolean getStatus() {
        return isCompleted;
    }

    public void markCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
