package me.ztiany.wifi.kit.sys

import android.content.Context
import androidx.fragment.app.FragmentActivity

val Context.activityContext: FragmentActivity?
    get() {
        var context = this
        while (context is android.content.ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }