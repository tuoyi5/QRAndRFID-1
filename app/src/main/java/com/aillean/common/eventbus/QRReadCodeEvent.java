package com.aillean.common.eventbus;

import com.aillean.tool.DeviceType;

public class QRReadCodeEvent {
    private DeviceType mDeviceType = DeviceType.QR;
    private String readCode;


    public DeviceType getDeviceType() {
        return mDeviceType;
    }

    public String getReadCode() {
        return readCode;
    }

    public QRReadCodeEvent setReadCode(String readCode) {
        this.readCode = readCode;
        return this;
    }
}
