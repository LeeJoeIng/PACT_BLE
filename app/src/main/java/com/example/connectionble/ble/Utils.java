package com.example.connectionble.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.widget.Toast;

import com.example.connectionble.MActivity;
import com.example.connectionble.ble.services.Service_GATT;

public class Utils {
    /**
     * Ensures Bluetooth is available on the device and it is enabled. If not, displays a dialog requesting user permission to enable Bluetooth.
     *
     * @param bluetoothAdapter
     * @return true if bluetooth is activated.
     */
    public static boolean checkBluetooth(BluetoothAdapter bluetoothAdapter) {

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestUserBluetooth(Activity activity) {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBluetooth, MActivity.REQUEST_ENABLE_BT);
    }

    public static void toast(Context context, String string) {

        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Service_GATT.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Service_GATT.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(Service_GATT.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(Service_GATT.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }

    public static String hexToString(byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length);

        for (byte byteChar : data) {
            sb.append(String.format("%02X ", byteChar)); //todo why 02X
        }

        return sb.toString();
    }

    public static int hasWriteProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_WRITE;
    }

    public static int hasReadProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_READ;
    }

    public static int hasNotifyProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_NOTIFY;
    }
}
