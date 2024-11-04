package me.ztiany.bt.kit.arch

import android.view.View
import androidx.viewbinding.ViewBinding

/** This class can be used when there is no xml layout to generate a ViewBinding instance. */
class DynamicViewBinding<out T : View>(private val rootView: T) : ViewBinding {

    override fun getRoot(): T {
        return rootView
    }

}