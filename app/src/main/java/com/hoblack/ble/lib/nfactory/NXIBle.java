package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothGattCharacteristic;

public interface NXIBle {

    boolean requestConnect(String address);

    boolean requestDiscoveryService(String address);

    boolean requestReadCharacteristic(String address, BluetoothGattCharacteristic characteristic);

    boolean requestCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic);

    boolean requestStopNotification(String address, BluetoothGattCharacteristic characteristic);

    boolean requestCharacteristicIndication(String address, BluetoothGattCharacteristic characteristic);

    boolean requestWriteCharacteristic(String address, BluetoothGattCharacteristic characteristic, String remark);

    boolean requestReadCharacteristic(String address, String serviceUUID, String readUUID);

    boolean requestSetCharacteristicNotification(String address, String serviceUUID, String notifyUUID, boolean enabled);

    boolean requestSetCharacteristicIndication(String address, String serviceUUID, String notifyUUID, boolean enabled);

    boolean requestWriteCharacteristic(String address, String serviceUUID, String writeUUID, byte[] data);

    void doOperationEnd(String address);


}


