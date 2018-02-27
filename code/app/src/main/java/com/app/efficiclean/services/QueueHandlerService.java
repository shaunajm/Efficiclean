package com.app.efficiclean.services;

import android.os.Bundle;
import com.app.efficiclean.classes.QueueHandler;
import com.app.efficiclean.classes.QueueHandlerCreater;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class QueueHandlerService extends JobService {

    private QueueHandler qHandler;
    private Bundle extras;
    private String hid;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        extras = jobParameters.getExtras();
        hid = extras.getString("hid");

        qHandler = QueueHandlerCreater.createHandler(hid);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
