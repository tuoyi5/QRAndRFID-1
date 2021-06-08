package com.aillean.common.eventbus;

import java.util.ArrayList;
import java.util.List;

public class RfidConnectInfoEvent {

	private int deviceState;
	private String message;

	public RfidConnectInfoEvent() {
	}

	public String getMessage() {
		return message;
	}

	public RfidConnectInfoEvent setMessage(String message) {
		this.message = message;
		return this;
	}

	public int getDeviceState() {
		return deviceState;
	}

	public RfidConnectInfoEvent setDeviceState(int deviceState) {
		this.deviceState = deviceState;
		return this;
	}

}
