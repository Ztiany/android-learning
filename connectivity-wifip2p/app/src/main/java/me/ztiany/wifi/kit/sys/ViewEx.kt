@file:JvmName("ViewEx")

package me.ztiany.wifi.kit.sys

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import me.ztiany.wifi.kit.sys.AndroidVersion.atLeast

val View.activityContext: FragmentActivity?
    get() {
        return context.activityContext
    }

/**
 * Find a view in the children of this view by id.
 */
@Suppress("UNCHECKED_CAST")
fun <V : View> View.findChild(@IdRes viewId: Int): V {
    return findViewById<View>(viewId) as V
}

inline val ViewGroup.views get() = (0 until childCount).map { getChildAt(it) }

fun View.layoutInflater(): LayoutInflater = LayoutInflater.from(context)

fun View.removeSelfFromParent() {
    val viewParent = parent
    if (viewParent != null && viewParent is ViewGroup) {
        viewParent.removeView(this)
    }
}

inline fun <T : View> T.doOnLayoutAvailable(crossinline block: T.() -> Unit) {
    //isLaidOut 方法作用：如果 view 已经通过至少一个布局，则返回true，因为它最后一次附加到窗口或从窗口分离。
    ViewCompat.isLaidOut(this).yes {
        block(this)
    }.otherwise {
        addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int,
            ) {
                removeOnLayoutChangeListener(this)
                block(this@doOnLayoutAvailable)
            }
        })
    }
}

inline fun <T : View> T.onGlobalLayoutOnce(crossinline action: T.() -> Unit) {
    val t: T = this
    t.viewTreeObserver
        .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                action.invoke(t)
                if (atLeast(16)) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    @Suppress("DEPRECATION")
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
            }
        })
}

/** Set every padding of the view to the given padding. */
fun View.setPaddings(padding: Int) {
    this.setPadding(padding, padding, padding, padding)
}

fun View.setHorizontalPadding(padding: Int) {
    this.setPadding(padding, paddingTop, padding, paddingBottom)
}

fun View.setVerticalPadding(padding: Int) {
    this.setPadding(paddingLeft, padding, paddingRight, padding)
}

fun View.setLeftPadding(padding: Int) {
    this.setPadding(padding, paddingTop, paddingRight, paddingBottom)
}

fun View.setRightPadding(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, padding, paddingBottom)
}

fun View.setTopPadding(padding: Int) {
    this.setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun View.setBottomPadding(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, padding)
}

fun View.setTopMargin(
    topMargin: Int,
    layoutParamsCreator: (() -> ViewGroup.MarginLayoutParams)? = null,
) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.topMargin = topMargin
    } else {
        layoutParams = layoutParamsCreator?.invoke() ?: newWWMarginLayoutParams().apply {
            this.topMargin = topMargin
        }
    }
}

fun View.setBottomMargin(
    bottomMargin: Int,
    layoutParamsCreator: (() -> ViewGroup.MarginLayoutParams)? = null,
) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.bottomMargin = bottomMargin
    } else {
        layoutParams = layoutParamsCreator?.invoke() ?: newWWMarginLayoutParams().apply {
            this.bottomMargin = bottomMargin
        }
    }
}

fun View.setLeftMargin(
    leftMargin: Int,
    layoutParamsCreator: (() -> ViewGroup.MarginLayoutParams)? = null,
) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.leftMargin = leftMargin
    } else {
        layoutParams = layoutParamsCreator?.invoke() ?: newWWMarginLayoutParams().apply {
            this.leftMargin = leftMargin
        }
    }
}

fun View.setRightMargin(
    rightMargin: Int,
    layoutParamsCreator: (() -> ViewGroup.MarginLayoutParams)? = null,
) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.rightMargin = rightMargin
    } else {
        layoutParams = layoutParamsCreator?.invoke() ?: newWWMarginLayoutParams().apply {
            this.rightMargin = rightMargin
        }
    }
}

fun View.setMargins(
    leftMargin: Int,
    topMargin: Int,
    rightMargin: Int,
    bottomMargin: Int,
    layoutParamsCreator: (() -> ViewGroup.MarginLayoutParams)? = null,
) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.rightMargin = rightMargin
        params.leftMargin = leftMargin
        params.bottomMargin = bottomMargin
        params.topMargin = topMargin
    } else {
        layoutParams = layoutParamsCreator?.invoke() ?: newWWMarginLayoutParams().apply {
            this.rightMargin = rightMargin
            this.leftMargin = leftMargin
            this.bottomMargin = bottomMargin
            this.topMargin = topMargin
        }
    }
}

fun View.setWidth(width: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.height = height
    layoutParams = params
}

fun View.setSize(width: Int, height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    params.height = height
    layoutParams = params
}