package me.ztiany.bt.kit.ble

import android.bluetooth.le.ScanResult

data class BtResult(
    val type: Int = 0,
    val state: Int = 0,
    val scanResult: ScanResult,
)