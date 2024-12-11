package me.ztiany.bt.kit.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic

sealed interface BleEvent {

    class CharacteristicChanged(
        val device: BluetoothDevice,
        val characteristic: BluetoothGattCharacteristic,
        val value: ByteArray,
    ) : BleEvent

    class CharacteristicRead(
        val device: BluetoothDevice,
        val characteristic: BluetoothGattCharacteristic
    ) : BleEvent

    class CharacteristicWrite(
        val device: BluetoothDevice,
        val characteristic: BluetoothGattCharacteristic,
    ) : BleEvent

}