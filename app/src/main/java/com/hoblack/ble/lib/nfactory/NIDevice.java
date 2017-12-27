package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import com.hoblack.ble.lib.nservice.NXBleService;

import java.util.List;
import java.util.UUID;


public abstract class NIDevice implements NIOperationService, NXIBle, NXIBleRequestHandler {

    private NXBleService service;

    public NIDevice(NXBleService service) {
        this.service = service;
    }


    public abstract String getBTAdapterMacAddr();

    public abstract boolean adapterEnabled();

    public NXBleService getService() {
        return this.service;
    }

    public void startScan() {
        getService().startScan();
    }

    public void stopScan() {
        getService().stopScan();
    }

    public boolean connect(String address) {
        return getService().connect(address);
    }

    public void disconnect(String address) {
        getService().disconnect(address);
    }

    public void close(String address) {
        getService().close(address);
    }

    public List<BluetoothGattService> getServices(String address) {
        return getService().getServices(address);
    }

    public boolean discoverServices(String address) {
        return getService().discoverServices(address);
    }

    public BluetoothGattService getService(String address, UUID uuid) {
        return getService().getService(address, uuid);
    }


    public boolean readCharacteristic(String address, String serviceUUID, String readUUID) {
        return getService().readCharacteristic(address, serviceUUID, readUUID);
    }


    public boolean setCharacteristicNotification(String address, String serviceUUID, String notifyUUID, boolean enabled) {
        return getService().setCharacteristicNotification(address, serviceUUID, notifyUUID, enabled);
    }


    public boolean setCharacteristicIndication(String address, String serviceUUID, String notifyUUID, boolean enabled) {
        return getService().setCharacteristicIndication(address, serviceUUID, notifyUUID, enabled);
    }


    public boolean writeCharacteristic(String address, String serviceUUID, String writeUUID, byte[] data) {
        return getService().writeCharacteristic(address, serviceUUID, writeUUID, data);
    }


    public boolean readCharacteristic(String address, BluetoothGattCharacteristic characteristic) {
        return getService().readCharacteristic(address, characteristic);
    }


    public boolean characteristicNotification(String address, BluetoothGattCharacteristic c) {
        return getService().characteristicNotification(address, c);
    }


    public boolean writeCharacteristic(String address, BluetoothGattCharacteristic characteristic) {
        return getService().writeCharacteristic(address, characteristic);
    }


    public boolean requestConnect(String address) {
        return getService().requestConnect(address);
    }

    public boolean requestDiscoveryService(String address) {
        return getService().requestConnect(address);
    }


    public boolean requestReadCharacteristic(String address, BluetoothGattCharacteristic characteristic) {
        return getService().requestReadCharacteristic(address, characteristic);
    }


    public boolean requestCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic) {
        return getService().requestCharacteristicNotification(address, characteristic);
    }


    public boolean requestWriteCharacteristic(String address, BluetoothGattCharacteristic characteristic, String remark) {
        return getService().requestWriteCharacteristic(address, characteristic, remark);
    }


    public boolean requestCharacteristicIndication(String address, BluetoothGattCharacteristic characteristic) {
        return getService().requestCharacteristicIndication(address, characteristic);
    }


    public boolean requestStopNotification(String address, BluetoothGattCharacteristic characteristic) {
        return getService().requestStopNotification(address, characteristic);
    }


    public void doOperationEnd(String address) {
        getService().doOperationEnd(address);
    }


    public boolean requestReadCharacteristic(String address, String serviceUUID, String readUUID) {
        return getService().requestReadCharacteristic(address, serviceUUID, readUUID);
    }


    public boolean requestSetCharacteristicNotification(String address, String serviceUUID, String notifyUUID, boolean enabled) {
        return getService().requestSetCharacteristicNotification(address, serviceUUID, notifyUUID, enabled);
    }


    public boolean requestSetCharacteristicIndication(String address, String serviceUUID, String notifyUUID, boolean enabled) {
        return getService().requestSetCharacteristicIndication(address, serviceUUID, notifyUUID, enabled);
    }


    public boolean requestWriteCharacteristic(String address, String serviceUUID, String writeUUID, byte[] data) {
        return getService().requestWriteCharacteristic(address, serviceUUID, writeUUID, data);
    }
}


