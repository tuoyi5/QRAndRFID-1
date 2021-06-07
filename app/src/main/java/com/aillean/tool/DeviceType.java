package com.aillean.tool;

public enum DeviceType {
    QR("FindQR") ,RFID("FindRFID");

    private String name;

    private DeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
