package com.aillean.common.eventbus;

import com.aillean.tool.DeviceType;

public class RfidReadCodeEvent {
    private String readCode;
    private DeviceType deviceType = DeviceType.RFID;

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getReadCode() {
        return readCode;
    }

    public RfidReadCodeEvent setReadCode(String readCode) {
        this.readCode = readCode;
        return this;
    }
}
