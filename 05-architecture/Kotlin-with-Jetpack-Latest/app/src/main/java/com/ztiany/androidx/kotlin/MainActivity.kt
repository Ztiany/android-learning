package com.ztiany.androidx.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.androidx.kotlin.coroutines.CoroutineWithGlobalScopeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun viewModelWithGlobalScope(view: View) {
        startActivity(Intent(this, CoroutineWithGlobalScopeActivity::class.java))
    }

}
