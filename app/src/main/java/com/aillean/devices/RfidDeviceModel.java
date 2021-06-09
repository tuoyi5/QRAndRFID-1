package com.aillean.devices;

import android.content.Context;

import com.aillean.common.eventbus.QRReadCodeEvent;
import com.aillean.tool.DeviceType;
import com.seuic.uhf.EPC;
import com.seuic.uhf.UHFService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class RfidDeviceModel {

    private static final String TAG = RfidDeviceModel.class.getSimpleName();
    private Context context;
    private EventBus eventBus;
    private UHFService mDevice;

    private List<String> deviceIdList = new ArrayList<>();

    public RfidDeviceModel(Context context, EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    public void registerUhfDevice() {
        mDevice = UHFService.getInstance();
        if (mDevice != null) {
            mDevice.open();
        }
    }

    public void unregisterUhfDevice() {
        if (mDevice != null) {
            mDevice.close();
            mDevice = null;
        }
    }

    public List<EPC> readTags() {
        return mDevice.getTagIDs();
    }

    public boolean startSearchTag() {
        return mDevice.inventoryStart();
    }

    public boolean stopSearchTag() {
        return mDevice.inventoryStop();
    }

    public String readTag() {
        EPC epc = new EPC();
        if (mDevice.inventoryOnce(epc, 100)) {
            eventBus.post(new QRReadCodeEvent().setReadCode(epc.getId()));
        }

        return null;
    }
}
