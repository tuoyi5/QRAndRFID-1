package com.aillean.common.request;


import com.aillean.common.manager.DataManager;
import com.aillean.common.rx.RxRequest;

public abstract class BaseDataRequest<T extends DataManager> extends RxRequest {

    private T dataManager;

    public BaseDataRequest(T dataManager) {
        this.dataManager = dataManager;
    }

    public void execute(final T manager) throws Exception {
    }

    @Override
    public void execute() throws Exception {
        execute(getManger());
    }

    public T getManger() {
        return dataManager;
    }

    public String getIdentifier() {
        return "data";
    }
}
