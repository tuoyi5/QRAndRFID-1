package com.aillean.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aillean.tool.SoapLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView view = this.findViewById(R.id.textView);
        EditText ipPort= this.findViewById(R.id.editTextTextIP);

        Button btnRFID =   this.findViewById(R.id.buttonRFID);
        btnRFID.setOnClickListener(v -> {
            //TODO Auto-generated method stub
            view.setText(new SoapLoader().Loader(ipPort.getText().toString()));
            Log.i("widgetDemo", "button1 被用户点击了。");
        });

        Button btnQR =   this.findViewById(R.id.buttonQR);
        btnQR.setOnClickListener(v -> {
            //TODO Auto-generated method stub
            view.setText(ipPort.getText());
            Log.i("widgetDemo", "button1 被用户点击了。");
        });
    }
}