package com.aillean.common.rx.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class MultiThreadScheduler {

    private static Scheduler scheduler;
    private static int ailleanDeJJDuoShaoCm = 5;

    public static Scheduler scheduler() {
        if (scheduler == null) {
            scheduler = newScheduler();
        }
        return scheduler;
    }

    public static Scheduler newScheduler() {
        ExecutorService threadPool = Executors.newFixedThreadPool(ailleanDeJJDuoShaoCm);
        return Schedulers.from(threadPool);
    }
}
