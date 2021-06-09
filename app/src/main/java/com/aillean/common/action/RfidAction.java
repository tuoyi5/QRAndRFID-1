package com.aillean.common.action;

import android.content.Context;
import android.util.Log;

import com.aillean.common.eventbus.RecvWSEvent;
import com.aillean.common.eventbus.RfidReadCodeEvent;
import com.aillean.common.manager.DataManager;
import com.aillean.common.manager.LocalDataManager;
import com.aillean.common.request.RfidRequest;
import com.aillean.common.rx.RxCallback;
import com.aillean.devices.RfidDeviceModel;

import androidx.annotation.NonNull;

public
class RfidAction<T extends DataManager> extends DataAction {

    RfidDeviceModel mRfidDeviceModel;

    public RfidAction(Context context, LocalDataManager dataManager) {
        super(context, dataManager);
    }

    public RfidDeviceModel getRfidDeviceModel() {
        return mRfidDeviceModel;
    }

    public RfidAction setRfidDeviceModel(RfidDeviceModel rfidDeviceModel) {
        mRfidDeviceModel = rfidDeviceModel;
        return this;
    }

    public void execute() {
        Log.i("aillean", "RfidaAction start");
        LocalDataManager localDataManager = (LocalDataManager) getDataManager();

        RfidRequest request = new RfidRequest(localDataManager)
                .setRfidDeviceModel(mRfidDeviceModel);

        getDataManager().submit(getContext(), request, new RxCallback() {
            @Override
            public void onNext(@NonNull Object o) {
                localDataManager.getEventbus().post(new RfidReadCodeEvent().setReadCode(request.getReadRfidTag()));
            }
        });
    }
}
