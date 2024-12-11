package me.ztiany.bt.kit.arch

import android.content.Context
import android.view.animation.Animation

interface FragmentTransitions {

    fun makeOpenEnterAnimation(context: Context): Animation?

    fun makeOpenExitAnimation(context: Context): Animation?

    fun makeCloseEnterAnimation(context: Context): Animation?

    fun makeCloseExitAnimation(context: Context): Animation?

    fun makeOpenEnterAttributes(context: Context): TransitingAttribute?

    fun makeOpenExitAttributes(context: Context): TransitingAttribute?

    fun makeCloseEnterAttributes(context: Context): TransitingAttribute?

    fun makeCloseExitAttributes(context: Context): TransitingAttribute?

}