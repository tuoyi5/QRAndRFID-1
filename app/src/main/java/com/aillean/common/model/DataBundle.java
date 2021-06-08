package com.aillean.common.model;

import android.content.Context;
import android.view.KeyEvent;

import com.aillean.common.action.NetworkDataAction;
import com.aillean.common.action.QrAction;
import com.aillean.common.action.RfidAction;
import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.manager.LocalDataManager;
import com.aillean.devices.QrDeviceModel;
import com.aillean.devices.RfidDeviceModel;
import com.aillean.devices.rfid.utils.RfidState;
import com.aillean.tool.DeviceType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class DataBundle {

	private Context appContext;
	private CloudDataManager mCloudDataManager;
	private LocalDataManager mLocalDataManager;
	private EventBus eventBus;
	private String ipProt;

	private QrDeviceModel mQrDeviceModel;
	private RfidDeviceModel mRfidDeviceModel;

	private ArrayList<Integer> entityKeys = new ArrayList<>();

	public DataBundle(Context appContext) {
		this.appContext = appContext;
		eventBus = new EventBus();
		mCloudDataManager = new CloudDataManager(eventBus);
		mLocalDataManager = new LocalDataManager(eventBus);
		mQrDeviceModel = new QrDeviceModel(appContext, eventBus);
		mRfidDeviceModel = new RfidDeviceModel(appContext, eventBus);
		register();
		initEntityKeys();
	}

	public void initEntityKeys() {
		entityKeys.add(249);
		entityKeys.add(250);
	}

	public Context getAppContext() {
		return appContext;
	}

	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

	public CloudDataManager getCloudDataManager() {
		return mCloudDataManager;
	}

	public void setCloudDataManager(CloudDataManager cloudDataManager) {
		mCloudDataManager = cloudDataManager;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public String getIpProt() {
		return ipProt;
	}

	public void setIpProt(String ipProt) {
		this.ipProt = ipProt;
	}

	public void startQrAction(String qrCode) {
		QrAction action = new QrAction(appContext, mLocalDataManager);
		action.execute();
	}

	public void startRfidAction(RfidDeviceModel rfidDeviceModel, RfidState rfidState) {
		RfidAction action = new RfidAction<>(appContext, mLocalDataManager);
		action.setRfidDeviceModel(rfidDeviceModel).setRfidState(rfidState);
		action.execute();
	}

	public void startNetworkDataAction(String code, DeviceType type) {
		NetworkDataAction action = new NetworkDataAction<>(appContext, mCloudDataManager)
			.setCode(code).setDeviceType(type).setIpPort(ipProt);
		action.execute();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!entityKeys.contains(keyCode)) {
			return false;
		}
		mQrDeviceModel.startScan();
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (!entityKeys.contains(keyCode)) {
			return false;
		}

		mQrDeviceModel.stopScan();
		return true;
	}

	public void register() {
		mQrDeviceModel.registerScannerFactory();
		mRfidDeviceModel.init();
	}

	public void unregister() {
		mQrDeviceModel.unregisterScannerFactory();
		mRfidDeviceModel.unregisterUhfDeviceCallback();
	}

	public QrDeviceModel getQrDeviceModel() {
		return mQrDeviceModel;
	}

	public void setQrDeviceModel(QrDeviceModel qrDeviceModel) {
		mQrDeviceModel = qrDeviceModel;
	}

	public RfidDeviceModel getRfidDeviceModel() {
		return mRfidDeviceModel;
	}

	public void setRfidDeviceModel(RfidDeviceModel rfidDeviceModel) {
		mRfidDeviceModel = rfidDeviceModel;
	}
}
