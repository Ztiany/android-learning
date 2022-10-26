package com.ztiany.androidx.jetpack.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ztiany.androidx.kotlin.databinding.ActivitiesFirstBinding

private const val TAG = "FirstActivity"

/**
 * 学习：
 *
 * 1. [Activity Result API](https://developer.android.com/training/basics/intents/result#register)
 */
class FirstActivity : AppCompatActivity() {

    private val vb by lazy { ActivitiesFirstBinding.inflate(layoutInflater) }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle the returned Uri
        Log.d(TAG, "getContent: ${uri.toString()}")
    }

    private val getRingtone = registerForActivityResult(PickRingtone()) { uri ->
        Log.d(TAG, "getRingtone: ${uri.toString()}")
    }

    private val getResultFromSecondActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.d(TAG, "getResultFromSecondActivity: $it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        vb.btnGetContent.setOnClickListener {
            getContent.launch("image/*")
        }

        vb.btnGetRingtone.setOnClickListener {
            getRingtone.launch(1)
        }

        vb.btnGetFromSecond.setOnClickListener {
            getResultFromSecondActivity.launch(Intent(this, SecondActivity::class.java))
        }
    }

}

/** 创建自定义协定 */
private class PickRingtone : ActivityResultContract<Int, Uri?>() {

    override fun createIntent(context: Context, input: Int) = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
        putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
    }
}