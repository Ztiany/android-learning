package me.ztiany.apm.utils

import android.app.Activity
import android.graphics.Color
import android.widget.FrameLayout
import android.widget.TextView

fun Activity.setDescription(description: String) {
    setContentView(FrameLayout(this).apply {
        addView(
            TextView(this@setDescription).apply {
                text = description
                textSize = 16F
                setTextColor(Color.RED)
                setPadding(dp(16), dp(16), dp(16), dp(16))
            },
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT).apply {
                gravity = android.view.Gravity.CENTER
            })
    })
}