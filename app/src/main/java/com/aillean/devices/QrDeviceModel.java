package com.aillean.devices;

import android.content.Context;

import com.aillean.common.eventbus.NetworkDataActionEvent;
import com.aillean.tool.DeviceType;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;

import org.greenrobot.eventbus.EventBus;

public class QrDeviceModel {

	private Scanner scanner = null;
	private Context context;
	private EventBus eventBus;

	public QrDeviceModel(Context context, EventBus eventBus) {
		this.eventBus = eventBus;
		this.context = context;
	}

	public void registerScannerFactory() {
		scanner = ScannerFactory.getScanner(context);
		scanner.open();
		scanner.setDecodeInfoCallBack(new DecodeInfoCallBack() {

			@Override
			public void onDecodeComplete(DecodeInfo info) {
				String strDecoder = info.barcode;
				eventBus.post(new NetworkDataActionEvent(DeviceType.QR).setReadCode(strDecoder));
				scanner.stopScan();
			}
		});
	}

	public void unregisterScannerFactory() {
		if (scanner != null) {
			scanner.close();
			scanner.setDecodeInfoCallBack(null);
			scanner = null;
		}
	}

	public void startScan() {
		if (scanner != null) {
			scanner.startScan();
		}
	}

	public void stopScan() {
		if (scanner != null) {
			scanner.stopScan();
		}
	}
}
