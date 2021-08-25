package com.ztiany.test.deviceid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class DeviceID {

    private static final String TAG = "DeviceID";

    private static String sFakeDeviceId = "";

    private final static AtomicBoolean ATOMIC_BOOLEAN = new AtomicBoolean(false);

    /**
     * 获取手机的设备 ID。
     *
     * @return DeviceId
     */
    public static String get(Context context) {
        String deviceId;
        deviceId = getIMEI(context);

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getAndroidId(context);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = mockFakeDeviceId(context);
        }

        Log.d(TAG, "finial device %s" + deviceId);

        return deviceId;
    }

    private static String mockFakeDeviceId(Context context) {
        while (TextUtils.isEmpty(sFakeDeviceId)) {
            if (ATOMIC_BOOLEAN.compareAndSet(false, true)) {
                String uuid = getUuidFromSystemSettings(context);
                if (TextUtils.isEmpty(uuid)) {
                    uuid = getUuidFromExternalStorage(context);
                }
                if (TextUtils.isEmpty(uuid)) {
                    uuid = getUuidFromSharedPreferences(context);
                }

                if (TextUtils.isEmpty(uuid)) {
                    uuid = UUID.randomUUID().toString();
                    Log.d(TAG, "Generate uuid by random: " + uuid);
                }

                saveUuidToSharedPreferences(context, uuid);
                saveUuidToSystemSettings(context, uuid);
                saveUuidToExternalStorage(context, uuid);

                sFakeDeviceId = uuid;
            }
        }

        return sFakeDeviceId;
    }

    private static String getUuidFromSystemSettings(Context context) {
        String uuid = Settings.System.getString(context.getContentResolver(), "GUID_uuid");
        Log.d(TAG, "Get uuid from system settings: " + uuid);
        return uuid;
    }

    private static void saveUuidToSystemSettings(Context context, String uuid) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(context)) {
            try {
                Settings.System.putString(context.getContentResolver(), "GUID_uuid", uuid);
                Log.d(TAG, "Save uuid to system settings: " + uuid);
            } catch (Exception e) {
                Log.e(TAG, "saveUuidToSystemSettings", e);
            }
        } else {
            Log.d(TAG, "android.permission.WRITE_SETTINGS not granted");
        }
    }

    private static String getUuidFromExternalStorage(Context context) {
        String uuid = "";
        File file = getGuidFile(context);
        if (file != null && file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                uuid = reader.readLine();
            } catch (Exception e) {
                Log.d(TAG, "getUuidFromExternalStorage", e);
            }
        }
        Log.d(TAG, "Get uuid from external storage: " + uuid);
        return uuid;
    }

    private static void saveUuidToExternalStorage(Context context, String uuid) {
        File file = getGuidFile(context);
        if (file == null) {
            Log.d(TAG, "UUID file in external storage is null");
            return;
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(uuid);
            writer.flush();
            Log.d(TAG, "Save uuid to external storage: " + uuid);
        } catch (Exception e) {
            Log.d(TAG, "saveUuidToExternalStorage", e);
        }
    }

    private static File getGuidFile(Context context) {
        boolean hasStoragePermission;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            hasStoragePermission = true;
        } else if (Build.VERSION.SDK_INT >= 30) {
            hasStoragePermission = false;
        } else {
            hasStoragePermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        if (hasStoragePermission && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return new File(Environment.getExternalStorageDirectory(), "Android/.gemini_telecom_uuid");
        }
        return null;
    }

    private static void saveUuidToSharedPreferences(Context context, String uuid) {
        SharedPreferences preferences = context.getSharedPreferences("GUID", Context.MODE_PRIVATE);
        preferences.edit().putString("uuid", uuid).apply();
        Log.d(TAG, "Save uuid to shared preferences: " + uuid);
    }

    private static String getUuidFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("GUID", Context.MODE_PRIVATE);
        String uuid = preferences.getString("uuid", "");
        Log.d(TAG, "Get uuid from shared preferences: " + uuid);
        return uuid;
    }

    /**
     * ANDROID_ID 是设备的系统首次启动时随机生成的一串字符，由 16 个 16 进制数（64 位）组成，理论上可以保证唯一性的。ANDROID_ID 的获取门槛是最低的，不需要任何权限。
     * <p>
     * 存在的问题：
     *     <ol>
     *         <li>无法保证稳定性，root、刷机或恢复出厂设置都会导致设备的 ANDROID_ID 发生改变。</li>
     *         <li>某些厂商定制系统的 Bug 会导致不同的设备可能会产生相同的 ANDROID_ID（比如常见的 9774d56d682e549c），而且某些设备获取到的 ANDROID_ID 为 null。</li>
     *     </ol>
     * </p>
     */
    private static String getAndroidId(Context context) {
        String androidId;

        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if ("9774d56d682e549c".equals(androidId)) {
                androidId = "";
            }
        } catch (Exception ignored) {
            androidId = "";
        }

        Log.d(TAG, "get androidId " + androidId);
        return androidId;
    }

    /**
     * IMEI(International Mobile Equipment Identity) 是国际移动设备识别码的缩写，由 15-17 位数字组成，与手机是一一对应的关系，该码是全球唯一的，并且永远不会改变。
     * <p>
     * 获取方法：
     *     <ol>
     *         <li>在Android 8.0（API Level 26）以下，可以通过 TelephonyManager 的 getDeviceId() 方法获取到设备的 IMEI 码（其实这个说法不准确，该方法是会根据手机设备的制式（GSM 或 CDMA）返回相应的设备码（IMEI、MEID 和 ESN））</li>
     *         <li>getDeviceId()  方法在 Android 8.0 及之后的版本已经被废弃了，取而代之的是 getImei() 方法。</li>
     *         <li>无论是 getDeviceId() 方法还是 getImei() 方法都可以传入一个参数 slotIndex。</li>
     *     </ol>
     *     限制：
     *     <ol>
     *         <li>Android 6.0 以下：无需申请权限，可以通过 getDeviceId() 方法获取到 IMEI 码。</li>
     *         <li>Android 6.0-Android 8.0：需要申请 READ_PHONE_STATE 权限，可以通过 getDeviceId() 方法获取到 IMEI 码，如果没有权限直接获取，会抛出 java.lang.SecurityException 异常</li>
     *         <li>Android 8.0-Android 10：需要申请 READ_PHONE_STATE 权限，可以通过 getImei() 方法获取到 IMEI 码，如果没有权限直接获取，会抛出 java.lang.SecurityException 异常</li>
     *         <li>
     *             Android 10 及以上：分为以下两种情况：
     *             <ol>
     *                 <li>targetSdkVersion < 29：没有申请权限的情况，通过 getImei() 方法获取 IMEI 码时抛出 java.lang.SecurityException 异常；申请了权限，通过 getImei() 方法获取到 IMEI 码为 null</li>
     *                 <li>targetSdkVersion >= 29：无论是否申请了权限，通过 getImei() 方法获取 IMEI 码时都会直接抛出 java.lang.SecurityException 异常</li>
     *             </ol>
     *         </li>
     *     </ol>
     * </p>
     */
    @SuppressLint({"MissingPermission"})
    private static String getIMEI(Context context) {
        String imei;

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = tm.getImei();
            } else {
                imei = tm.getDeviceId();
            }
        } catch (Exception ignored) {
            imei = "";
        }

        Log.d(TAG, "get IMEI " + imei);
        return imei;
    }

}