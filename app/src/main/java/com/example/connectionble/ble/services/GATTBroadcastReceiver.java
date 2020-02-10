package com.example.connectionble.ble.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.connectionble.Activity_BLE;
import com.example.connectionble.ble.Utils;

public class GATTBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = Service_GATT.class.getSimpleName();

    private boolean connected = false;

    private Activity_BLE activity;

    public GATTBroadcastReceiver(Activity_BLE activity) {
        Log.i(TAG, "BROADCAST");

        this.activity = activity;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read or notification operations.

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (Service_GATT.ACTION_GATT_CONNECTED.equals(action)) {
            connected = true;
        } else if (Service_GATT.ACTION_GATT_DISCONNECTED.equals(action)) {
            connected = false;
            Utils.toast(activity.getApplicationContext(), "Disconnected From Device");
            activity.finish();
        } else if (Service_GATT.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            ServiceBLE.updateServices();
        }
        return;
    }

}
