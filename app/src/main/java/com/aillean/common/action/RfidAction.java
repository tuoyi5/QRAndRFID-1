package com.aillean.common.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.aillean.common.eventbus.NetworkDataActionEvent;
import com.aillean.common.eventbus.RfidActionEvent;
import com.aillean.common.eventbus.RfidConnectInfoEvent;
import com.aillean.common.eventbus.RfidReadCodeEvent;
import com.aillean.common.eventbus.RfidTagEvent;
import com.aillean.common.manager.DataManager;
import com.aillean.common.manager.LocalDataManager;
import com.aillean.common.request.RfidRequest;
import com.aillean.common.rx.RxCallback;
import com.aillean.devices.RfidDeviceModel;
import com.aillean.devices.rfid.utils.RfidState;
import com.aillean.tool.DeviceType;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;

public
class RfidAction<T extends DataManager> extends DataAction {

	RfidDeviceModel mRfidDeviceModel;
	RfidState mRfidState;

	public RfidAction(Context context, LocalDataManager dataManager) {
		super(context, dataManager);
	}

	public RfidDeviceModel getRfidDeviceModel() {
		return mRfidDeviceModel;
	}

	public RfidAction setRfidDeviceModel(RfidDeviceModel rfidDeviceModel) {
		mRfidDeviceModel = rfidDeviceModel;
		return this;
	}

	public RfidState getRfidState() {
		return mRfidState;
	}

	public RfidAction setRfidState(RfidState rfidState) {
		mRfidState = rfidState;
		return this;
	}

	public void execute() {
		Log.i("aillean", "RfidaAction start");
		LocalDataManager localDataManager = (LocalDataManager) getDataManager();

		RfidRequest request = new RfidRequest(localDataManager)
			.setRfidDeviceModel(mRfidDeviceModel)
			.setRfidState(mRfidState);

		getDataManager().submit(getContext(), request, new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				switch (mRfidState) {

				}
			}
		});
	}
}
