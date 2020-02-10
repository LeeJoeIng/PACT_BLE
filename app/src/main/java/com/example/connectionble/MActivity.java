package com.example.connectionble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.connectionble.ble.connection.BLEStateUpdateReceiver;
import com.example.connectionble.ble.connection.ConnectionBLE;
import com.example.connectionble.ble.Utils;

public class MActivity extends AppCompatActivity {
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int BLE_SERVICES = 2;
    private ConnectionBLE connectionBLE;
    private BLEStateUpdateReceiver bleStateUpdateReceiver;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        connectionBLE = new ConnectionBLE(this,button);
        bleStateUpdateReceiver = new BLEStateUpdateReceiver(this);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getApplicationContext(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectionBLE.pairing();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(bleStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionBLE.stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(bleStateUpdateReceiver);
        connectionBLE.stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), getString(R.string.info_ble));
            }
        } else if (requestCode == BLE_SERVICES) {
            Utils.toast(getApplicationContext(), getString(R.string.pairing));
        }
    }
}


