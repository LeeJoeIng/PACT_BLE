package com.example.connectionble.ble.connection;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.Button;

import com.example.connectionble.Activity_BLE;
import com.example.connectionble.MActivity;
import com.example.connectionble.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionBLE {

    private Button scanButton;
    private BLEScanner bleScanner;
    private MActivity scanActivity;
    private HashMap<String, BLEDevice> devicesHashMap;
    private ArrayList<BLEDevice> devicesArrayList;

    public ConnectionBLE(MActivity scanActivity, Button scanButton) {
        this.scanActivity = scanActivity;
        this.scanButton = scanButton;
        devicesHashMap = new HashMap<>();
        devicesArrayList = new ArrayList<>();
        bleScanner = new BLEScanner(this, 7000, -75);  //signal strength higher number stronger signal

    }

    public MActivity getScanActivity() {
        return scanActivity;
    }

    public void pairing() {
        if (!bleScanner.isScanning()) {
            startScan();
        } else {
            stopScan();
        }
    }

    public void startScan() {
        scanButton.setText(scanActivity.getString(R.string.scanning));
        devicesArrayList.clear();
        devicesHashMap.clear();
        bleScanner.start();

    }

    public void stopScan() {
        scanButton.setText(scanActivity.getString(R.string.rescan));
        bleScanner.stop();
        for (BLEDevice device : devicesArrayList) {
            if (device.getName() != null && device.getName().length() > 0) {
                if ((device.getName()).equals("DrinkConnected")) {
                    //System.out.println(device.getName());
                    bleService(device.getName(), device.getAddress());

                } else {
                    startScan();
                }
            }

        }
    }

    private void bleService(String deviceName, String deviceAddress) {
        Intent intent = new Intent(scanActivity, Activity_BLE.class);
        intent.putExtra(Activity_BLE.EXTRA_NAME, deviceName);
        intent.putExtra(Activity_BLE.EXTRA_ADDRESS, deviceAddress);
        scanActivity.startActivityForResult(intent, scanActivity.BLE_SERVICES);
        //Activity_Services is the UI that will be shown up after pairing
    }

    /**
     * Prevents same device to be added multiple times
     *
     * @param device
     * @param rssi
     */
    public void addDevice(BluetoothDevice device, int rssi) {

        String address = device.getAddress();
        if (!devicesHashMap.containsKey(address)) {
            BLEDevice bleDevice = new BLEDevice(device);
            bleDevice.setRSSI(rssi);

            devicesHashMap.put(address, bleDevice);
            devicesArrayList.add(bleDevice);
        } else {
            devicesHashMap.get(address).setRSSI(rssi);
        }

    }


}
