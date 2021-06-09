package com.aillean.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aillean.common.eventbus.QRReadCodeEvent;
import com.aillean.common.eventbus.RecvWSEvent;
import com.aillean.common.eventbus.RfidReadCodeEvent;
import com.aillean.common.model.DataBundle;
import com.aillean.tool.DeviceType;
import com.aillean.utils.EventBusUtils;
import com.seuic.scankey.IKeyEventCallback;
import com.seuic.scankey.ScanKeyService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {

    private DataBundle mDataBundle;
    private EditText ipPort;
    private TextView mTextView;
    private RadioButton btnRFID, btnQR;
    private RadioGroup mRadioGroup;
    private ScanKeyService mScanKeyService = ScanKeyService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanKeyService.registerCallback(mCallback, "248,249,250");

        mTextView = this.findViewById(R.id.textView);
        ipPort = this.findViewById(R.id.editTextTextIP);
        btnRFID = this.findViewById(R.id.buttonRFID);
        btnQR = this.findViewById(R.id.buttonQR);
        mRadioGroup = this.findViewById(R.id.radio_group);

        btnRFID.setOnClickListener(v -> {
            mDataBundle.setDeviceType(DeviceType.RFID);
        });

        btnQR.setOnClickListener(v -> {
            mDataBundle.setDeviceType(DeviceType.QR);
        });

        registerModel();
        registerEventBus();
    }

    public void get(View view) {
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) mRadioGroup.getChildAt(i);
            if (button.isChecked()) {
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        mScanKeyService.unregisterCallback(mCallback);

        super.onDestroy();
        unregisterEventBus();
        unregisterModel();

    }

    private IKeyEventCallback mCallback = new IKeyEventCallback.Stub() {
        @Override
        public void onKeyDown(int keyCode) throws RemoteException {
            mDataBundle.onKeyDown(keyCode);
        }

        @Override
        public void onKeyUp(int keyCode) throws RemoteException {
            mDataBundle.onKeyUp(keyCode);
        }
    };

    public void registerModel() {
        mDataBundle = new DataBundle(this);
    }

    public void unregisterModel() {
        if (mDataBundle != null) {
            mDataBundle.unregister();
        }
    }

    private void registerEventBus() {
        EventBusUtils.ensureRegister(mDataBundle.getEventBus(), this);
        EventBusUtils.ensureRegister(EventBus.getDefault(), this);
    }

    private void unregisterEventBus() {
        EventBusUtils.ensureUnregister(mDataBundle.getEventBus(), this);
        EventBusUtils.ensureUnregister(EventBus.getDefault(), this);
    }


    public void udpateTextView(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        mTextView.setText(value);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecvWSEvent(RecvWSEvent recvWSEvent) {
        Log.i("aillean", "MainActivity: onRecvWSEvent: " + recvWSEvent.getStrRead());
        udpateTextView(recvWSEvent.getStrRead());
    }

    //rfid与 qr码向服务端请求数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkDataActionEvent(QRReadCodeEvent event) {
        Log.i("aillean", "MainActivity: NetworkDataActionEvent");
        if(event.getReadCode().isEmpty())
        {
            udpateTextView("Not Read QR Data");
        }else
        {
            udpateTextView(event.getReadCode());

            mDataBundle.startNetworkDataAction(ipPort.getText().toString(),event.getReadCode(), event.getDeviceType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecvWSEvent(RfidReadCodeEvent event) {
        Log.i("aillean", "MainActivity: onRecvWSEvent: " + event.getReadCode());
        if(event.getReadCode().isEmpty())
        {
            udpateTextView("Not Read RFID Data");
        }else {
            udpateTextView(event.getReadCode());

            mDataBundle.startNetworkDataAction(ipPort.getText().toString(), event.getReadCode(), event.getDeviceType());
        }
    }

}