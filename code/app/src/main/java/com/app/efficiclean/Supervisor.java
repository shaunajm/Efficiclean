package com.app.efficiclean;

import java.util.ArrayList;

public class Supervisor extends Staff {

    private boolean onDuty;
    public ArrayList<Approval> approvals;

    public Supervisor() {
        super();
        approvals = new ArrayList<Approval>();
    }

    public boolean isOnDuty() {
        return onDuty;
    }

    public void setOnDuty(boolean onDuty) {
        this.onDuty = onDuty;
    }
}
