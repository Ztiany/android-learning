package com.ztiany.view.drawable

import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.View
import com.google.android.material.ripple.RippleDrawableCompat
import com.ztiany.view.R
import com.ztiany.view.SimpleLayoutFragment

/** RippleDrawable 是 Android 5.0 引入的，感觉：RippleDrawable 不太适合和 SelectorDrawable 一起用，也应该只用于一些可触控的控件上，比如 Button。*/
class RippleDrawableFragment : SimpleLayoutFragment() {

    override fun getLayoutId() = R.layout.drawable_fragment_ripple

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}