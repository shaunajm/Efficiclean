package com.app.efficiclean;

import java.sql.Timestamp;

public class Job {

    private Guest createdBy;
    private Timestamp timestamp;
    private Housekeeper assignedTo;
    private boolean isCompleted;

    public Job(Guest guest) {
        createdBy = guest;
        timestamp = new Timestamp(System.currentTimeMillis());
        isCompleted = false;
    }

    public Guest getCreatedBy() {
        return createdBy;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Housekeeper getAssignedTo() {
        return assignedTo;
    }

    public boolean getStatus() {
        return isCompleted;
    }
}
