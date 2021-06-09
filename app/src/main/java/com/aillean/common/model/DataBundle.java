package com.aillean.common.model;

import android.content.Context;

import com.aillean.common.action.NetworkDataAction;
import com.aillean.common.action.RfidAction;
import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.manager.LocalDataManager;
import com.aillean.devices.QrDeviceModel;
import com.aillean.devices.RfidDeviceModel;
import com.aillean.tool.DeviceType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class DataBundle {

    private Context appContext;
    private CloudDataManager mCloudDataManager;
    private LocalDataManager mLocalDataManager;
    private EventBus eventBus;
    private DeviceType selectedDeviceType = DeviceType.RFID;
    private DeviceType currentlyDeviceType = selectedDeviceType;

    private QrDeviceModel mQrDeviceModel;
    private RfidDeviceModel mRfidDeviceModel;

    private ArrayList<Integer> entityKeys = new ArrayList<>();

    public DataBundle(Context appContext) {
        this.appContext = appContext;
        eventBus = new EventBus();
        mCloudDataManager = new CloudDataManager(eventBus);
        mLocalDataManager = new LocalDataManager(eventBus);
        mQrDeviceModel = new QrDeviceModel(appContext, eventBus);
        mRfidDeviceModel = new RfidDeviceModel(appContext, eventBus);
        register();
        initEntityKeys();
    }

    public void initEntityKeys() {
        entityKeys.add(249);
        entityKeys.add(250);
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public CloudDataManager getCloudDataManager() {
        return mCloudDataManager;
    }

    public void setCloudDataManager(CloudDataManager cloudDataManager) {
        mCloudDataManager = cloudDataManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }


    public void setSelectedDeviceType(DeviceType selectedDeviceType) {
        this.selectedDeviceType = selectedDeviceType;
    }

    public void startNetworkDataAction(String ipProt,String code, DeviceType type) {
        NetworkDataAction action = new NetworkDataAction<>(appContext, mCloudDataManager)
                .setCode(code).setDeviceType(type).setIpPort(ipProt);
        action.execute();
    }

    private void startQrOrRfid(DeviceType mDeviceType) {
        switch (mDeviceType) {
            case RFID:
                RfidAction action = new RfidAction<>(appContext, mLocalDataManager)
                        .setRfidDeviceModel(mRfidDeviceModel);
                action.execute();
                break;
            case QR:
                mQrDeviceModel.startScan();
                break;
        }
        currentlyDeviceType = mDeviceType;
    }

    public void stopQrOrRfid(DeviceType mDeviceType) {
        switch (mDeviceType) {
            case RFID:
                mRfidDeviceModel.stopSearchTag();
                break;
            case QR:
                mQrDeviceModel.stopScan();
                break;
        }
    }

    public boolean onKeyDown(int keyCode) {
        if (!entityKeys.contains(keyCode)) {
            return false;
        }
        startQrOrRfid(selectedDeviceType);
        return true;
    }

    public boolean onKeyUp(int keyCode) {
        if (!entityKeys.contains(keyCode)) {
            return false;
        }

        stopQrOrRfid(currentlyDeviceType);
        return true;
    }

    public void register() {
        mQrDeviceModel.registerScannerFactory(this.appContext);
        mRfidDeviceModel.registerUhfDevice();
    }

    public void unregister() {
        mQrDeviceModel.unregisterScannerFactory(this.appContext);
        mRfidDeviceModel.unregisterUhfDevice();
    }

    public QrDeviceModel getQrDeviceModel() {
        return mQrDeviceModel;
    }

    public void setQrDeviceModel(QrDeviceModel qrDeviceModel) {
        mQrDeviceModel = qrDeviceModel;
    }

    public RfidDeviceModel getRfidDeviceModel() {
        return mRfidDeviceModel;
    }

    public void setRfidDeviceModel(RfidDeviceModel rfidDeviceModel) {
        mRfidDeviceModel = rfidDeviceModel;
    }
}
