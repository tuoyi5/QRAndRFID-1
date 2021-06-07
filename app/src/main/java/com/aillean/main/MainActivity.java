package com.aillean.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aillean.common.action.NetworkTestAction;
import com.aillean.common.eventbus.TestEvent;
import com.aillean.common.eventbus.TestTwoEvent;
import com.aillean.common.model.DataBundle;
import com.aillean.tool.SoapLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    DataBundle mDataBundle;
    EditText ipPort;
    TextView mTextView;
    Button btnRFID,btnQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initModel();

        mTextView = this.findViewById(R.id.textView);
        ipPort= this.findViewById(R.id.editTextTextIP);
        btnRFID =   this.findViewById(R.id.buttonRFID);
        btnQR =  this.findViewById(R.id.buttonQR);

        btnRFID.setOnClickListener(v -> {
            startSoapLoader();
            Log.i("widgetDemo", "button1 被用户点击了。");
        });

        btnQR.setOnClickListener(v -> {
            Log.i("widgetDemo", "button1 被用户点击了。");
        });
    }

    public void initModel() {
        mDataBundle = new DataBundle(getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(TestEvent testEvent) {
        Log.i("aillean", "MainActivity: " + testEvent.getTestString());
        mTextView.setText(testEvent.getTestString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestTwoEvent(TestTwoEvent testEvent) {
        Log.i("aillean", "MainActivity: " + testEvent.getTestString());
        mTextView.setText(testEvent.getTestString());
    }

    private void startSoapLoader() {
        NetworkTestAction networkTestAction =
            new NetworkTestAction(getApplicationContext(), mDataBundle.getCloudDataManager());
        networkTestAction.setIpPort(ipPort.getText().toString());
        networkTestAction.execute();
    }
}