package com.example.connectionble.ble.services;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.connectionble.Activity_BLE;
import com.example.connectionble.ble.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceBLE {
    private final static String TAG = Activity_BLE.class.getSimpleName();
    private static ArrayList<BluetoothGattService> services_ArrayList;
    private static HashMap<String, BluetoothGattCharacteristic> characteristics_HashMap;
    private static HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMapList;

    private static Service_GATT ble_Service;
    private boolean ble_Service_Bound;

    private String name;
    private String address;
    private Activity ble;

    public ServiceBLE(Activity ble, String name, String address) {
        this.name = name;
        this.address = address;
        this.ble = ble;
        services_ArrayList = new ArrayList<>();
        characteristics_HashMap = new HashMap<>();
        characteristics_HashMapList = new HashMap<>();

    }

    private ServiceConnection ble_ServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Service_GATT.BLEServiceBinder binder = (Service_GATT.BLEServiceBinder) service;
            ble_Service = binder.getService();
            ble_Service_Bound = true;

            if (!ble_Service.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                ble.finish();
            }

            ble_Service.connect(address);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ble_Service = null;
            ble_Service_Bound = false;
        }
    };

    public static void updateServices() {
        if (ble_Service != null) {

            services_ArrayList.clear();
            characteristics_HashMap.clear();
            characteristics_HashMapList.clear();

            List<BluetoothGattService> servicesList = ble_Service.getSupportedGattServices();

            for (BluetoothGattService service : servicesList) {

                services_ArrayList.add(service);

                List<BluetoothGattCharacteristic> characteristicsList = service.getCharacteristics();
                ArrayList<BluetoothGattCharacteristic> newCharacteristicsList = new ArrayList<>();

                for (BluetoothGattCharacteristic characteristic : characteristicsList) {
                    characteristics_HashMap.put(characteristic.getUuid().toString(), characteristic);
                    newCharacteristicsList.add(characteristic);
                }

                characteristics_HashMapList.put(service.getUuid().toString(), newCharacteristicsList);
            }

        }
    }
    
     /**
     * @param uuid The uuid of the service
     * @param data The data received on real time from the broadcast (User ID)
     */
    public static void updateCharacteristic(String uuid, String data) {
        //listAdapter.notifyDataSetChanged();
        //YOUR CODE HERE (Use the data which is User Id to get their business card from databases etc.)
    }

    public ServiceConnection getBle_ServiceConnection() {
        return ble_ServiceConnection;
    }

    public void infoHardware() {
        //System.out.println(services_ArrayList);

       /* for (Map.Entry entry : characteristics_HashMap.entrySet())
        {
            System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
        }
        for (Map.Entry entry : characteristics_HashMapList.entrySet())
        {
            //System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
        }*/

        ArrayList<BluetoothGattCharacteristic> array = new ArrayList<>();

        for (String entry : characteristics_HashMapList.keySet()) {
            array = characteristics_HashMapList.get(entry);
            Log.i(TAG, array.toString());

            for (BluetoothGattCharacteristic characteristic : array) {
                byte[] data = characteristic.getValue();
                if (data != null) {
                    System.out.println("Value: " + Utils.hexToString(data));
                    //tv_value.setText("Value: " + Utils.hexToString(data));

                } else {
                    //tv_value.setText("Value: ---");
                    System.out.println("Value: ---");

                }
                if (Utils.hasWriteProperty(characteristic.getProperties()) != 0) {
                    String uuid = characteristic.getUuid().toString();
                    Log.i(TAG, "CAN WRITE.");
                } else if (Utils.hasReadProperty(characteristic.getProperties()) != 0) {
                    if (ble_Service != null) {
                        ble_Service.readCharacteristic(characteristic);
                        Log.i(TAG, "CAN READ.");
                    }
                } else if (Utils.hasNotifyProperty(characteristic.getProperties()) != 0) {
                    if (ble_Service != null) {
                        Log.i(TAG, "CAN NOTIFY.");

                        ble_Service.setCharacteristicNotification(characteristic, true);
                    }
                }
            }

        }


    }

    public void write() {
        ble_Service.sendMessage();
    }
}
