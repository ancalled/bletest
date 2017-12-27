package com.hoblack.ble.lib.nfactory;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.hoblack.ble.lib.nservice.NXBleService;

public class NXBleApplication extends Application {

    private static final String ACTION_BLE_SERVICE_READY = "com.hoblack.ble.service_ready";

    private NXBleService bleService;
    private NXDeviceAndroid device;

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            NXBleService.LocalBinder binder = (NXBleService.LocalBinder) rawBinder;
            bleService = binder.getService();
            device = bleService.getDevice();

            if (device != null && !device.adapterEnabled()) {
                BluetoothManager bluetooth = (BluetoothManager) getApplicationContext().getSystemService("bluetooth");
                if (bluetooth != null) {
                    BluetoothAdapter adapter = bluetooth.getAdapter();
                    if (adapter != null) {
                        adapter.enable();
                    }
                }
            }
            if (device != null) {
                sendBroadcast(new Intent(ACTION_BLE_SERVICE_READY));

//                getService().writeCharacteristic(address,
//                        "98B5BF3E-F473-A390-DE68-602ED238551B",
//                        "98B5FFF2-F473-A390-DE68-602ED238551B",  //CHARACT_SHORT_UUID_URL_RW
//                        data
//                );
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            bleService = null;
        }
    };


    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, NXBleService.class);
        bindService(intent, this.serviceConnection, 1);
    }


    public NXDeviceAndroid getDevice() {
        return device;
    }
}
