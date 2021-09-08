package com.ztiany.androidx.kotlin.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class CoroutineWithGlobalScopeActivity : AppCompatActivity() {

    private val viewModel by viewModels<CoroutineWithGlobalScopeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.startLoad()
    }

}