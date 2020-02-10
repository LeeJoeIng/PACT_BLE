package com.example.connectionble.ble.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import com.example.connectionble.ble.Utils;

public class BLEScanner {

    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;
    private boolean scanning;

    private long scanPeriod;
    private int signalStrength;
    private ConnectionBLE connectionBLE;

    public BLEScanner(ConnectionBLE connectionBLE, long scanPeriod, int signalStrength) {
        this.connectionBLE = connectionBLE;

        handler = new Handler();

        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager) connectionBLE.getScanActivity().getSystemService(Context.BLUETOOTH_SERVICE);

        //bluetooth module on phone
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning() {
        return scanning;
    }

    public void start() {
        if (!Utils.checkBluetooth(bluetoothAdapter)) {
            Utils.requestUserBluetooth(connectionBLE.getScanActivity());
            connectionBLE.stopScan();
        } else {
            scanDevice(true);
        }
    }

    public void stop() {
        scanDevice(false);
    }

    private void scanDevice(final boolean enable) {
        if (enable && !scanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(scanCallback);
                    connectionBLE.stopScan();

                }
            }, scanPeriod);

            scanning = true;
            bluetoothAdapter.startLeScan(scanCallback);
        } else {
            scanning = false;
            bluetoothAdapter.stopLeScan(scanCallback);
        }
    }

    // Device scan callback.
    //be executed by a thread
    private BluetoothAdapter.LeScanCallback scanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    final int new_rssi = rssi;
                    if (rssi > signalStrength) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                connectionBLE.addDevice(device, new_rssi);
                            }
                        });
                    }
                }
            };
}
