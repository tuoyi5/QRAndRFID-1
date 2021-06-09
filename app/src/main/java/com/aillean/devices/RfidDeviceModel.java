package com.aillean.devices;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.aillean.common.eventbus.RfidReadCodeEvent;
import com.aillean.common.eventbus.RfidReadStopEvent;
import com.seuic.sleduhf.UhfCallback;
import com.seuic.sleduhf.UhfDevice;
import com.seuic.uhf.EPC;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RfidDeviceModel {

    private static final String TAG = RfidDeviceModel.class.getSimpleName();
    private Context context;
    private EventBus eventBus;
    private UhfDevice uhfDevice;


    public RfidDeviceModel(Context context, EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    UhfCallback uhfCallback= new UhfCallback() {
        @Override
        public void onGetBtInfo(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void onGetConnectStatus(int i) {

        }

        @Override
        public void onGetTagsId(List<com.seuic.sleduhf.EPC> list) {
            StringBuffer strBuffer = new StringBuffer();
            for (com.seuic.sleduhf.EPC item : list) {
                String id = item.getId();
                if (!TextUtils.isEmpty(id)) {
                    strBuffer.append(id).append("\r\n");
                }
            }
            //当有数据返回时. 即通知主线程. 主线程在接收到后停止读取.
            eventBus.post(new RfidReadCodeEvent().setReadCode(strBuffer.toString()));
            eventBus.post(new RfidReadStopEvent());
        }

        @Override
        public void onGetScanKeyMode(int i) {
        }

        @Override
        public void onDecodeComplete(com.seuic.sleduhf.DecodeInfo info) {

        }
    };


    public void registerUhfDevice() {
        uhfDevice= UhfDevice.getInstance(context);
        if (uhfDevice != null) {
            uhfDevice.registerUhfCallback(uhfCallback);
        }
    }

    public void unregisterUhfDevice() {
        if (uhfDevice != null) {
            uhfDevice.unregisterUhfCallback(uhfCallback);
            uhfDevice = null;
        }
    }

    public boolean startSearchTag() {
        return uhfDevice.inventoryStart();
    }

    public boolean stopSearchTag() {
        return uhfDevice.inventoryStop();
    }

    public List<EPC> readTags() {
        return null;
    }
}
