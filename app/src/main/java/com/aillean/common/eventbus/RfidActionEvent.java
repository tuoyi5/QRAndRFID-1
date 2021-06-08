package com.aillean.common.eventbus;

import com.aillean.devices.RfidDeviceModel;
import com.aillean.devices.rfid.utils.RfidState;

import java.util.ArrayList;
import java.util.List;

public class RfidActionEvent {

	RfidDeviceModel rfidDeviceModel;
	RfidState rfidState;


	public RfidActionEvent(RfidState rfidState) {
		this.rfidState = rfidState;
	}

	public RfidDeviceModel getRfidDeviceModel() {
		return rfidDeviceModel;
	}

	public void setRfidDeviceModel(final RfidDeviceModel rfidDeviceModel) {
		this.rfidDeviceModel = rfidDeviceModel;
	}

	public RfidState getRfidState() {
		return rfidState;
	}

	public void setRfidState(RfidState rfidState) {
		this.rfidState = rfidState;
	}
}
