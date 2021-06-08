package com.aillean.common.request;

import android.text.TextUtils;

import com.aillean.common.eventbus.RfidActionEvent;
import com.aillean.common.manager.DataManager;
import com.aillean.common.manager.LocalDataManager;
import com.aillean.devices.RfidDeviceModel;
import com.aillean.devices.rfid.utils.RfidState;
import com.aillean.devices.rfid.utils.RfidTag;

import org.greenrobot.eventbus.EventBus;

public class RfidRequest extends BaseDataRequest {

    private RfidState mRfidState;
    private RfidDeviceModel mRfidDeviceModel;

    String readRfidTag;
    boolean wriftRfid;
    boolean isOpen = false;

    public <T extends DataManager> RfidRequest(LocalDataManager dataManager) {
        super(dataManager);
    }

    public RfidRequest setRfidState(RfidState rfidState) {
        mRfidState = rfidState;
        return this;
    }


    public RfidRequest setRfidDeviceModel(RfidDeviceModel rfidDeviceModel) {
        mRfidDeviceModel = rfidDeviceModel;
        return this;
    }

    public String getReadRfidTag() {
        return readRfidTag;
    }

    public boolean isWriftRfid() {
        return wriftRfid;
    }

    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void execute() throws Exception {
        switch (mRfidState) {
            case OPEN:
                String btMac = mRfidDeviceModel.open();
                isOpen = !TextUtils.isEmpty(btMac);
                if (isOpen) {
                    poshEvent(RfidState.START_SEARCH);
                }
                break;
            case START_SEARCH:
                mRfidDeviceModel.startSearchTag();
                poshEvent(RfidState.STOP_SEARCH);
                break;
            case STOP_SEARCH:
                mRfidDeviceModel.stopSearchTag();
                break;
            case READ:
                readRfidTag= mRfidDeviceModel.readTag();
                break;
            case WRITE:
                wriftRfid = mRfidDeviceModel.writeTag();
                break;
        }
    }

    private void poshEvent(RfidState rfidState) {
        EventBus.getDefault().post(new RfidActionEvent(rfidState));
    }
}
