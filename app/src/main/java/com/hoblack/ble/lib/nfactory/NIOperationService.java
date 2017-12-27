package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothGattService;

import java.util.List;
import java.util.UUID;

public interface NIOperationService extends NXIBle {

    void startScan();

    void stopScan();

    void disconnect(String address);

    void close(String address);

    boolean connect(String address);

    boolean discoverServices(String address);

    List<BluetoothGattService> getServices(String address);

    BluetoothGattService getService(String address, UUID uuid);

    boolean readCharacteristic(String address, String serviceUUID, String readUUID);

    boolean setCharacteristicNotification(String address, String serviceUUID, String notifyUUID, boolean enabled);

    boolean setCharacteristicIndication(String address, String serviceUUID, String notifyUUID, boolean enabled);

    boolean writeCharacteristic(String address, String serviceUUID, String writeUUID, byte[] data);
}


