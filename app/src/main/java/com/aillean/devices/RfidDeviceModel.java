package com.aillean.devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.aillean.common.eventbus.NetworkDataActionEvent;
import com.aillean.common.eventbus.RfidActionEvent;
import com.aillean.common.eventbus.RfidConnectInfoEvent;
import com.aillean.devices.rfid.utils.RfidImpl;
import com.aillean.devices.rfid.utils.RfidState;
import com.aillean.devices.rfid.utils.RfidTag;
import com.aillean.tool.DeviceType;
import com.seuic.sleduhf.EPC;
import com.seuic.sleduhf.UhfCallback;
import com.seuic.sleduhf.UhfDevice;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RfidDeviceModel {

	private static final String TAG = RfidDeviceModel.class.getSimpleName();
	private Context context;
	private EventBus eventBus;
	private UhfDevice uhfDevice = null;
	private String btMc;

	private List<String> deviceIdList=new ArrayList<>();

	public RfidDeviceModel(Context context, EventBus eventBus) {
		this.context = context;
		this.eventBus = eventBus;
	}

	public void registerUhfDeviceCallback() {
		uhfDevice = UhfDevice.getInstance(context);
		uhfDevice.registerUhfCallback(uhfCallback);
	}

	public void unregisterUhfDeviceCallback() {
		if (uhfDevice != null) {
			uhfDevice.unregisterUhfCallback(uhfCallback);
		}
	}

	UhfCallback uhfCallback = new UhfCallback() {
		@Override
		public void onGetBtInfo(BluetoothDevice device) {

		}

		@Override
		public void onGetConnectStatus(int i) {
			String value = null;
			switch (i){
				case 0:
					value = "onGetConnectStatus: 连接成功";
					break;
				case -1:
					value = "onGetConnectStatus: 断开连接";
					break;
				case -2:
					value = "onGetConnectStatus: 连接超时";
					break;
			}
			eventBus.post(new RfidConnectInfoEvent().setDeviceState(i).setMessage(value));
		}

		@Override
		public void onGetTagsId(List<EPC> list) {
			deviceIdList.clear();
			for (EPC epc:list) {
				deviceIdList.add(epc.getId());
			}
			eventBus.post(new RfidActionEvent(RfidState.READ));
		}

		@Override
		public void onGetScanKeyMode(int i) {

		}

		@Override
		public void onDecodeComplete(com.seuic.sleduhf.DecodeInfo info) {
			String strDecoder = info.barcode;
			eventBus.post(new NetworkDataActionEvent(DeviceType.RFID).setReadCode(strDecoder));
		}
	};

	//需要通过binder连接蓝牙服务,故需要主线程中进行.
	public void init( ){
		registerUhfDeviceCallback();

		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!btAdapter.isEnabled()) {
			eventBus.post(new RfidConnectInfoEvent().setMessage("蓝牙未启用"));
		}

		Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
		if (devices.size() > 0 ) {
			for (BluetoothDevice dev : devices) {
				Log.i(TAG, "init: bluetooth address:" + dev.getAddress());

				if (dev.getAddress().startsWith("00:15")) {
					btMc = dev.getAddress();
					uhfDevice.connect(btMc,5000);
					break;
				}
			}
		}else {
			eventBus.post(new RfidConnectInfoEvent().setMessage("未找到已连接配对设备"));
		}
	}

	public String open() {
		btMc = RfidImpl.open(uhfDevice);
		return btMc;
	}

	public void close() {
		unregisterUhfDeviceCallback();
		btMc = null;
	}

	public boolean startSearchTag() {
		return RfidImpl.startSearchTag(uhfDevice);
	}

	public boolean stopSearchTag() {
		return RfidImpl.stopSearchTag(uhfDevice);
	}

	public String readTag() {
		RfidTag tag = new RfidTag();
		tag.setMemoryRegion(3);
		tag.setOffset(0);
		tag.setLength(16);
		tag.setAccessPwd("00000000");
		for (String id : deviceIdList) {
			//tag.setId("6400911151342508661905301000010100739951400000020501");
			tag.setId(id);
			return RfidImpl.read(uhfDevice, tag);
		}
		return null;
	}

	public boolean writeTag() {
		RfidTag tag=new RfidTag();
		tag.setMemoryRegion(3);
		tag.setOffset(0);
		tag.setLength(2);
		tag.setAccessPwd("00000000");
		for (String id : deviceIdList) {
			//tag.setId("6400911151342508661905301000010100739951400000020501");
			tag.setId(id);
			return RfidImpl.write(uhfDevice, tag, "3132");
		}
		return false;
	}

}
