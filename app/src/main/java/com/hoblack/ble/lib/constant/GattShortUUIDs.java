package com.hoblack.ble.lib.constant;

public class GattShortUUIDs {

    public static final String SERVICE_BATTERY_UUID = "180F";
    public static final String SERVICE_SHORT_UUID = "FF80";

    public static final String CHARACT_SHORT_UUID_BATTERY_R = "2A19";
    //    public static final String SERVICE_SHORT_UUID = "BF3E";
    public static final String CHARACT_SHORT_UUID_uuid_RW = "FF81";
    public static final String CHARACT_SHORT_UUID_MAJOR_RW = "FF82";
    public static final String CHARACT_SHORT_UUID_MINOR_RW = "FF83";
    public static final String CHARACT_SHORT_UUID_INTERNAL_RW = "FF84";
    public static final String CHARACT_SHORT_UUID_CONNECTABLE_RW = "FF85";
    public static final String CHARACT_SHORT_UUID_TXPOWER_RW = "FF86";
    public static final String CHARACT_SHORT_UUID_TIME_RW = "FF87";
    public static final String CHARACT_SHORT_UUID_NAME_RW = "FF88";
    public static final String CHARACT_SHORT_UUID_FAST_CON_W = "FF89";
    public static final String CHARACT_SHORT_UUID_PAIR_W = "FF8A";
    public static final String CHARACT_SHORT_UUID_PAIR_N = "FF8B";
    public static final String CHARACT_SHORT_UUID_HUMITURE_SW_RW = "FF91";
    public static final String CHARACT_SHORT_UUID_HUMITURE_R = "FF92";
    public static final String CHARACT_SHORT_UUID_ACC_SW_RW = "FF93";
    public static final String CHARACT_SHORT_UUID_ACC_N = "FF94";
    public static final String CHARACT_SHORT_UUID_MOVEDECT_SW_RW = "FFA1";
    public static final String CHARACT_SHORT_UUID_BROAD_TIME_RW = "FFA2";
    public static final String CHARACT_SHORT_UUID_URL_RW = "FFF2";
    public static final String CHARACT_SHORT_UUID_ID_RW = "FFF1";
    public static final String CHARACT_SHORT_UUID_MODE_BROAD_RW = "FFFF";

    public static final String CLIENT_CHARACTERISTIC_CONFIGURATION = "2902";


    public static final String PASSWORD = "888888";


    public static String toFullUUidString(String shortUUID) {
        if (shortUUID == null || shortUUID.length() != 4) {
            throw new IllegalArgumentException("null or length not equal 4");
        }
        return "0000" + shortUUID + "-0000-1000-8000-00805F9B34FB";
//        return "98B5" + shortUUID + "-F473-A390-DE68-602ED238551B";
    }

    
}

