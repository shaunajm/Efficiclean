package com.app.efficiclean.classes;

public class SevereMessApproval extends Approval {

    private int priorityCounter;
    private String description;

    public SevereMessApproval() {
        super();
        priorityCounter = 2;
    }

    public int getPriorityCounter() {
        return priorityCounter;
    }

    public void incrementPriorityCounter() {
        this.priorityCounter++;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
