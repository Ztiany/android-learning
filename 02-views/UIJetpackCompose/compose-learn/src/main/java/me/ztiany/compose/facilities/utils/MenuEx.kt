package me.ztiany.compose.theme

import android.view.MenuItem

fun MenuItem.onClick(onClick: () -> Unit) {
    setOnMenuItemClickListener {
        onClick()
        true
    }
}