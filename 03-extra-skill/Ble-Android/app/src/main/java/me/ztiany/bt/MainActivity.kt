package me.ztiany.bt

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ztiany.bt.kit.arch.commit
import me.ztiany.bt.kit.ble.BluetoothSwitcher
import me.ztiany.bt.kit.sys.ifNull
import me.ztiany.bt.ui.widget.installBluetoothStateView
import me.ztiany.bt.ui.widget.showBluetoothState
import me.ztiany.ble.R

class MainActivity : AppCompatActivity() {

    private val bluetoothSwitcher = BluetoothSwitcher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        connectBluetoothState()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        installBluetoothStateView()

        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                addFragment(MainFragment())
            }
        }
    }

    private fun connectBluetoothState() {
        lifecycleScope.launch {
            bluetoothSwitcher.state.collectLatest { state ->
                showBluetoothState(state)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bluetoothSwitcher.startBluetoothEnablementProcedure()
    }

}