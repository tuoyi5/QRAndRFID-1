package com.aillean.common.eventbus;

import com.aillean.tool.DeviceType;

public class NetworkDataActionEvent {
	DeviceType mDeviceType;
	String readCode;

	public NetworkDataActionEvent(DeviceType mDeviceType) {
		this.mDeviceType = mDeviceType;
	}

	public DeviceType getDeviceType() {
		return mDeviceType;
	}

	public NetworkDataActionEvent setDeviceType(DeviceType deviceType) {
		mDeviceType = deviceType;
		return this;
	}

	public String getReadCode() {
		return readCode;
	}

	public NetworkDataActionEvent setReadCode(String readCode) {
		this.readCode = readCode;
		return this;
	}
}
