package me.ztiany.bt.kit.arch

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.content.res.ResourcesCompat

class HorizontalTransitions : FragmentTransitions {

    override fun makeOpenEnterAnimation(context: Context): Animation {
        val translateAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 1F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F
        )
        translateAnimation.interpolator = DecelerateInterpolator()

        val animationSet = AnimationSet(true)
        animationSet.duration = 300
        animationSet.addAnimation(translateAnimation)
        return animationSet
    }

    override fun makeOpenExitAnimation(context: Context): Animation {
        val translateAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, -0.5F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F
        )

        val alphaAnimation = AlphaAnimation(1.0F, 0F)
        alphaAnimation.fillAfter = true
        alphaAnimation.interpolator = DecelerateInterpolator()

        val animationSet = AnimationSet(true)
        animationSet.duration = 300
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(alphaAnimation)
        return animationSet
    }

    override fun makeCloseEnterAnimation(context: Context): Animation {
        val translateAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, -0.26F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F
        )

        val alphaAnimation = AlphaAnimation(0.3F, 1F)

        val animationSet = AnimationSet(true)
        animationSet.duration = 300
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(alphaAnimation)
        return animationSet
    }

    override fun makeCloseExitAnimation(context: Context): Animation {
        val translateAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 1F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F,
            TranslateAnimation.RELATIVE_TO_PARENT, 0F
        )
        translateAnimation.interpolator = AccelerateInterpolator()

        val animationSet = AnimationSet(true)
        animationSet.duration = 300
        animationSet.addAnimation(translateAnimation)
        return animationSet
    }

    override fun makeOpenEnterAttributes(context: Context): TransitingAttribute {
        return TransitingAttribute(context.dip(2F), context.getWindowBackground())
    }

    override fun makeOpenExitAttributes(context: Context): TransitingAttribute {
        return TransitingAttribute(0F, context.getWindowBackground())
    }

    override fun makeCloseEnterAttributes(context: Context): TransitingAttribute {
        return TransitingAttribute(0F, context.getWindowBackground())
    }

    override fun makeCloseExitAttributes(context: Context): TransitingAttribute {
        return TransitingAttribute(context.dip(2F), context.getWindowBackground())
    }

    private fun Context.dip(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    }

    private fun Context.getWindowBackground(): Drawable? {
        val a: TypedArray = theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.windowBackground)
        )
        val background = a.getResourceId(0, 0)
        a.recycle()
        return ResourcesCompat.getDrawable(resources, background, theme)
    }

}