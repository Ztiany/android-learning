package com.ztiany.kotlin.anko.layouts

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-11 22:18
 */
/*
或者自定义View
 */
inline fun ViewManager.customView(theme: Int = 0) = customView(theme) {}

inline fun ViewManager.customView(theme: Int = 0, init: CustomView.() -> Unit): CustomView {
    return ankoView({ CustomView(it) }, theme, init)
}