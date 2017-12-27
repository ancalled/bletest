package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothGattCharacteristic;


@Deprecated
public class NBleRequest {

    public RequestType type;
    public String address;
    public BluetoothGattCharacteristic characteristic;
    public String remark;

    public NBleRequest(RequestType type, String address) {
        this.type = type;
        this.address = address;
    }

    public NBleRequest(RequestType type, String address, BluetoothGattCharacteristic characteristic) {
        this.type = type;
        this.address = address;
        this.characteristic = characteristic;
    }

    public NBleRequest(RequestType type, String address, BluetoothGattCharacteristic characteristic, String remark) {
        this.type = type;
        this.address = address;
        this.characteristic = characteristic;
        this.remark = remark;
    }

    public boolean equals(Object o) {
        if (!(o instanceof NBleRequest)) {
            return false;
        }

        NBleRequest req = (NBleRequest) o;
        return (this.type == req.type) && (this.address.equals(req.address));
    }

    public static enum FailReason {
    }

    public static enum RequestType {
    }

}


