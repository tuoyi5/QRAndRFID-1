package com.aillean.common.action;

import android.content.Context;
import android.util.Log;

import com.aillean.common.eventbus.RecvWSEvent;
import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.manager.DataManager;
import com.aillean.common.request.NetworkDataRequest;
import com.aillean.common.rx.RxCallback;
import com.aillean.tool.DeviceType;
import com.aillean.tool.SoapLoader;

import io.reactivex.annotations.NonNull;

public class NetworkDataAction <T extends DataManager> extends DataAction {

	String ipPort;
	String code;
	DeviceType mDeviceType;

	public NetworkDataAction(Context context, CloudDataManager cloudDataManager) {
		super(context, cloudDataManager);
	}

	public NetworkDataAction setIpPort(String ipPort) {
		this.ipPort = ipPort;
		return this;
	}

	public NetworkDataAction setCode(String code) {
		this.code = code;
		return this;
	}
	public NetworkDataAction setDeviceType(DeviceType deviceType) {
		mDeviceType = deviceType;
		return this;
	}

	public void execute() {
		Log.i("aillean", "QrAction start");
		CloudDataManager mCloudDataManager =(CloudDataManager) getDataManager();

		SoapLoader soapLoader = new SoapLoader();
		soapLoader.setIpPort(ipPort);
		soapLoader.setStrData(code);
		soapLoader.setEmType(mDeviceType);

		final NetworkDataRequest request = new NetworkDataRequest(mCloudDataManager).setSoapLoader(soapLoader);
		mCloudDataManager.submit(getContext(), request, new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				mCloudDataManager.getEventbus().post(new RecvWSEvent().setStrRead(request.getStrRead()));
			}
		});
	}
}
