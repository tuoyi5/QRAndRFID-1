package com.aillean.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.aillean.common.eventbus.QRReadCodeEvent;
import com.seuic.scanner.Scanner;

import org.greenrobot.eventbus.EventBus;

public class QrDeviceModel {

    private String SCANACTION = "com.android.server.scannerservice.aillean.broadcast";//定义广播名

    private Scanner scanner = null;
    private Context context;
    private EventBus eventBus;

    public QrDeviceModel(Context context, EventBus eventBus) {
        this.eventBus = eventBus;
        this.context = context;

        BoardCastSetting();
    }

    //修改扫描工具参数：广播名、条码发送方式、声音、震动等
    private void BoardCastSetting() {
        Intent intent = new Intent("com.android.scanner.service_settings");
        //修改广播名称：修改扫描工具广播名，接收广播时也是这个广播名
        intent.putExtra("action_barcode_broadcast", SCANACTION);
        //条码发送方式：广播；其他设置看文档
        intent.putExtra("barcode_send_mode", "BROADCAST");
        //键值，一般不改
        intent.putExtra("key_barcode_broadcast", "scannerdata");
        //声音
        intent.putExtra("sound_play", true);
        //震动
        intent.putExtra("viberate", true);
        //扫描间隔
        intent.putExtra("interval", 1000);
        //循环扫描
        intent.putExtra("scan_continue", false);
        //过滤不可见字符
        intent.putExtra("filter_invisible_chars ", true);
        //抬起停止扫描
        intent.putExtra("keyup_to_stop_scan ", false);
        //其他参数设置参照：Android扫描服务设置.doc
        context.sendBroadcast(intent);
    }

    public void registerScannerFactory(Context context) {
        IntentFilter intentFilter = new IntentFilter(SCANACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        context.registerReceiver(scanReceiver, intentFilter);

        Intent intentEnable = new Intent("com.android.scanner.ENABLED");
        intentEnable.putExtra("enabled", true);
        this.context.sendBroadcast(intentEnable);
    }

    BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SCANACTION)) {
                String code = intent.getStringExtra("scannerdata");
                code=code.replace('\n',' ');
                code= code.trim();
                eventBus.post(new QRReadCodeEvent().setReadCode(code));
            }
        }
    };


    public void unregisterScannerFactory(Context context) {
        Intent intentDisable = new Intent("com.android.scanner.ENABLED");
        intentDisable.putExtra("enabled", false);
        this.context.sendBroadcast(intentDisable);

        context.unregisterReceiver(scanReceiver);
    }

    public void startScan() {
        Intent intentStart = new Intent("com.scan.onStartScan");
        this.context.sendBroadcast(intentStart);
    }

    public void stopScan() {
        Intent intentEnd = new Intent("com.scan.onEndScan");
        this.context.sendBroadcast(intentEnd);
    }
}
