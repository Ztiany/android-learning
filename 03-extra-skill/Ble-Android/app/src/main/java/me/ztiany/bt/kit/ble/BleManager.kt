package me.ztiany.bt.kit.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private const val DELAY_FOR_STOP_SCAN = 100L
private const val DEFAULT_SCAN_TIME = 10000L

enum class BleState {
    IDLE,
    SCANNING
}

@SuppressLint("MissingPermission")
object BleManager {

    ///////////////////////////////////////////////////////////////////////////
    // Coroutine related
    ///////////////////////////////////////////////////////////////////////////

    private val singleExecutor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private fun CoroutineScope.launchSingle(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        launch(context + singleExecutor, block = block)
    }

    private fun <T> CoroutineScope.asyncSingle(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> T,
    ) = async(context + singleExecutor, block = block)

    fun launchOnMain(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch(block = block)
    }

    fun launchOnSingle(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launchSingle(block = block)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Ble related
    ///////////////////////////////////////////////////////////////////////////

    private val _btResults = MutableStateFlow(emptyList<BtResult>())
    val btResults = _btResults.asStateFlow()

    private val _state = MutableStateFlow(BleState.IDLE)
    val state = _state.asStateFlow()

    private val _errorState = MutableStateFlow(-1)
    val errorState = _errorState.asStateFlow()

    private val channels = mutableListOf<BleChannel>()

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val scanCallback = object : ScanCallback() {

        /*
         * callbackType 表示扫描的类型，有三种：
         *
         *   - CALLBACK_TYPE_ALL_MATCHES: 这是最常用的类型，表示扫描到一个设备，并且这个设备满足了所有的过滤条件。
         *   - CALLBACK_TYPE_FIRST_MATCH: 表示扫描到一个设备，并且这个设备是第一个满足过滤条件的设备。
         *   - CALLBACK_TYPE_MATCH_LOST: 表示之前扫描到的一个设备，现在已经不在扫描范围内了。
         */
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Timber.d("callbackType = $callbackType, onScanResult: $result")
            handleScanResult(callbackType, result)
        }

        /**
         * 可能的错误有：
         *
         *  - SCAN_FAILED_ALREADY_STARTED: 表示扫描已经开始，但是又调用了 startScan() 方法。
         *  - SCAN_FAILED_APPLICATION_REGISTRATION_FAILED: 表示 app 无法注册扫描。
         *  - SCAN_FAILED_FEATURE_UNSUPPORTED: 表示设备不支持扫描。
         *  - SCAN_FAILED_INTERNAL_ERROR: 表示扫描过程中发生了内部错误。
         *  - SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES: 表示没有足够的硬件资源来执行扫描。
         *  - SCAN_FAILED_SCANNING_TOO_FREQUENTLY: 表示扫描太频繁。
         */
        override fun onScanFailed(errorCode: Int) {
            Timber.e("onScanFailed: $errorCode")
            _errorState.update { errorCode }
            if (SCAN_FAILED_ALREADY_STARTED != errorCode) {
                _state.update { BleState.IDLE }
            }
        }
    }

    private fun handleScanResult(type: Int, result: ScanResult) {
        if (state.value != BleState.SCANNING) {
            Timber.w("handleScanResult is called. But BleManager is not scanning.")
            return
        }

        coroutineScope.launchSingle {
            _btResults.update { currentResults ->
                when (type) {
                    ScanSettings.CALLBACK_TYPE_MATCH_LOST -> {
                        currentResults.filter { old -> old.scanResult.device.address != result.device.address }
                    }

                    ScanSettings.CALLBACK_TYPE_ALL_MATCHES, ScanSettings.CALLBACK_TYPE_FIRST_MATCH -> {
                        currentResults.indexOfFirst { old -> old.scanResult.device.address == result.device.address }.let {
                            if (it == -1) {
                                currentResults + BtResult(scanResult = result)
                            } else {
                                currentResults.toMutableList().apply {
                                    set(it, BtResult(scanResult = result))
                                }
                            }
                        }
                    }

                    else -> currentResults
                }
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // bind related
    ///////////////////////////////////////////////////////////////////////////

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            with(intent) {
                if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                    val device = parcelableExtraCompat<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val previousBondState = getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)
                    val bondState = getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                    val bondTransition = "${previousBondState.toBondStateDescription()} to " +
                            bondState.toBondStateDescription()
                    Timber.w("${device?.address} bond state changed | $bondTransition")
                }
            }
        }

        private fun Int.toBondStateDescription() = when (this) {
            BluetoothDevice.BOND_BONDED -> "BONDED"
            BluetoothDevice.BOND_BONDING -> "BONDING"
            BluetoothDevice.BOND_NONE -> "NOT BONDED"
            else -> "ERROR: $this"
        }
    }

    /**
     * A backwards compatible approach of obtaining a parcelable extra from an [Intent] object.
     *
     * NOTE: Despite the docs stating that [Intent.getParcelableExtra] is deprecated in Android 13,
     * Google has confirmed in https://issuetracker.google.com/issues/240585930#comment6 that the
     * replacement API is buggy for Android 13, and they suggested that developers continue to use the
     * deprecated API for Android 13. The issue will be fixed for Android 14 (U).
     */
    private inline fun <reified T : Parcelable> Intent.parcelableExtraCompat(key: String): T? = when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    fun listenToBondStateChanges(context: Context) {
        context.applicationContext.registerReceiver(
            broadcastReceiver,
            IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // scan related
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 开始扫描 BLE 设备，扫描时间为 scanTime，单位为毫秒。
     */
    fun startScan(
        scanTime: Long = DEFAULT_SCAN_TIME,
        filters: List<ScanFilter> = emptyList(),
        settings: ScanSettings = ScanSettings.Builder().build(),
    ) {
        if (state.value != BleState.IDLE) {
            Timber.w("startScan is called, But BleManager is already scanning.")
            return
        }
        if (scanTime <= 0) {
            Timber.w("BleManager scanTime is not set, please remember to stop the scanning.")
        }

        _state.update { BleState.SCANNING }
        clearResults()
        Timber.d("BleManager start scanning.")

        // 用 Android5.0 新增的扫描 API，扫描返回的结果更友好，比如 BLE 广播数据以前是 byte[] scanRecord，
        // 而新 API 帮我们解析成 ScanRecord 类。
        val bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner()
        bluetoothLeScanner.startScan(filters, settings, scanCallback)

        // 旧 API 是 BluetoothAdapter.startLeScan(LeScanCallback callback) 方式扫描BLE蓝牙设备，如下：
        //bluetoothAdapter.startLeScan()

        if (scanTime > 0) {
            coroutineScope.launch {
                delay(scanTime)
                stopScan()
            }
        }
    }

    fun stopScan() {
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback)
        _state.compareAndSet(BleState.SCANNING, BleState.IDLE)
        clearResults()
    }

    private fun clearResults() {
        _btResults.update { emptyList() }
    }

    /**
     * 连接 BLE 设备：通过扫描 BLE 设备，根据设备名称区分出目标设备 targetDevice，下一步实现与目标设备的连接，在连接设备之前
     * 要停止搜索蓝牙。停止搜索一般需要一定的时间来完成，最好调用停止搜索函数之后加以 100ms 的延时，保证系统能够完全停止搜索蓝牙
     * 设备。停止搜索之后启动连接过程。
     */
    internal fun safeOperation(block: () -> Unit) {
        coroutineScope.launch {
            if (state.value == BleState.SCANNING) {
                stopScan()
                delay(DELAY_FOR_STOP_SCAN)
            }
            block()
        }
    }

    @Synchronized
    private fun newBleChannel(device: BluetoothDevice): BleChannel {
        return channels.find { it.device.address == device.address }
            ?: BleChannel(device).also {
                channels.add(it)
            }
    }

    @Synchronized
    private fun removeBleChannel(device: BluetoothDevice) {
        channels.removeAll { it.device.address == device.address }
    }

}