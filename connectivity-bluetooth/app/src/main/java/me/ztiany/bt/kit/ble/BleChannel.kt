package me.ztiany.bt.kit.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.concurrent.ConcurrentLinkedQueue


/** Maximum BLE MTU size as defined in gatt_api.h. */
private const val GATT_MAX_MTU_SIZE = 517
private const val GATT_MIN_MTU_SIZE = 23
private const val GATT_PACKET_HEADER_SIZE = 3

@SuppressLint("MissingPermission")
class BleChannel(val device: BluetoothDevice) {

    private var connectGatt: BluetoothGatt? = null

    private val operationQueue = ConcurrentLinkedQueue<BleOperation>()
    private var pendingOperation: BleOperation? = null

    private var mtuPayloadSize = GATT_MIN_MTU_SIZE - GATT_PACKET_HEADER_SIZE

    private val _state = MutableStateFlow<BleChannelState>(BleChannelState.DISCONNECTED(ErrorType.NORMAL))
    val state = _state.asStateFlow()

    private val deviceName: String
        get() = device.name ?: device.address

    private val gattCallback = object : BluetoothGattCallback() {

        // step3: 与设备建立连接之后，回调 onConnectionStateChange() 函数。
        @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Timber.d("${deviceName}-->onConnectionStateChange() called with: gatt = $gatt, status = $status, newState = $newState")
            status.printConnectionState(deviceName)

            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (newState) {
                    BluetoothGatt.STATE_CONNECTED -> {
                        Timber.d("${deviceName}-->Connected to GATT server.")
                        connectGatt = gatt
                        dismantleConnectionTimeout()
                        discoverServices()
                    }

                    BluetoothGatt.STATE_DISCONNECTED -> {
                        Timber.d("${deviceName}-->Disconnected from GATT server.")
                        connectGatt?.close()
                        connectGatt = null
                    }

                    BluetoothGatt.STATE_CONNECTING -> {
                        Timber.d("${deviceName}-->Connecting to GATT server.")
                        _state.update { BleChannelState.CONNECTING }
                    }

                    BluetoothGatt.STATE_DISCONNECTING -> {
                        Timber.d("${deviceName}-->Disconnecting from GATT server.")
                        _state.update { BleChannelState.DISCONNECTING }
                    }
                }
            } else {
                Timber.e("${deviceName}-->Failed to connect to GATT server.")
                disconnect(status)
            }
        }

        @RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Timber.d("${deviceName}-->onServicesDiscovered: $status")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.w("${deviceName}-->Discovered ${gatt.services.size} services for ${device.address}.")
                gatt.printGattTable()
                requestMtu(device, GATT_MAX_MTU_SIZE)
                _state.update { BleChannelState.CONNECTED }
                signalEndOfOperation()
            } else {
                Timber.e("${deviceName}-->Service discovery failed due to status $status")
                disconnect(ErrorType.NO_SERVICE)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            Timber.w("${deviceName}-->ATT MTU changed to $mtu, success: ${status == BluetoothGatt.GATT_SUCCESS}")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mtuPayloadSize = mtu - GATT_PACKET_HEADER_SIZE
                Timber.d("${deviceName}-->MTU changed to $mtuPayloadSize")
            }
            signalEndOfOperation()
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            Timber.d("onCharacteristicChanged() called with: gatt = $gatt, characteristic = $characteristic, value = $value")
            fireEvent(BleEvent.CharacteristicChanged(gatt.device, characteristic, value))
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            fireEvent(BleEvent.CharacteristicChanged(gatt.device, characteristic, characteristic.value))
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
            Timber.d("onCharacteristicRead() called with: gatt = $gatt, characteristic = $characteristic, value = $value, status = $status")
            val uuid = characteristic.uuid
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    Timber.i("Read characteristic $uuid | value: ${value.toHexString()}")
                    fireEvent(BleEvent.CharacteristicRead(gatt.device, characteristic))
                }

                BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                    Timber.e("Read not permitted for $uuid!")
                }

                else -> {
                    Timber.e("Characteristic read failed for $uuid, error: $status")
                }
            }
            signalEndOfOperation()
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Timber.d("onCharacteristicRead() called with: gatt = $gatt, characteristic = $characteristic, status = $status")
            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        Timber.i("Read characteristic $uuid | value: ${value.toHexString()}")
                        fireEvent(BleEvent.CharacteristicRead(gatt.device, this))
                    }

                    BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                        Timber.e("Read not permitted for $uuid!")
                    }

                    else -> {
                        Timber.e("Characteristic read failed for $uuid, error: $status")
                    }
                }
            }
            signalEndOfOperation()
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Timber.d("onCharacteristicWrite() called with: gatt = $gatt, characteristic = $characteristic, status = $status")
            val writtenValue = (pendingOperation as? BleOperation.CharacteristicWrite)?.payload
            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        Timber.i("Wrote to characteristic $uuid | value: ${writtenValue?.toHexString()}")
                        fireEvent(BleEvent.CharacteristicWrite(gatt.device, this))
                    }

                    BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
                        Timber.e("Write not permitted for $uuid!")
                    }

                    else -> {
                        Timber.e("Characteristic write failed for $uuid, error: $status")
                    }
                }
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int, value: ByteArray) {
            Timber.d("onDescriptorRead() called with: gatt = $gatt, descriptor = $descriptor, status = $status, value = $value")
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {

        }

        /*
         * 若开启监听成功则会回调 onDescriptorWrite() 方法，处理方式如下:
         */
        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor, status: Int) {
            Timber.d("onDescriptorWrite() called with: gatt = $gatt, descriptor = $descriptor, status = $status")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //开启监听成功，可以向设备写入命令了
                Timber.d("enable notification success. now you can write command to device.")
            }
        }
    } // end of gattCallback

    private fun setupDiscoveryTimeout() {

    }

    private fun setupConnectionTimeout() {

    }

    private fun dismantleDiscoveryTimeout() {

    }

    private fun dismantleConnectionTimeout() {

    }

    private fun connect(context: Context) {

    }

    // setupDiscoveryTimeout()
    private fun discoverServices() {

    }

    private fun requestMtu(device: BluetoothDevice, mtu: Int) {

    }

    private fun disconnect(status: Int = 0) {

    }

    @Synchronized
    private fun enqueueOperation(operation: BleOperation) {
        operationQueue.add(operation)
        if (pendingOperation == null) {
            doNextOperation()
        }
    }

    @Synchronized
    private fun signalEndOfOperation() {
        Timber.d("End of $pendingOperation")
        pendingOperation = null
        if (operationQueue.isNotEmpty()) {
            doNextOperation()
        }
    }

    private fun doNextOperation() {
        if (pendingOperation != null) {
            Timber.e("doNextOperation() called when an operation is pending! Aborting.")
            return
        }

        val operation = operationQueue.poll() ?: run {
            Timber.v("Operation queue empty, returning")
            return
        }

        pendingOperation = operation
        doOperation(operation)
    }

    private fun doOperation(operation: BleOperation) {
        Timber.d("${deviceName}-->doOperation: $operation")
    }

    private fun fireEvent(bleEvent: BleEvent) {

    }

}