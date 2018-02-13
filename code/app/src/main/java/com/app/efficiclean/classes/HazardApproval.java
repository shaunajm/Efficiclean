package com.app.efficiclean.classes;

public class HazardApproval extends Approval {

    private int priorityCounter;
    private String description;

    public HazardApproval() {
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
