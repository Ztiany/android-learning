package com.ztiany.androidx.jetpack.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.androidx.kotlin.databinding.ActivitiesFirstBinding

private const val TAG = "FirstActivity"

class FirstActivity : AppCompatActivity() {

    private val vb by lazy { ActivitiesFirstBinding.inflate(layoutInflater) }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        Log.d(TAG, "getContent: ${uri.toString()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        vb.btnGetContent.setOnClickListener {
            getContent.launch("image/*")
        }
    }

}