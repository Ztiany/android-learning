package me.ztiany.apm

import com.bytedance.android.bytehook.ByteHook

class JNIBridge {

    // Used to load the 'hook' library on application startup.
    companion object {
        init {
            ByteHook.init();
            System.loadLibrary("apm")
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Memory Monitor
    ///////////////////////////////////////////////////////////////////////////

    external fun hookMemoryAllocation()

    external fun dumpMemoryAllocationInfo()

}