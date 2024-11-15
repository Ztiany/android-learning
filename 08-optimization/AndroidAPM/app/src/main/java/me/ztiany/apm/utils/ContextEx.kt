package me.ztiany.apm.utils

import android.content.Context
import android.view.View
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

val View.activityContext: FragmentActivity?
    get() = context.activityContext