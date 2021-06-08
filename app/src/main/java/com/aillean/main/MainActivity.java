package com.aillean.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aillean.common.eventbus.NetworkDataActionEvent;
import com.aillean.common.eventbus.RecvWSEvent;
import com.aillean.common.eventbus.RfidActionEvent;
import com.aillean.common.eventbus.RfidConnectInfoEvent;
import com.aillean.common.eventbus.RfidTagEvent;
import com.aillean.common.model.DataBundle;
import com.aillean.tool.DeviceType;
import com.aillean.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity{

    private DataBundle mDataBundle;
    private EditText ipPort;
    private TextView mTextView;
    private RadioButton btnRFID, btnQR;
    private RadioGroup mRadioGroup;
    private Button testButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = this.findViewById(R.id.textView);
        ipPort = this.findViewById(R.id.editTextTextIP);
        btnRFID = this.findViewById(R.id.buttonRFID);
        btnQR = this.findViewById(R.id.buttonQR);
        testButton = this.findViewById(R.id.testButton);
        mRadioGroup = this.findViewById(R.id.radio_group);

        btnRFID.setOnClickListener(v -> {
            mDataBundle.setIpProt(ipPort.getText().toString());
            mDataBundle.setDeviceType(DeviceType.RFID);
        });

        btnQR.setOnClickListener(v -> {
            mDataBundle.setIpProt(ipPort.getText().toString());
            mDataBundle.setDeviceType(DeviceType.QR);
        });


        testButton.setOnClickListener( v -> {
            //测试rfid流程的入口
            mDataBundle.startQrOrRfid();
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
        super.onDestroy();
        unregisterEventBus();
        unregisterModel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //影响扫码
        return mDataBundle.onKeyDown(keyCode, event) ? true : super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //影响扫码
        return  mDataBundle.onKeyUp(keyCode, event) ? true : super.onKeyUp(keyCode, event);
    }

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

    //显示rfid的相关提示信息.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRfidConnectInfoEvent(RfidConnectInfoEvent event) {
        Log.i("aillean", "MainActivity: RfidConnectInfoEvent: " + event.getMessage());
        udpateTextView("rfid,请求返回: " + event.getMessage());
    }

    //获取到rfid的tag后,准备读取数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRfidTagEvent(RfidTagEvent event) {
        Log.i("aillean", "MainActivity: onRfidTagEvent: " + event.getRfidTag());
        udpateTextView("当前的rfid tag: " + event.getRfidTag());
    }

    //Rfid事件请求, 打开,读,写等.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRfidActionEvent(RfidActionEvent event) {
        Log.i("aillean", "MainActivity: RfidActionEvent");
        mDataBundle.startRfidAction(mDataBundle.getRfidDeviceModel(), event.getRfidState());
    }

    //rfid与 qr码向服务端请求数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkDataActionEvent(NetworkDataActionEvent event) {
        Log.i("aillean", "MainActivity: NetworkDataActionEvent");
        mDataBundle.startNetworkDataAction(event.getReadCode(), event.getDeviceType());
    }

}