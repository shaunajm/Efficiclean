package com.app.efficiclean.classes;

public class QueueHandlerCreater {

    public static QueueHandler createHandler(String hid) {
        JobQueue jQueue = new JobQueue(hid);
        TeamQueue tQueue = new TeamQueue(hid);

        QueueHandler qh = new QueueHandler(hid, jQueue, tQueue);

        jQueue.addObserver(qh);
        tQueue.addObserver(qh);

        return qh;
    }
}
