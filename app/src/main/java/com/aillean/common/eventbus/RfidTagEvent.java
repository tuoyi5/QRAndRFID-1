package com.aillean.common.eventbus;

public class RfidTagEvent {

	String rfidTag;
	public RfidTagEvent() {

	}

	public String getRfidTag() {
		return rfidTag;
	}

	public RfidTagEvent setRfidTag(String rfidTag) {
		this.rfidTag = rfidTag;
		return this;
	}
}
