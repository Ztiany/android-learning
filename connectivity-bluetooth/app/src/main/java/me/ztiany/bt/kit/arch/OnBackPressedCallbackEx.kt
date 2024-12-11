package me.ztiany.bt.kit.arch

import androidx.activity.OnBackPressedCallback

fun OnBackPressedCallback.enable() {
    isEnabled = true
}

fun OnBackPressedCallback.disable() {
    isEnabled = false
}