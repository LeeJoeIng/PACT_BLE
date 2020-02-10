package com.example.connectionble.ble.connection;

import android.bluetooth.BluetoothDevice;

/**
 * Wrapper class of bluetooth device object
 * Used to store the rssi value when the scanner detects the bluetooth device
 */
class BLEDevice {
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BLEDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public int getRSSI() {
        return rssi;
    }
}
