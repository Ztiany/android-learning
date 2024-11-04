package me.ztiany.bt.kit.ble

import timber.log.Timber
import java.util.concurrent.ConcurrentLinkedQueue

class OperationQueue {

    private val receivers = mutableListOf<OperationReceiver>()




    @Synchronized
    fun addOperationReceiver(receiver: OperationReceiver) {
        receivers.add(receiver)
    }

    @Synchronized
    fun removeOperationReceiver(receiver: OperationReceiver) {
        receivers.remove(receiver)
    }

    interface OperationReceiver {
        fun onExecute(operation: BleOperation)
    }

}