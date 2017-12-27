package com.hoblack.ble.lib.nservice;

import android.app.Service;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.hoblack.ble.lib.constant.ClsUtils;
import com.hoblack.ble.lib.nfactory.NIOperationService;
import com.hoblack.ble.lib.nfactory.NXDeviceAndroid;
import com.hoblack.ble.lib.nfactory.NXIBleRequestHandler;
import com.hoblack.ble.lib.nfactory.XNBleRequest;

import java.util.*;

import static android.bluetooth.BluetoothGatt.GATT_READ_NOT_PERMITTED;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static com.hoblack.ble.lib.constant.GattShortUUIDs.CLIENT_CHARACTERISTIC_CONFIGURATION;
import static com.hoblack.ble.lib.constant.GattShortUUIDs.toFullUUidString;


public class NXBleService extends Service implements NIOperationService, NXIBleRequestHandler {

    private static final String BLE_NOT_SUPPORTED = "com.hoblack.ble.not_supported";
    private static final String BLE_NO_BT_ADAPTER = "com.hoblack.ble.no_bt_adapter";
    private static final String BLE_STATUS_ABNORMAL = "com.hoblack.ble.status_abnormal";
    private static final String BLE_REQUEST_FAILED = "com.hoblack.ble.request_failed";
    private static final String BLE_DEVICE_FOUND = "com.hoblack.ble.device_found";
    private static final String ACTION_GATT_CONNECTED = "com.hoblack.ble.gatt_connected";
    private static final String ACTION_GATT_DISCONNECTED = "com.hoblack.ble.gatt_disconnected";
    private static final String ACTION_GATT_SERVICES_DISCOVERED = "com.hoblack.ble.service_discovered";
    private static final String ACTION_OPERATION_END = "com.hoblack.ble.operation_end";
    private static final String ACTION_BLUETOOTH_STATEON = "com.hoblack.ble.bluetooth_stateon";
    private static final String ACTION_BLUETOOTH_STATEOFF = "com.hoblack.ble.bluetooth_stateoff";
    private static final String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    private static final String BLE_CHARACTERISTIC_READ = "com.hoblack.ble.characteristic_read";
    private static final String BLE_CHARACTERISTIC_NOTIFICATION = "com.hoblack.ble.characteristic_notification";
    private static final String BLE_CHARACTERISTIC_INDICATION = "com.hoblack.ble.characteristic_indication";
    private static final String BLE_CHARACTERISTIC_WRITE = "com.hoblack.ble.characteristic_write";
    private static final String BLE_CHARACTERISTIC_CHANGED = "com.hoblack.ble.characteristic_changed";
    private static final String EXTRA_DEVICE = "com.hoblack.ble.DEVICE";
    private static final String EXTRA_RSSI = "com.hoblack.ble.RSSI";
    private static final String EXTRA_SCAN_RECORD = "com.hoblack.ble.SCAN_RECORD";
    private static final String EXTRA_SOURCE = "com.hoblack.ble.SOURCE";
    private static final String EXTRA_ADDR = "com.hoblack.ble.";
    private static final String EXTRA_CONNECTED = "com.hoblack.ble.CONNECTED";
    private static final String EXTRA_STATUS = "com.hoblack.ble.STATUS";
    private static final String EXTRA_UUID = "com.hoblack.ble.UUID";
    @Deprecated
    private static final String EXTRA_DATA = "com.hoblack.ble.VALUE";
    private static final String EXTRA_VALUE = "com.hoblack.ble.VALUE";
    private static final String EXTRA_REQUEST = "com.hoblack.ble.REQUEST";
    private static final String EXTRA_REASON = "com.hoblack.ble.REASON";


//    private static final UUID DESC_CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final UUID DESC_CCC = UUID.fromString(toFullUUidString(CLIENT_CHARACTERISTIC_CONFIGURATION));


    private static final int DEVICE_SOURCE_SCAN = 0;
    private static final int DEVICE_SOURCE_BONDED = 1;
    private static final int DEVICE_SOURCE_CONNECTED = 2;
    private static final int STATE_DISCONNECTING = 3;
    private static final int STATE_NONE = 4;

    private final IBinder binder;

//    private Object a;
    private String[] statuses;

    private final Queue<XNBleRequest> requestQueue;

    private XNBleRequest currRequest;

    private boolean started;
//    private int p;
    private Thread thread;
    private Runnable runnable;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private Map<String, BluetoothGatt> gattMap;
    private BroadcastReceiver broadcastReceiver;
    private BluetoothGattCallback gattCallback;
    private Handler handler;
    private BluetoothAdapter.LeScanCallback scanCallback;
    private NXDeviceAndroid device;

    private boolean someBool;
//    private Object someObj;
    private int state;
    private Integer somState;



    public NXBleService() {
        binder = new LocalBinder();
//        this.a = new Object();
        statuses = new String[]{"disconnected[0]", "connecting[1]", "connected[2]", "disconnecting[3]"};
        someBool = false;
//        this.someObj = new Object();
        scanCallback = new BluetoothAdapter.LeScanCallback() {
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                bleDeviceFound(device, rssi, scanRecord, 0);
            }

        };
        state = DEVICE_SOURCE_SCAN;
        requestQueue = new LinkedList<>();
        currRequest = null;
        started = false;
//        this.p = 0;

        runnable = new Runnable() {
            public void run() {
                try {
                    somState = 0;
                    while (init()) {
                        Thread.sleep(10L);
                        getBlesdk();

//                        if (smth > 500) {
//                            if (getCurrentRequest() != null) {
//                                bleRequestFailed(getCurrentRequest().address, getCurrentRequest().type, XNBleRequest.FailReason.TIMEOUT);
//                                bleStatusAbnormal(getCurrentRequest() + " [timeout]");
//                                handler.sendEmptyMessage(136);
//                            }
//                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED") &&
                        intent.getIntExtra("android.bluetooth.adapter.extra.STATE", BluetoothAdapter.STATE_OFF) ==
                                BluetoothAdapter.STATE_ON) {

                    processIntent();
//                    requestQueue.clear();

                    srart();
                    init();

                    sendBroadcast(new Intent(ACTION_BLUETOOTH_STATEON));
                }

            }
        };

        gattCallback = new BluetoothGattCallback() {

            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                String address = gatt.getDevice().getAddress();

                Log.e("ble-service", "onConnectionStateChange " + address + " status " + status + " newState " +
                                statuses[newState] + requestQueue.size()
                );

                if (status != GATT_SUCCESS) {
                    state = DEVICE_SOURCE_SCAN;
                    disconnect(address);
                    bleGattDisConnected(address);
                    return;
                }

                if (newState == GATT_READ_NOT_PERMITTED) {
                    state = DEVICE_SOURCE_CONNECTED;
                    bleGattConnected(gatt.getDevice());

                } else if (newState == GATT_SUCCESS) {
                    state = DEVICE_SOURCE_SCAN;
                    bleGattDisConnected(address);
                    disconnect(address);
                }
            }

            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                String address = gatt.getDevice().getAddress();
                if (status != GATT_SUCCESS) {
                    requestProcessed(address, XNBleRequest.RequestType.DISCOVER_SERVICE, null, false);
                    return;
                }
                state = DEVICE_SOURCE_CONNECTED;
                bleServiceDiscovered(gatt.getDevice().getAddress());
            }


            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                String str = gatt.getDevice().getAddress();
                if (status != 0) {
                    requestProcessed(str, XNBleRequest.RequestType.READ_CHARACTERISTIC,
                            characteristic.getUuid().toString(), false);
                    return;
                }

                bleCharacteristicRead(gatt.getDevice().getAddress(), characteristic
                        .getUuid().toString(), status, characteristic.getValue());
            }


            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                String str = gatt.getDevice().getAddress();

                NXBleService.this.bleCharacteristicChanged(str,
                        characteristic.getUuid().toString(),
                        characteristic.getValue());
            }

            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                String str = gatt.getDevice().getAddress();
                if (status != 0) {
                    NXBleService.this.requestProcessed(str, XNBleRequest.RequestType.WRITE_CHARACTERISTIC,
                            characteristic.getUuid().toString(), false);
                    return;
                }
                NXBleService.this.bleCharacteristicWrite(gatt.getDevice().getAddress(), characteristic
                        .getUuid().toString(), status);
            }

            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                String address = gatt.getDevice().getAddress();
                XNBleRequest request = getCurrentRequest();
                String uuid = descriptor.getCharacteristic().getUuid().toString();

                if (request.belongDescriptor()) {
                    if (status != 0) {
                        requestProcessed(address, XNBleRequest.RequestType.CHARACTERISTIC_NOTIFICATION, uuid, false);
                        return;
                    }

                    if (request.type == XNBleRequest.RequestType.CHARACTERISTIC_NOTIFICATION) {
                        bleCharacteristicNotification(address, uuid, true, status);

                    } else if (request.type == XNBleRequest.RequestType.CHARACTERISTIC_INDICATION) {
                        bleCharacteristicIndication(address, uuid, status);

                    } else {
                        bleCharacteristicNotification(address, uuid, false, status);
                    }

                }
            }

            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

        };

        this.handler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                int i = msg.what;
                if (i == 255) {
                    if (getCurrentRequest() != null) {
                        return true;
                    }
                    handler.removeMessages(255);

//                    NXBleService.f(NXBleService.this);
                } else if (i == 136) {
                    handler.removeMessages(136);
                    setCurrentRequest(null, "timeout thread");
//                    NXBleService.f(NXBleService.this);
                }
                return true;
            }
        });
    }


    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BLE_NOT_SUPPORTED);
        filter.addAction(BLE_NO_BT_ADAPTER);
        filter.addAction(BLE_STATUS_ABNORMAL);
        filter.addAction(BLE_REQUEST_FAILED);
        filter.addAction(BLE_DEVICE_FOUND);
        filter.addAction(ACTION_GATT_CONNECTED);
        filter.addAction(ACTION_GATT_DISCONNECTED);
        filter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(ACTION_OPERATION_END);
        filter.addAction(PAIRING_REQUEST);
        filter.addAction(BLE_CHARACTERISTIC_READ);
        filter.addAction(BLE_CHARACTERISTIC_NOTIFICATION);
        filter.addAction(BLE_CHARACTERISTIC_WRITE);
        filter.addAction(BLE_CHARACTERISTIC_CHANGED);
        return filter;
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }


    public void onCreate() {
        init();

        BLESDK blesdk = getBlesdk();
        if (blesdk == BLESDK.NOT_SUPPORTED) {
            return;
        }

        this.device = new NXDeviceAndroid(this);
        IntentFilter intentFilter = getIntentFilter();
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        registerReceiver(this.broadcastReceiver, intentFilter);
    }


    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(this.broadcastReceiver);
    }

    private void bleNotSupported() {
        Intent intent = new Intent(BLE_NOT_SUPPORTED);
        sendBroadcast(intent);
    }

    private void bleNoBtAdapter() {
        Intent intent = new Intent(BLE_NO_BT_ADAPTER);
        sendBroadcast(intent);
    }

    private BLESDK getBlesdk() {
        if (getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return BLESDK.ANDROID;
        }
        bleNotSupported();
        return BLESDK.NOT_SUPPORTED;
    }

    public NXDeviceAndroid getDevice() {
        return device;
    }


    private void bleDeviceFound(BluetoothDevice device, int rssi, byte[] scanRecord, int source) {
        Intent intent = new Intent(BLE_DEVICE_FOUND);
        intent.putExtra(EXTRA_DEVICE, device);
        intent.putExtra(EXTRA_RSSI, rssi);
        intent.putExtra(EXTRA_SCAN_RECORD, scanRecord);
        intent.putExtra(EXTRA_SOURCE, source);
        sendBroadcast(intent);
    }


    private void bleGattConnected(BluetoothDevice device) {
        Intent intent = new Intent(ACTION_GATT_CONNECTED);
        intent.putExtra(EXTRA_DEVICE, device);
        intent.putExtra(EXTRA_ADDR, device.getAddress());
        sendBroadcast(intent);
        requestProcessed(device.getAddress(), XNBleRequest.RequestType.CONNECT_GATT, null, true);
    }


    private void bleGattDisConnected(String address) {
        joinThread();
        Log.e("ble-service", "remain count" + requestQueue.size());

        requestQueue.clear();
        Intent intent = new Intent(ACTION_GATT_DISCONNECTED);
        intent.putExtra(EXTRA_ADDR, address);
        sendBroadcast(intent);
    }


    private void bleServiceDiscovered(String address) {
        Intent intent = new Intent(ACTION_GATT_SERVICES_DISCOVERED);
        intent.putExtra(EXTRA_ADDR, address);
        sendBroadcast(intent);
        requestProcessed(address, XNBleRequest.RequestType.DISCOVER_SERVICE, null, true);
    }


    private void broadcastOperationEnd(String address) {
        Intent intent = new Intent(ACTION_OPERATION_END);
        intent.putExtra(EXTRA_ADDR, address);
        sendBroadcast(intent);
    }


    private void joinThread() {
        if (thread == null) {
            return;
        }

        if (thread.isAlive()) {
            try {
                started = false;
                thread.join();
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void bleCharacteristicRead(String address, String uuid, int status, byte[] value) {
        Intent intent = new Intent(BLE_CHARACTERISTIC_READ);
        intent.putExtra(EXTRA_ADDR, address);
        intent.putExtra(EXTRA_UUID, uuid);
        intent.putExtra(EXTRA_STATUS, status);
        intent.putExtra(EXTRA_VALUE, value);
        sendBroadcast(intent);
        requestProcessed(address, XNBleRequest.RequestType.READ_CHARACTERISTIC, uuid, true);
    }


    private void addBleRequest(XNBleRequest request) {
        if (state != DEVICE_SOURCE_CONNECTED) {
            return;
        }
        requestQueue.add(request);
        handler.sendEmptyMessage(255);
    }


    private void requestProcessed(String address, XNBleRequest.RequestType requestType, String uuid, boolean success) {
        XNBleRequest request = getCurrentRequest();
        if (request != null && request.type == requestType && request.equalCharacteristic(uuid)) {
            joinThread();
            if (!success) {
                bleRequestFailed(request.address, request.type, XNBleRequest.FailReason.RESULT_FAILED);
            }
            setCurrentRequest(null, "processed");
            handler.sendEmptyMessage(255);
        }
    }


//    private void d() {
    private void processIntent() {
        if (getCurrentRequest() != null) {
            //skip if current intent was not processed
            return;
        }

        joinThread();

        synchronized (requestQueue) {
            if (requestQueue.isEmpty()) {
                return;
            }

            try {
                setCurrentRequest(requestQueue.remove(), "next request");
                this.someBool = false;
            } catch (NoSuchElementException e) {
                return;
            }
        }

        XNBleRequest request = getCurrentRequest();

        boolean success = false;
        switch (request.type) {
            case CONNECT_GATT:
                success = device.connect(request.address);
                break;
            case DISCOVER_SERVICE:
                success = device.discoverServices(request.address);
                break;
            case CHARACTERISTIC_INDICATION:
            case CHARACTERISTIC_NOTIFICATION:
//            case 5:
                success = device.characteristicNotification(
                        request.address,
                        request.characteristic);
                break;
            case READ_CHARACTERISTIC:
                success = device.readCharacteristic(
                        request.address,
                        request.characteristic);
                break;
            case WRITE_CHARACTERISTIC:
                success = device.writeCharacteristic(
                        request.address,
                        request.characteristic);
                break;
            case CHARACTERISTIC_STOP_NOTIFICATION:
                break;
            case OPERATION_END:
                broadcastOperationEnd(request.address);
                break;
        }


        if (request.type == XNBleRequest.RequestType.OPERATION_END) {
            setCurrentRequest(null, "END");
            Log.e("ble-service", "**************END**************************");
            return;
        }

        if (getCurrentRequest() == null) {
            return;
        }

        if (success) {
            srart();

        } else {
            bleRequestFailed(request.address,
                    request.type,
                    XNBleRequest.FailReason.START_FAILED);
            handler.sendEmptyMessage(136);
        }
    }


    private void srart() {
        this.started = true;
        this.thread = new Thread(this.runnable);
        this.thread.start();
    }

    private XNBleRequest getCurrentRequest() {
        return currRequest;
    }


    private void setCurrentRequest(XNBleRequest mCurrentRequest, String mark) {
        this.currRequest = mCurrentRequest;
    }


    private void bleCharacteristicNotification(String address, String uuid, boolean isEnabled, int status) {
        Intent intent = new Intent(BLE_CHARACTERISTIC_NOTIFICATION);
        intent.putExtra(EXTRA_ADDR, address);
        intent.putExtra(EXTRA_UUID, uuid);
        intent.putExtra(EXTRA_VALUE, isEnabled);
        intent.putExtra(EXTRA_STATUS, status);
        sendBroadcast(intent);
        if (isEnabled) {
            requestProcessed(address, XNBleRequest.RequestType.CHARACTERISTIC_NOTIFICATION, uuid, true);
        } else {
            requestProcessed(address, XNBleRequest.RequestType.CHARACTERISTIC_STOP_NOTIFICATION, uuid, true);
        }
    }


    private void bleCharacteristicIndication(String address, String uuid, int status) {
        Intent intent = new Intent(BLE_CHARACTERISTIC_INDICATION);
        intent.putExtra(EXTRA_ADDR, address);
        intent.putExtra(EXTRA_UUID, uuid);
        intent.putExtra(EXTRA_STATUS, status);
        sendBroadcast(intent);
        requestProcessed(address, XNBleRequest.RequestType.CHARACTERISTIC_INDICATION, uuid, true);
    }


    private void bleCharacteristicWrite(String address, String uuid, int status) {
        Intent intent = new Intent(BLE_CHARACTERISTIC_WRITE);
        intent.putExtra(EXTRA_ADDR, address);
        intent.putExtra(EXTRA_UUID, uuid);
        intent.putExtra(EXTRA_STATUS, status);
        sendBroadcast(intent);
        requestProcessed(address, XNBleRequest.RequestType.WRITE_CHARACTERISTIC, uuid, true);
    }


    private void bleCharacteristicChanged(String address, String uuid, byte[] value) {
        Intent intent = new Intent(BLE_CHARACTERISTIC_CHANGED);
        intent.putExtra(EXTRA_ADDR, address);
        intent.putExtra(EXTRA_UUID, uuid);
        intent.putExtra(EXTRA_VALUE, value);
        sendBroadcast(intent);
    }


    private void bleStatusAbnormal(String reason) {
        Intent intent = new Intent(BLE_STATUS_ABNORMAL);
        intent.putExtra(EXTRA_VALUE, reason);
        sendBroadcast(intent);
    }


    private void bleRequestFailed(String address, XNBleRequest.RequestType type, XNBleRequest.FailReason reason) {
        Intent intent = new Intent(BLE_REQUEST_FAILED);
        intent.putExtra(EXTRA_ADDR, address);
        intent.putExtra(EXTRA_REQUEST, type);
        intent.putExtra(EXTRA_REASON, reason.ordinal());
        sendBroadcast(intent);
    }


    private boolean init() {
        bluetoothManager = ((BluetoothManager) getSystemService("bluetooth"));
        if (bluetoothManager == null) {
            return false;
        }

        gattMap = new HashMap<>();
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            bleNoBtAdapter();
            return false;
        }
        return true;
    }


    public void startScan() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.startLeScan(this.scanCallback);
        }
    }


    public void stopScan() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(this.scanCallback);
        }
    }


    public void disconnect(String address) {
        if (gattMap.containsKey(address)) {

            BluetoothGatt gatt = gattMap.remove(address);
//            String[] arrayOfString = {"None[10]", "Bonding[11]", "Bonded[12]"};

            if (gatt.getDevice().getBondState() != BluetoothDevice.BOND_NONE) {
                try {
                    Class<? extends BluetoothDevice> btClass = gatt.getDevice().getClass();
                    ClsUtils.removeBond(btClass, gatt.getDevice());
                    ClsUtils.cancelBondProcess(btClass, gatt.getDevice());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            gatt.disconnect();
        }
    }


    public void close(String address) {
        if (gattMap.containsKey(address)) {
            BluetoothGatt gatt = gattMap.remove(address);
            if (gatt != null) {
                gatt.disconnect();
                gatt.close();
            }
        }
    }


    public int getConnectState(String address) {
        if (address == null) {
            return STATE_NONE;
        }
        if (address.length() < 1) {
            return STATE_NONE;
        }

        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (bluetoothAdapter == null)) {
            return STATE_NONE;
        }
        Log.e("ex-ble", "request5 connect" + address + gatt);
        return bluetoothManager.getConnectionState(bluetoothAdapter.getRemoteDevice(address), 7);
    }


    public boolean connect(String address) {
        Log.e("ble-service", "connect to " + address + new Date().toLocaleString());
        if (bluetoothAdapter == null || address == null) {
            return false;
        }
//        String[] arrayOfString = {"None[10]", "Bonding[11]", "Bonded[12]"};
        BluetoothGatt gatt = gattMap.get(address);
        if (gatt != null && gatt.getDevice().getBondState() != BluetoothDevice.BOND_NONE) {
            if (gatt.connect()) {
                state = DEVICE_SOURCE_BONDED;
                return true;
            }


            return false;
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }

        state = DEVICE_SOURCE_SCAN;
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            try {
                ClsUtils.removeBond(device.getClass(), device);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        gatt = device.connectGatt(this, false, this.gattCallback);

        if (gatt == null) {
            gattMap.remove(address);
            return false;
        }

        state = DEVICE_SOURCE_BONDED;
        gattMap.put(address, gatt);
        return true;
    }


    public boolean discoverServices(String address) {
        BluetoothGatt gatt = gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        boolean bool = gatt.discoverServices();
        Log.e("ble-service", "discover services--------" + gatt + bool);

        return bool;
    }

    public List<BluetoothGattService> getServices(String address) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt != null) {
            return gatt.getServices();
        }
        return null;
    }


    public boolean readCharacteristic(String address, String serviceUUID, String readUUID) {
        BluetoothGatt gatt = gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic ch = service.getCharacteristic(UUID.fromString(readUUID));
        return ch != null && gatt.readCharacteristic(ch);

    }


    public boolean setCharacteristicNotification(String address, String serviceUUID, String notifyUUID, boolean enable) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService serv = gatt.getService(UUID.fromString(serviceUUID));
        if (serv == null) {
            return false;
        }
        BluetoothGattCharacteristic ch = serv.getCharacteristic(UUID.fromString(notifyUUID));
        if (ch == null) {
            return false;
        }

        if (!gatt.setCharacteristicNotification(ch, enable)) {
            return false;
        }

        BluetoothGattDescriptor desc = ch.getDescriptor(DESC_CCC);
        if (desc == null) {
            return false;
        }

        byte[] data;
        if (!enable) {
            data = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        } else {
            data = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        }

        return desc.setValue(data) && gatt.writeDescriptor(desc);

    }


    public boolean setCharacteristicIndication(String address, String serviceUUID, String notifyUUID, boolean enable) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic ch = service.getCharacteristic(UUID.fromString(notifyUUID));
        if (ch == null) {
            return false;
        }

        if (!gatt.setCharacteristicNotification(ch, enable)) {
            return false;
        }

        BluetoothGattDescriptor desc = ch.getDescriptor(DESC_CCC);
        if (desc == null) {
            return false;
        }

        byte[] data;
        if (!enable) {
            data = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        } else {
            data = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
        }

        return desc.setValue(data) && gatt.writeDescriptor(desc);

    }

    public BluetoothGatt getGatt(String address) {
        return this.gattMap.get(address);
    }


    public boolean writeCharacteristic(String address, String serviceUUID, String writeUUID, byte[] data) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }

        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic ch = service.getCharacteristic(UUID.fromString(writeUUID));
        return ch != null &&
                data != null &&
                ch.setValue(data) &&
                gatt.writeCharacteristic(ch);

    }


    public BluetoothGattService getService(String address, UUID uuid) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return null;
        }

        return gatt.getService(uuid);
    }


    public boolean readCharacteristic(String address, BluetoothGattCharacteristic characteristic) {
        BluetoothGatt gatt = this.gattMap.get(address);
        return gatt != null && gatt.readCharacteristic(characteristic);
    }


    public boolean characteristicNotification(String address, BluetoothGattCharacteristic c) {
        XNBleRequest request = getCurrentRequest();
        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (c == null)) {
            return false;
        }

        boolean bool = true;
        if (request.type == XNBleRequest.RequestType.CHARACTERISTIC_STOP_NOTIFICATION) {
            bool = false;
        }
        if (!gatt.setCharacteristicNotification(c, bool)) {
            return false;
        }

        BluetoothGattDescriptor desc = c.getDescriptor(DESC_CCC);
        if (desc != null) {
            byte[] data;
            if (request.type == XNBleRequest.RequestType.CHARACTERISTIC_NOTIFICATION) {
                data = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
            } else if (request.type == XNBleRequest.RequestType.CHARACTERISTIC_INDICATION) {
                data = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
            } else {
                data = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            }

            desc.setValue(data);
            gatt.writeDescriptor(desc);
        }
        return true;
    }


    public boolean writeCharacteristic(String address, BluetoothGattCharacteristic characteristic) {
        BluetoothGatt gatt = this.gattMap.get(address);
        return gatt != null && gatt.writeCharacteristic(characteristic);
    }


    public boolean requestConnect(String address) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt != null && gatt.getServices().size() == 0) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.CONNECT_GATT)
                .addAddress(address));
        return true;
    }

    public boolean requestDiscoveryService(String address) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.DISCOVER_SERVICE)
                .addAddress(address));
        return true;
    }


    public boolean requestReadCharacteristic(String address, BluetoothGattCharacteristic characteristic) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (characteristic == null)) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.READ_CHARACTERISTIC)
                .addGattCharacteristic(characteristic)
                .addAddress(gatt.getDevice().getAddress()));
        return true;
    }


    public boolean requestCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (characteristic == null)) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.CHARACTERISTIC_NOTIFICATION)
                .addGattCharacteristic(characteristic)
                .addAddress(gatt.getDevice().getAddress()));
        return true;
    }


    public boolean requestWriteCharacteristic(String address, BluetoothGattCharacteristic characteristic, String remark) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (characteristic == null)) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.WRITE_CHARACTERISTIC)
                .addGattCharacteristic(characteristic)
                .addAddress(gatt.getDevice().getAddress()).addMark(remark));
        return true;
    }


    public boolean requestCharacteristicIndication(String address, BluetoothGattCharacteristic characteristic) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (characteristic == null)) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.CHARACTERISTIC_INDICATION)
                .addGattCharacteristic(characteristic)
                .addAddress(gatt.getDevice().getAddress()));
        return true;
    }


    public boolean requestStopNotification(String address, BluetoothGattCharacteristic characteristic) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if ((gatt == null) || (characteristic == null)) {
            return false;
        }

        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.CHARACTERISTIC_STOP_NOTIFICATION)
                .addGattCharacteristic(characteristic)
                .addAddress(gatt.getDevice().getAddress()));
        return true;
    }

    public void doOperationEnd(String address) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return;
        }
        addBleRequest(XNBleRequest.instance()
                .addType(XNBleRequest.RequestType.OPERATION_END)
                .addAddress(gatt.getDevice().getAddress()));
    }


    public boolean requestReadCharacteristic(String address, String serviceUUID, String readUUID) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(readUUID));
        return requestReadCharacteristic(address, characteristic);
    }


    public boolean requestSetCharacteristicNotification(String address, String serviceUUID, String notifyUUID, boolean enabled) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(
                UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(notifyUUID));

        return enabled ? requestCharacteristicNotification(address, characteristic) :
                requestStopNotification(address, characteristic);
    }


    public boolean requestSetCharacteristicIndication(String address, String serviceUUID, String notifyUUID, boolean enabled) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(
                UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(notifyUUID));

        return enabled ? requestCharacteristicNotification(address, characteristic) : requestStopNotification(address, characteristic);
    }


    public boolean requestWriteCharacteristic(String address, String serviceUUID, String writeUUID, byte[] data) {
        BluetoothGatt gatt = this.gattMap.get(address);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            return false;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(writeUUID));
        return characteristic != null && (data != null) &&
                (characteristic.setValue(data)) &&
                requestWriteCharacteristic(address, characteristic, null);

    }

    public class LocalBinder extends Binder {

        public NXBleService getService() {
            return NXBleService.this;
        }
    }

    public enum BLESDK {
        ANDROID, NOT_SUPPORTED
    }
}

