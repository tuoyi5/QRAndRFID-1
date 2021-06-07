package com.aillean.common.rx;

import android.content.Context;

import com.aillean.common.rx.manager.RxManager;

import java.util.concurrent.ConcurrentHashMap;

public class ThreadPoolHolder {

    private ConcurrentHashMap<String, RxManager> threadPoolMap = new ConcurrentHashMap<>();

    public RxManager getRxManager(Context context, String identifier, boolean multi) {
        String poolKey = identifier + (multi ? "-multi" : "-single");
        RxManager rxManager = threadPoolMap.get(poolKey);
        if (rxManager == null) {
            RxManager.Builder.initAppContext(context.getApplicationContext());
            rxManager = multi ? RxManager.Builder.newMultiThreadManager() : RxManager.Builder.newSingleThreadManager();
            threadPoolMap.put(poolKey, rxManager);
        }
        return rxManager;
    }
}
