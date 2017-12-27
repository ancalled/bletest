package com.hoblack.ble.lib.nfactory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import com.hoblack.ble.lib.constant.GattShortUUIDs;
import com.hoblack.ble.lib.nservice.NXBleService;

import static com.hoblack.ble.lib.constant.GattShortUUIDs.*;


public class NXDeviceAndroid extends NIDevice implements NXIBle, NXIBleRequestHandler {

    protected static final String TAG = "blelib";

    public NXDeviceAndroid(NXBleService service) {
        super(service);
    }


    public boolean adapterEnabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) getService().getSystemService("bluetooth");
        if (bluetoothManager == null) {
            return false;
        }
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }


    public String getBTAdapterMacAddr() {
        BluetoothManager bluetoothManager = (BluetoothManager) getService().getSystemService("bluetooth");
        if (bluetoothManager == null) {
            return null;
        }
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        if (adapter != null) {
            return adapter.getAddress();
        }
        return null;
    }


    public void doWriteEddyURL(String address, byte[] data) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_URL_RW), data);
    }


    public void requestWriteEddyURL(String address, byte[] data) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_URL_RW), data);
    }


    public void doWriteEddyInstance(String address, byte[] data) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ID_RW), data);
    }


    public void requestWriteEddyInstance(String address, byte[] data) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ID_RW), data);
    }


    public void doReadEddyInstance(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ID_RW));
    }


    public void requestReadEddyInstance(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ID_RW));
    }


    public void doReadEddyURL(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_URL_RW));
    }


    public void requestReadEddyURL(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_URL_RW));
    }


    public void doReadBattery(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_BATTERY_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_BATTERY_R));
    }


    public void requestReadBattery(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_BATTERY_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_BATTERY_R));
    }


    public void notifyAcceleration(String address, boolean enable) {
        setCharacteristicNotification(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ACC_N), enable);
    }


    public void requestNotifyAcceleration(String address, boolean enable) {
        setCharacteristicNotification(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ACC_N), enable);
    }


    public void doWriteHumitureState(String address, boolean on) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_HUMITURE_SW_RW),
                new byte[]{(byte) (on ? 'ÿ' : 0)});
    }


    public void doWriteAccState(String address, boolean on) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ACC_SW_RW),
                new byte[]{(byte) (on ? 'ÿ' : 0)});
    }


    public void doWriteMoveDetectModeState(String address, boolean on) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MOVEDECT_SW_RW),
                new byte[]{(byte) (on ? 'ÿ' : 0)});
    }


    public void doWriteBroadTime(String address, int time) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_BROAD_TIME_RW),
                new byte[]{(byte) (time >> 8 & 0xFF), (byte) (time & 0xFF)});
    }


    public void doWriteBroadMode(String address, int mode) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MODE_BROAD_RW),
                new byte[]{(byte) mode});
    }


    public void requestWriteHumitureState(String address, boolean on) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_HUMITURE_SW_RW),
                new byte[]{(byte) (on ? 'ÿ' : 0)});
    }


    public void requestWriteAccState(String address, boolean on) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ACC_SW_RW), new byte[]{(byte) (on ? 'ÿ' : 0)});
    }


    public void requestWriteMoveDetectModeState(String address, boolean on) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MOVEDECT_SW_RW), new byte[]{(byte) (on ? 'ÿ' : 0)});
    }


    public void requestWriteBroadTime(String address, int time) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_BROAD_TIME_RW), new byte[]{(byte) (time >> 8 & 0xFF), (byte) (time & 0xFF)});
    }


    public void requestWriteBroadMode(String address, int mode) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MODE_BROAD_RW), new byte[]{(byte) mode});
    }


    public void doReadHumitureState(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_HUMITURE_SW_RW));
    }


    public void doReadHumiture(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_HUMITURE_R));
    }


    public void doReadAccState(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ACC_SW_RW));
    }


    public void doReadMoveDetectModeState(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MOVEDECT_SW_RW));
    }


    public void doReadBroadTime(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_BROAD_TIME_RW));
    }


    public void doReadBroadMode(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MODE_BROAD_RW));
    }


    public void requestReadHumitureState(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_HUMITURE_SW_RW));
    }


    public void requestReadHumiture(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_HUMITURE_R));
    }


    public void requestReadAccState(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_ACC_SW_RW));
    }


    public void requestReadMoveDetectModeState(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MOVEDECT_SW_RW));
    }


    public void requestReadBroadTime(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_BROAD_TIME_RW));
    }


    public void requestReadBroadMode(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MODE_BROAD_RW));
    }


    public void doReadValueUUID(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_uuid_RW));
    }


    public void doReadValueConnectable(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_CONNECTABLE_RW));
    }


    public void doReadValueTransmitInterval(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_INTERNAL_RW));
    }


    public void doReadMajor(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MAJOR_RW));
    }


    public void doReadMinor(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MINOR_RW));
    }


    public void doReadName(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_NAME_RW));
    }


    public void doReadTime(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TIME_RW));
    }


    public void doReadMeasuredAndTxpower(String address) {
        readCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TXPOWER_RW));
    }


    public void writeUUID(String address, byte[] uuid) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_uuid_RW), uuid);
    }


    public void writeMajor(String address, short major) {
        byte[] bytes = new byte[2];
        bytes[1] = ((byte) (major >> 8 & 0xFF));
        bytes[0] = ((byte) (major & 0xFF));
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MAJOR_RW), bytes);
    }


    public void writeMinor(String address, short minor) {
        byte[] bytes = new byte[2];
        bytes[1] = ((byte) (minor >> 8 & 0xFF));
        bytes[0] = ((byte) (minor & 0xFF));
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MINOR_RW), bytes);
    }


    public void writeTransmitInterval(String address, short interval) {
        byte[] bytes = new byte[2];
        bytes[1] = ((byte) (interval >> 8 & 0xFF));
        bytes[0] = ((byte) (interval & 0xFF));
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_INTERNAL_RW), bytes);
    }


    public void writeConnectable(String address, boolean enable) {
        byte[] bytes = new byte[1];
        bytes[0] = 1;
        if (enable) {
            bytes[0] = 0;
        }
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_CONNECTABLE_RW), bytes);
    }


    public void writeMeasuredAndTxpower(String address, short power) {
        byte[] bytes = new byte[2];
        bytes[0] = ((byte) (power >> 8));
        bytes[1] = ((byte) power);
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TXPOWER_RW), bytes);
    }


    public void writeTime(String address, byte[] bytes) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TIME_RW), bytes);
    }


    public void writeName(String address, String name) {
        byte[] arrayOfByte = name.getBytes();

        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_NAME_RW), arrayOfByte);
    }


    public void writeFastConnect(String address, boolean enable) {
        byte[] arrayOfByte = new byte[1];
        arrayOfByte[0] = 0;
        if (enable) {
            arrayOfByte[0] = 1;
        }
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_FAST_CON_W), arrayOfByte);
    }


    public void writePairCode(String address, String code) {
        if ((code == null) || (code.length() < 6)) {
            return;
        }
        byte[] bytes;
        if (code.length() > 11) {
            bytes = code.substring(0, 12).getBytes();
        } else {
            bytes = code.substring(0, 6).getBytes();
        }
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_PAIR_W), bytes);
    }


    public void notifyPairCode(String address, boolean enable) {
        setCharacteristicNotification(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_PAIR_N), enable);
    }


    public void doReadPwdSate(String address) {
        writeCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_PAIR_N), new byte[]{-1});
    }


    public void requestReadValueUUID(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_uuid_RW));
    }


    public void requestReadValueConnectable(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_CONNECTABLE_RW));
    }


    public void requestReadValueTransmitInterval(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_INTERNAL_RW));
    }


    public void requestReadMajor(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MAJOR_RW));
    }


    public void requestReadMinor(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MINOR_RW));
    }


    public void requestReadName(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_NAME_RW));
    }


    public void requestReadTime(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TIME_RW));
    }


    public void requestReadMeasuredAndTxpower(String address) {
        requestReadCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TXPOWER_RW));
    }


    public void requestWriteUUID(String address, byte[] uuid) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_uuid_RW), uuid);
    }


    public void requestWriteMajor(String address, short major) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[1] = ((byte) (major >> 8 & 0xFF));
        arrayOfByte[0] = ((byte) (major & 0xFF));
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MAJOR_RW), arrayOfByte);
    }


    public void requestWriteMinor(String address, short minor) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[1] = ((byte) (minor >> 8 & 0xFF));
        arrayOfByte[0] = ((byte) (minor & 0xFF));
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_MINOR_RW), arrayOfByte);
    }


    public void requestWriteTransmitInterval(String address, short interval) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[1] = ((byte) (interval >> 8 & 0xFF));
        arrayOfByte[0] = ((byte) (interval & 0xFF));

        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_INTERNAL_RW), arrayOfByte);
    }


    public void requestWriteConnectable(String address, boolean enable) {
        byte[] arrayOfByte = new byte[1];
        arrayOfByte[0] = 1;
        if (enable) {
            arrayOfByte[0] = 0;
        }
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_CONNECTABLE_RW), arrayOfByte);
    }


    public void requestWriteMeasuredAndTxpower(String address, short power) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[0] = ((byte) (power >> 8));
        arrayOfByte[1] = ((byte) power);
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TXPOWER_RW), arrayOfByte);
    }


    public void requestWriteTime(String address, byte[] bytes) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_TIME_RW), bytes);
    }


    public void requestWriteName(String address, String name) {
        byte[] arrayOfByte = name.getBytes();

        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_NAME_RW), arrayOfByte);
    }


    public void requestWriteFastConnect(String address, boolean enable) {
        byte[] arrayOfByte = new byte[1];
        arrayOfByte[0] = 0;
        if (enable) {
            arrayOfByte[0] = 1;
        }
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_FAST_CON_W), arrayOfByte);
    }


    public void requestWritePairCode(String address, String code) {
        if ((code == null) || (code.length() < 6)) {
            return;
        }
        byte[] arrayOfByte;
        if (code.length() > 11) {
            arrayOfByte = code.substring(0, 12).getBytes();
        } else {
            arrayOfByte = code.substring(0, 6).getBytes();
        }
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_PAIR_W), arrayOfByte);
    }


    public void requestNotifyPairCode(String address, boolean enable) {
        requestSetCharacteristicNotification(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_PAIR_N), enable);
    }


    public void requestPwdSate(String address) {
        requestWriteCharacteristic(address,
                toFullUUidString(SERVICE_SHORT_UUID),
                toFullUUidString(CHARACT_SHORT_UUID_PAIR_N), new byte[]{-1});
    }
}


