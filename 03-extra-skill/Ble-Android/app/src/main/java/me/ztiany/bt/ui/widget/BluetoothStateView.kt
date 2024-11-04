package me.ztiany.bt.ui.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.FrameLayout
import android.widget.TextView
import me.ztiany.bt.kit.ble.State
import me.ztiany.bt.kit.sys.dp
import java.util.WeakHashMap


private val cache = WeakHashMap<Activity, TextView>()

@SuppressLint("SetTextI18n")
fun Activity.installBluetoothStateView() {
    if (cache.containsKey(this)) {
        return
    }

    val bluetoothStateView = TextView(this).apply {
        setBackgroundColor(android.graphics.Color.parseColor("#80FF0000"))
        setTextColor(android.graphics.Color.parseColor("#FFFFFF"))
        setPadding(
            this@installBluetoothStateView.dp(16),
            this@installBluetoothStateView.dp(18),
            this@installBluetoothStateView.dp(16),
            this@installBluetoothStateView.dp(18)
        )
        textSize = 10F
        text = "BBS: UNKNOWN"
    }

    this.findViewById<FrameLayout>(android.R.id.content)
        .addView(
            bluetoothStateView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
            })

    bluetoothStateView.bringToFront()

    cache[this] = bluetoothStateView
}

fun Activity.showBluetoothState(state: State) {
    cache[this]?.text = when (state) {
        State.UNKNOWN -> "BBS: UNKNOWN"
        State.DISABLED -> "BBS: DISABLED"
        State.REJECTED -> "BBS: REJECTED"
        State.DENIED -> "BBS: DENIED"
        State.ENABLED -> "BBS: ENABLED"
        State.UNAVAILABLE -> "BBS: UNAVAILABLE"
    }
}

