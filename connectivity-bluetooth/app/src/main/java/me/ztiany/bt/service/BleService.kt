package me.ztiany.bt.service

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.ParcelUuid
import android.os.RemoteCallbackList
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import me.ztiany.ble.BleServer
import me.ztiany.ble.BleServerListener
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class BleService : Service() {

    companion object {
        // Random UUID for our service known between the client and server to allow communication
        val SERVICE_UUID: UUID = UUID.fromString("00002222-0000-1000-8000-00805f9b34fb")

        // Same as the service but for the characteristic
        val CHARACTERISTIC_READ_UUID: UUID = UUID.fromString("00001111-0000-1000-8000-00805f9b34fc")
        val CHARACTERISTIC_WRITE_UUID: UUID = UUID.fromString("00001111-0000-1000-8000-00805f9b34fd")
        val DESCRIPTOR_NOTIFICATION_UUID: UUID = UUID.fromString("00001111-0000-1000-8000-00805f9b34fe")
    }

    private val scope = CoroutineScope(SupervisorJob())

    private var state = AtomicInteger(State.SERVER_STOPPED)
    private val listeners: RemoteCallbackList<BleServerListener> = RemoteCallbackList()

    private val channels = ConcurrentHashMap<String, BluetoothDevice>()

    ///////////////////////////////////////////////////////////////////////////
    // Bluetooth related fields
    ///////////////////////////////////////////////////////////////////////////

    private val manager: BluetoothManager by lazy {
        applicationContext.getSystemService() ?: throw IllegalStateException("BluetoothManager not available")
    }

    private val advertiser: BluetoothLeAdvertiser
        get() = manager.adapter.bluetoothLeAdvertiser

    // 必须要开启可连接的 BLE 广播，其它设备才能发现并连接 BLE 服务端。
    private val service = BluetoothGattService(
        SERVICE_UUID,
        BluetoothGattService.SERVICE_TYPE_PRIMARY
    ).apply {
        val readCharacteristic = BluetoothGattCharacteristic(
            CHARACTERISTIC_READ_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ,
        ).apply {
            addDescriptor(
                BluetoothGattDescriptor(
                    DESCRIPTOR_NOTIFICATION_UUID,
                    BluetoothGattDescriptor.PERMISSION_WRITE
                )
            )
        }

        val writeCharacteristic = BluetoothGattCharacteristic(
            CHARACTERISTIC_WRITE_UUID,
            BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        addCharacteristic(readCharacteristic)
        addCharacteristic(writeCharacteristic)
    }

    private var server: BluetoothGattServer? = null

    private val bleServiceBinder by lazy { BleServiceBinder() }

    private val sampleServerCallback by lazy { SampleServerCallback() }
    private val sampleAdvertiseCallback by lazy { SampleAdvertiseCallback() }

    override fun onCreate() {
        super.onCreate()
        Timber.i("BleService onCreate")
    }

    override fun onBind(intent: Intent): IBinder {
        Timber.i("BleService onBind")
        return bleServiceBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("BleService onDestroy")
        stopGattServer()
    }

    private fun startGattServer(): Boolean {
        // If we are missing permission stop the service.
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (hasPermission) {
            startInForeground()
            server = manager.openGattServer(applicationContext, sampleServerCallback)
            val result = server?.addService(service)
            Timber.i("startGattServer, result: $result")
            if (result == true) {
                updateState(State.SERVER_STARTED)
            }
        }

        return hasPermission
    }

    private fun stopGattServer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            advertiser.stopAdvertising(sampleAdvertiseCallback)
        }
        server?.close()
        scope.cancel()
        updateState(State.SERVER_STOPPED)
    }

    private fun startAdvertising(): Boolean {
        // 广播设置
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED) // 广播模式：平衡
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM) // 发射功率：中等
            .setConnectable(true) // 是否可以连接
            .setTimeout(0) // 广播超时时间，0 表示一直广播
            .build()

        // 广播数据
        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true) // 包含设备名称
            .setIncludeTxPowerLevel(true) // 包含发射功率
            .addServiceUuid(ParcelUuid(SERVICE_UUID)) // 设置广播的 UUID
            .build()

        //扫描响应数据（可选，当客户端扫描时才发送）
        val scanResponse = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .setIncludeTxPowerLevel(true)
            .addServiceUuid(ParcelUuid(SERVICE_UUID))
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                this@BleService,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            manager.adapter.name = "Alien's BLE ${Build.MODEL}"
            advertiser.startAdvertising(settings, data, scanResponse, sampleAdvertiseCallback)
            return true
        }

        return false
    }

    private fun stopAdvertising() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                this@BleService,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            advertiser.stopAdvertising(sampleAdvertiseCallback)
        }
        updateState(State.ADVERTISING_STOPPED)
    }

    private fun updateState(newState: Int) {
        val updated = state.updateAndGet {
            newState
        }
        val count = listeners.beginBroadcast()
        for (i in 0 until count) {
            listeners.getBroadcastItem(i).onServerStateChanged(updated)
        }
    }

    private inner class SampleServerCallback : BluetoothGattServerCallback() {

        override fun onServiceAdded(status: Int, service: BluetoothGattService?) {
            Timber.i("onServiceAdded, status: $status, service: $service")
        }

        override fun onConnectionStateChange(
            device: BluetoothDevice,
            status: Int,
            newState: Int,
        ) {
            Timber.i("onConnectionStateChange, device: $device, status: $status, newState: $newState")
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                channels[device.address] = device
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                channels.remove(device.address)
            }
        }

        /*
         * 客户端请求写入特征值时触发。处理方式：读取客户端写入的特征值，然后回复响应。
         */
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray,
        ) {
            Timber.i("onCharacteristicWriteRequest, device: $device, requestId: $requestId, preparedWrite: $preparedWrite, responseNeeded: $responseNeeded, offset: $offset, value: ${value.contentToString()}")
            // Here you should apply the write of the characteristic and notify connected devices that it changed

            // If response is needed reply to the device that the write was successful
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                    this@BleService,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                if (responseNeeded) {
                    server?.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        offset,
                        value,
                    )
                }

                val response = "echo: ${value.contentToString()}"
                val characteristicRead = service.getCharacteristic(CHARACTERISTIC_READ_UUID)
                characteristicRead.setValue(response.toByteArray())
                server?.notifyCharacteristicChanged(device, characteristicRead, false)
            }
        }

        override fun onCharacteristicReadRequest(
            device: BluetoothDevice,
            requestId: Int,
            offset: Int,
            characteristic: BluetoothGattCharacteristic,
        ) {
            Timber.i("onCharacteristicReadRequest, device: $device, requestId: $requestId, offset: $offset, characteristic: $characteristic")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                    this@BleService,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                server?.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    0,
                    byteArrayOf(0x01),
                )
            }
        }

        /*
         * This method is called when a remote device has requested to write to a descriptor.
         * 特征被写入：当回复响应成功后，客户端会写入，然后触发本方法。
         * bluetoothGattServer.sendResponse 会触发客户端的 onCharacteristicWriteRequest 方法。
         */
        override fun onDescriptorWriteRequest(
            device: BluetoothDevice,
            requestId: Int,
            descriptor: BluetoothGattDescriptor,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray?,
        ) {
            Timber.i("onDescriptorWriteRequest, device: $device, requestId: $requestId, preparedWrite: $preparedWrite, responseNeeded: $responseNeeded, offset: $offset, value: ${value?.contentToString()}")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                    this@BleService,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                if (responseNeeded) {
                    server?.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        0,
                        null,
                    )
                }
            }
        }

        /*
         * This method is called when a remote device has requested to read a descriptor.
         * 特征被读取：当回复响应成功后，客户端会读取，然后触发本方法。
         */
        override fun onDescriptorReadRequest(device: BluetoothDevice, requestId: Int, offset: Int, descriptor: BluetoothGattDescriptor) {
            Timber.i("onDescriptorReadRequest, device: $device, requestId: $requestId, offset: $offset, descriptor: $descriptor")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || (ActivityCompat.checkSelfPermission(
                    this@BleService,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                server?.sendResponse(
                    device,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    byteArrayOf(0x03),
                )
            }
        }

        override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
            Timber.i("onNotificationSent, device: $device, status: $status")
        }

        override fun onExecuteWrite(device: BluetoothDevice?, requestId: Int, execute: Boolean) {
            Timber.i("onExecuteWrite, device: $device, requestId: $requestId, execute: $execute")
        }

        override fun onMtuChanged(device: BluetoothDevice?, mtu: Int) {
            Timber.i("onMtuChanged, device: $device, mtu: $mtu")
        }
    }

    private inner class SampleAdvertiseCallback : AdvertiseCallback() {

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Timber.i("onStartSuccess, settingsInEffect: $settingsInEffect")
            updateState(State.ADVERTISING_STARTED)
        }

        override fun onStartFailure(errorCode: Int) {
            Timber.e("onStartFailure, errorCode: $errorCode")
            if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED/*Already started*/) {
                return
            }
            updateState(State.ADVERTISING_STOPPED)
        }
    }

    private inner class BleServiceBinder : BleServer.Stub() {

        override fun startGattServer(): Int {
            Timber.i("startGattServer")
            this@BleService.startGattServer()
            return OPERATION_OK
        }

        override fun stopGattServer(): Int {
            Timber.i("stopGattServer")
            this@BleService.stopGattServer()
            return OPERATION_OK
        }

        override fun startAdvertising(): Int {
            if (state.get() == State.SERVER_STOPPED) {
                Timber.e("startAdvertising, server not started")
                return ERROR_SERVER_NOT_STARTED
            }
            Timber.i("startAdvertising")
            this@BleService.startAdvertising()
            return OPERATION_OK
        }

        override fun stopAdvertising(): Int {
            Timber.i("stopAdvertising")
            this@BleService.stopAdvertising()
            return OPERATION_OK
        }

        override fun getServerState(): Int {
            return state.get()
        }

        override fun registerBleServerListener(listener: BleServerListener) {
            listener.onServerStateChanged(state.get())
            listeners.register(listener)
        }

        override fun unregisterBleServerListener(listener: BleServerListener) {
            listeners.unregister(listener)
        }
    }

}