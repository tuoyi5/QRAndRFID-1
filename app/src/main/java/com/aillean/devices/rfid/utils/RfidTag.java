package com.aillean.devices.rfid.utils;

public class RfidTag {
    private String id;
    private int memoryRegion;
    private String accessPwd;
    private String killPwd;
    private int offset;
    private int length;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMemoryRegion() {
        return memoryRegion;
    }

    public void setMemoryRegion(int memoryRegion) {
        this.memoryRegion = memoryRegion;
    }

    public String getAccessPwd() {
        return accessPwd == null ? "00000000" : accessPwd;
    }

    public void setAccessPwd(String accessPwd) {
        this.accessPwd = accessPwd;
    }

    public String getKillPwd() {
        return killPwd == null ? "" : killPwd;
    }

    public void setKillPwd(String killPwd) {
        this.killPwd = killPwd;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
