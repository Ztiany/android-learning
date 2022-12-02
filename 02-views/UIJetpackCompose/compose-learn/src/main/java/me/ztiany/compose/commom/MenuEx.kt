package me.ztiany.compose.commom

import android.view.MenuItem

fun MenuItem.onClick(onClick: () -> Unit) {
    setOnMenuItemClickListener {
        onClick()
        true
    }
}