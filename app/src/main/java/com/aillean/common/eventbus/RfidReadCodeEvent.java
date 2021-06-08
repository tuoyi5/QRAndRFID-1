package com.aillean.common.eventbus;

public class RfidReadCodeEvent {
	String readCode;
	public RfidReadCodeEvent() {

	}

	public String getReadCode() {
		return readCode;
	}

	public RfidReadCodeEvent setReadCode(String readCode) {
		this.readCode = readCode;
		return this;
	}
}
