package me.ztiany.mmkv.simulation

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.enjoy.mmkv.MMKV
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*
import xcrash.XCrash
import java.io.File

class MainActivity : AppCompatActivity() {

    private val nativeBridge by lazy { NativeBridge() }

    private val mmkv by lazy {
        MMKV.initialize(this)
        MMKV.defaultMMKV() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        askPermission()

        btnInit.setOnClickListener {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "mmap/test.txt")
            file.createNew()
            nativeBridge.mmapInit(file.absolutePath)
        }

        btnMMapWrite.setOnClickListener {
            nativeBridge.mmapWrite()
        }

        btnMMapRead.setOnClickListener {
            nativeBridge.mmapRead()
        }

        btnTestMMVK.setOnClickListener {
            performMMKV()
        }

    }

    private fun performMMKV() {
        mmkv.putInt("a", 1)
        Log.d(TAG, "a = " + mmkv.getInt("a", 0))
        mmkv.putInt("b", -1)
        Log.d(TAG, "b = " + mmkv.getInt("b", 0))
    }

    private fun askPermission() {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .onGranted {
                val initParameters = XCrash.InitParameters().apply {
                    this.setLogDir(this@MainActivity.getExternalFilesDir("")!!.absolutePath)
                }
                XCrash.init(this, initParameters)
            }
            .onDenied {
                supportFinishAfterTransition()
            }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeBridge.destroy()
    }

    companion object {
        private const val TAG = "MMKV"
    }

}
