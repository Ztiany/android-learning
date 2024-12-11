package com.ztiany.test.deviceid

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.IGetter
import com.github.gzuliyujiang.oaid.OAIDLog
import com.ztiany.test.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class DeviceIDActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OAIDLog.enable()
        setContentView(R.layout.activity_device_id)
    }

    fun getPermission(view: View) {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.READ_PHONE_STATE)
            .onGranted { }
            .onDenied {}
            .start()
    }

    fun getDeviceId(view: View) {
        Log.d("DeviceIDActivity", "设备ID：" + DeviceIdUtil.getDeviceId(this))
    }

    fun getDeviceIdBySDK(view: View) {
        val builder = StringBuilder()
        builder.appendLine()
        builder.append("UniqueID: ")
        // 获取设备唯一标识，只支持Android 10之前的系统，需要READ_PHONE_STATE权限，可能为空
        // 获取设备唯一标识，只支持Android 10之前的系统，需要READ_PHONE_STATE权限，可能为空
        val uniqueID = DeviceID.getUniqueID(this)
        if (TextUtils.isEmpty(uniqueID)) {
            builder.append("DID/IMEI/MEID获取失败")
        } else {
            builder.append(uniqueID)
        }
        builder.append("\n")
        builder.append("AndroidID: ")
        // 获取安卓ID，可能为空
        // 获取安卓ID，可能为空
        val androidID = DeviceID.getAndroidID(this)
        if (TextUtils.isEmpty(androidID)) {
            builder.append("AndroidID获取失败")
        } else {
            builder.append(androidID)
        }
        builder.append("\n")
        builder.append("WidevineID: ")
        // 获取数字版权管理ID，可能为空
        // 获取数字版权管理ID，可能为空
        val widevineID = DeviceID.getWidevineID(this)
        if (TextUtils.isEmpty(widevineID)) {
            builder.append("WidevineID获取失败")
        } else {
            builder.append(widevineID)
        }
        builder.append("\n")
        builder.append("PseudoID: ")
        // 获取伪造ID，根据硬件信息生成，不会为空，有大概率会重复
        // 获取伪造ID，根据硬件信息生成，不会为空，有大概率会重复
        builder.append(DeviceID.getPseudoID())
        builder.append("\n")
        builder.append("GUID: ")
        // 获取GUID，随机生成，不会为空
        // 获取GUID，随机生成，不会为空
        builder.append(DeviceID.getGUID(this))
        builder.append("\n")
        // 是否支持OAID/AAID
        // 是否支持OAID/AAID
        builder.append("supported:").append(DeviceID.supportedOAID(this))
        builder.append("\n")

        // 获取OAID/AAID，异步回调
        // 获取OAID/AAID，异步回调
        DeviceID.getOAID(this, object : IGetter {
            override fun onOAIDGetComplete(result: String) {
                // 不同厂商的OAID/AAID格式是不一样的，可进行MD5、SHA1之类的哈希运算统一
                builder.append("OAID/AAID: ").append(result)

                Log.d("DeviceIDActivity", "ClientIdMD5：$builder")
            }

            override fun onOAIDGetError(error: Throwable) {
                // 获取OAID/AAID失败
                builder.append("OAID/AAID: 失败，").append(error)
                Log.d("DeviceIDActivity", "ClientIdMD5：$builder")
            }
        })
    }

}