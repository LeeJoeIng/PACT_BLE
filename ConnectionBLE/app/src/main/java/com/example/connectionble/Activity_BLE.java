package com.example.connectionble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.connectionble.ble.Utils;
import com.example.connectionble.ble.services.GATTBroadcastReceiver;
import com.example.connectionble.ble.services.ServiceBLE;
import com.example.connectionble.ble.services.Service_GATT;

public class Activity_BLE extends AppCompatActivity {
    private final static String TAG = Activity_BLE.class.getSimpleName();
    public static final String EXTRA_NAME = "com.example.connectionble.Activity_BLE.NAME";
    public static final String EXTRA_ADDRESS = "com.example.ble.Activity_Services.ADDRESS";

    private ServiceBLE serviceBLE;
    private GATTBroadcastReceiver gattUpdateReceiver;
    private Button button2;
    private Intent ble_Service_Intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        button2 = findViewById(R.id.button2);

        Intent intent = getIntent();
        String name = intent.getStringExtra(Activity_BLE.EXTRA_NAME);
        String address = intent.getStringExtra(Activity_BLE.EXTRA_ADDRESS);
        serviceBLE = new ServiceBLE(this, name, address);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                serviceBLE.write();
                //serviceBLE.infoHardware();
                //todo add second button to read
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        gattUpdateReceiver = new GATTBroadcastReceiver(this);
        registerReceiver(gattUpdateReceiver, Utils.makeGattUpdateIntentFilter());

        ble_Service_Intent = new Intent(this, Service_GATT.class);
        bindService(ble_Service_Intent, serviceBLE.getBle_ServiceConnection(), Context.BIND_AUTO_CREATE);
        startService(ble_Service_Intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(gattUpdateReceiver);
        unbindService(serviceBLE.getBle_ServiceConnection());
        ble_Service_Intent = null;
    }
}
