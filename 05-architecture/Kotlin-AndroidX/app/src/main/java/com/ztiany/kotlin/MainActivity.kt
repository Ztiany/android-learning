package com.ztiany.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.ztiany.kotlin.coroutines.CoroutineWithGlobalScopeActivity
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun viewModelWithGlobalScope(view: View) {
        startActivity(Intent(this, CoroutineWithGlobalScopeActivity::class.java))
    }

}
