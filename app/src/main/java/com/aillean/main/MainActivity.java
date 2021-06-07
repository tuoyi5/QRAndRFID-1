package com.aillean.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aillean.common.action.NetworkTestAction;
import com.aillean.common.eventbus.RecvWSEvent;
import com.aillean.common.model.DataBundle;
import com.aillean.tool.DeviceType;
import com.aillean.utils.EventBusUtils;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.Scanner;
import com.seuic.sleduhf.UhfDevice;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity implements DecodeInfoCallBack {

    private DataBundle mDataBundle;
    private EditText ipPort;
    private TextView mTextView;
    private Button btnRFID, btnQR;

    private Scanner scanner = null;
    private UhfDevice uhfDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initModel();

        scanner = ScannerFactory.getScanner(this);
        scanner.open();
        scanner.setDecodeInfoCallBack(this);

        uhfDevice = UhfDevice.getInstance(this);
      //  uhfDevice.registerUhfCallback(uhfCallback);

        mTextView = this.findViewById(R.id.textView);
        ipPort = this.findViewById(R.id.editTextTextIP);
        btnRFID = this.findViewById(R.id.buttonRFID);
        btnQR = this.findViewById(R.id.buttonQR);

        btnRFID.setOnClickListener(v -> {
            startSoapLoader(DeviceType.RFID);
            Log.i("widgetDemo", "button1 被用户点击了。");
        });

        btnQR.setOnClickListener(v -> {
            startSoapLoader(DeviceType.QR);
            Log.i("widgetDemo", "button1 被用户点击了。");
        });

        registerEventBus();
    }

    @Override
    public void onDecodeComplete(final DecodeInfo decodeInfo) {
        String strDecoder = decodeInfo.barcode;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();

        if (scanner != null) {
            scanner.close();
            scanner.setDecodeInfoCallBack(null);
            scanner = null;
        }
    }


    public void initModel() {
        mDataBundle = new DataBundle(getApplicationContext());
    }

    private void registerEventBus() {
        EventBusUtils.ensureRegister(mDataBundle.getEventBus(), this);
    }

    private void unregisterEventBus() {
        EventBusUtils.ensureUnregister(mDataBundle.getEventBus(), this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecvWSEvent(RecvWSEvent recvWSEvent) {
        Log.i("aillean", "MainActivity: " + recvWSEvent.getStrRead());
        udpateTextView(recvWSEvent.getStrRead());
    }

    private void startSoapLoader(DeviceType emType) {
        NetworkTestAction networkTestAction =
                new NetworkTestAction(getApplicationContext(), mDataBundle.getCloudDataManager());
        networkTestAction.setIpPort(ipPort.getText().toString());
        networkTestAction.setEmType(emType);

        if(emType==DeviceType.RFID)
        {
            networkTestAction.setStrData("QR1");
        }
        if(emType==DeviceType.QR)
        {
            networkTestAction.setStrData("aa");
        }

        networkTestAction.execute();
    }

    public void udpateTextView(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        mTextView.setText(value);
    }
}