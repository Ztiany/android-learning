package me.ztiany.bt.kit.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import java.util.UUID

sealed interface BleOperation {
    class Connect(val context: Context) : BleOperation

    class Disconnect(val reason: Int = ErrorType.NORMAL) : BleOperation

    class CharacteristicWrite(
        val device: BluetoothDevice,
        val characteristicUuid: UUID,
        /**
         * The write type to perform. should be [BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT] or [BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE],
         */
        val writeType: Int,
        val payload: ByteArray,
    ) : BleOperation
}

sealed interface BleChannelState {
    data object DISCONNECTING : BleChannelState

    data object CONNECTING : BleChannelState

    data object CONNECTED : BleChannelState

    data class DISCONNECTED(val error: Int) : BleChannelState
}

annotation class ErrorType {
    companion object {
        const val NORMAL = 0
        const val NO_SERVICE = -1
    }
}