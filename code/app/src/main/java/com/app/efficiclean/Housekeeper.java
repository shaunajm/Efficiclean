package com.app.efficiclean;

public class Housekeeper extends Staff {

    private int priorityCounter;
    private Job currentJob;
    private String status;

    public Housekeeper() {
        super();
    }

    public int getPriorityCounter() {
        return priorityCounter;
    }

    public void setPriorityCounter(int priorityCounter) {
        this.priorityCounter = priorityCounter;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
