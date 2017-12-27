package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothGattCharacteristic;

public interface NXIBleRequestHandler {

    boolean readCharacteristic(String address, BluetoothGattCharacteristic characteristic);

    boolean characteristicNotification(String address, BluetoothGattCharacteristic c);

    boolean writeCharacteristic(String address, BluetoothGattCharacteristic characteristic);
}


