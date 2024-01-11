package me.ztiany.compose.facility.utils

import android.view.MenuItem

fun MenuItem.onClick(onClick: () -> Unit) {
    setOnMenuItemClickListener {
        onClick()
        true
    }
}