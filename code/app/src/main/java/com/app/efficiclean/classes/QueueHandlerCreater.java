package com.app.efficiclean.classes;

public class QueueHandlerCreater {

    public static QueueHandler createHandler(String hid) {
        JobQueue jQueue = new JobQueue(hid);
        StaffQueue sQueue = new StaffQueue(hid);

        QueueHandler qh = new QueueHandler(hid, jQueue, sQueue);

        jQueue.addObserver(qh);
        sQueue.addObserver(qh);

        return qh;
    }
}
