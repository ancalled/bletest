package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothGattCharacteristic;


public class XNBleRequest {

    public RequestType type;
    public String address;
    public BluetoothGattCharacteristic characteristic;
    public String mark;

    public static XNBleRequest instance() {
        return new XNBleRequest();
    }

    public boolean belongDescriptor() {
        return type == RequestType.CHARACTERISTIC_INDICATION ||
                type == RequestType.CHARACTERISTIC_NOTIFICATION ||
                type == RequestType.CHARACTERISTIC_STOP_NOTIFICATION;
    }


    public XNBleRequest addType(RequestType type) {
        this.type = type;
        return this;
    }

    public XNBleRequest addAddress(String address) {
        this.address = address;
        return this;
    }

    public XNBleRequest addGattCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
        return this;
    }

    public XNBleRequest addMark(String mark) {
        this.mark = mark;
        return this;
    }

    public boolean equalCharacteristic(String uuid) {
        return characteristic == null && uuid == null ||
                (characteristic != null) && (uuid != null) &&
                        characteristic.getUuid().toString().equals(uuid);

    }

    public boolean equals(Object o) {
        if (!(o instanceof XNBleRequest)) {
            return false;
        }

        XNBleRequest req = (XNBleRequest) o;
        boolean equals = this.type == req.type;

        if (equals) {
            if ((this.address != null) && (req.address != null)) {
                equals = this.address.equals(req.address);
            } else {
                equals = this.address.equals(req.address);
            }
        }
        if (equals) {
            if ((this.characteristic != null) && (req.characteristic != null)) {
                equals = this.characteristic.getUuid().toString().equals(req.characteristic.getUuid().toString());
            } else {
                equals = this.characteristic == req.characteristic;
            }
        }
        return equals;
    }


    public String toString() {
        return "type " + this.type + " charact " +
                (this.characteristic != null ? this.characteristic.getUuid() : "") + " address " +
                this.address + " remark " + this.mark;
    }

    public enum FailReason {
        START_FAILED, TIMEOUT, RESULT_FAILED
    }

    public enum RequestType {
        CONNECT_GATT,
        DISCOVER_SERVICE,

        CHARACTERISTIC_INDICATION,
        CHARACTERISTIC_NOTIFICATION,
        //something lost here,
        READ_CHARACTERISTIC,
        WRITE_CHARACTERISTIC,

        CHARACTERISTIC_STOP_NOTIFICATION,
        OPERATION_END
    }
}


