package com.hoblack.ble.lib.constant;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ClsUtils {
    
    public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method method = btClass.getMethod("createBond", new Class[0]);
        Boolean res = (Boolean) method.invoke(btDevice, new Object[0]);
        return res.booleanValue();
    }


    public static boolean removeBond(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice) throws Exception {
        Method method = btClass.getMethod("removeBond");
        return (Boolean) method.invoke(btDevice);
    }


    public static boolean setPin(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice, String str)
            throws Exception {
        try {
            Method method = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
            Boolean res = (Boolean) method.invoke(btDevice, new Object[]{str
                    .getBytes()});
            Log.e("returnValue", "" + res);
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return true;
    }


    public static boolean cancelPairingUserInput(Class<? extends BluetoothDevice> btClass, BluetoothDevice device) throws Exception {
        Method method = btClass.getMethod("cancelPairingUserInput");
        return (Boolean) method.invoke(device, new Object[0]);
    }


    public static boolean cancelBondProcess(Class<? extends BluetoothDevice> btClass, BluetoothDevice device) throws Exception {
        Method method = btClass.getMethod("cancelBondProcess");
        return (Boolean) method.invoke(device);
    }


    public static void printAllInform(Class clsShow) {
        try {
            Method[] arrayOfMethod = clsShow.getMethods();
            for (int i = 0;
                 i < arrayOfMethod.length; i++) {
                Log.e("method name", arrayOfMethod[i].getName() + ";and the i is:" + i);
            }


            Field[] arrayOfField = clsShow.getFields();
            for (int i = 0; i < arrayOfField.length; i++) {
                Log.e("Field name", arrayOfField[i].getName());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }
}


