package com.ztiany.buglytinker.sample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()

        if (Build.VERSION.SDK_INT >= 23) {
            val checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                //必须允许
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {

        tvCurrentVersion.text = "当前版本：" + getCurrentVersion(this)

        //kill进程
        btnKillSelf.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
        }

        //加载本地补丁
        btnLoadPatch.setOnClickListener {
            Beta.applyTinkerPatch(applicationContext,
                    Environment.getExternalStorageDirectory().absolutePath + "/patch_signed_7zip.apk")
        }


        btnLoadLibrary.setOnClickListener {
            Toast.makeText(this, "后续", Toast.LENGTH_LONG).show()
        }

        btnDownloadPatch.setOnClickListener {
            Beta.downloadPatch()
        }

        btnPatchDownloaded.setOnClickListener {
            Beta.applyDownloadedPatch()
        }

        btnCheckUpgrade.setOnClickListener {
            Beta.checkUpgrade()
        }

        testJava.setOnClickListener {
            Bug1Tools().doBug1(this)
        }

        testKotlin.setOnClickListener {
            doBug2(this)
        }

    }


    /**
     * 获取当前版本.
     *
     * @param context 上下文对象
     * @return 返回当前版本
     */
    private fun getCurrentVersion(context: Context): String {
        try {
            val packageInfo = context.packageManager.getPackageInfo(this.packageName, PackageManager.GET_CONFIGURATIONS)
            val versionCode = packageInfo.versionCode
            val versionName = packageInfo.versionName
            return "$versionName.$versionCode"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
