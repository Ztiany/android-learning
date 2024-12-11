package me.ztiany.wifi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import me.ztiany.wifi.kit.arch.BaseActivity
import me.ztiany.wifi.kit.arch.commit
import me.ztiany.wifi.kit.sys.ifNull
import me.ztiany.wifip2p.R

class MainActivity : BaseActivity() {

    override fun initialize(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
    }

    override fun provideLayout() = R.layout.activity_main

    override fun setUpLayout(savedInstanceState: Bundle?) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                addFragment(MainFragment())
            }
        }
    }

}