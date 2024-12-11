package me.ztiany.apm.aspect.memory

import com.bytedance.android.bytehook.ByteHook
import timber.log.Timber

class MemoryTracker {

    companion object {
        init {
            val init = ByteHook.init()
            Timber.w("ByteHook init result: $init")
            System.loadLibrary("memory_tracker")
        }
    }

    external fun hookMemoryAllocation()

    external fun dumpMemoryAllocationInfo()

}