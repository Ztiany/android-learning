package me.ztiany.bt.kit.arch

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * This is the traditional implementation of back press handling. Now, it is disabled by default in [BaseActivity],
 * and you can enable it by calling [BaseActivity.enableTraditionalBackPressHandling].
 *
 * As per the [Basics of System Back video](https://www.youtube.com/watch?v=Elpqr5xpLxQ) , the whole point
 * of the Predictive Back gesture is to know ahead of time what is going to handle back. So, if you want to implement
 * Predictive Back gesture, You should use [ComponentActivity.onBackPressedDispatcher] instead of this.
 *
 * For more details, check out [stackoverflow.com/questions/75462234](https://stackoverflow.com/questions/75462234/proper-usage-of-onbackpresseddispatcher).
 */
interface OnBackPressListener {

    fun onBackPressed(): Boolean

}

internal fun activityHandleBackPress(fragmentActivity: FragmentActivity): Boolean {
    return handleBackPress(fragmentActivity.supportFragmentManager)
}

private fun handleBackPress(fragmentManager: FragmentManager): Boolean {
    val fragments = fragmentManager.fragments
    for (i in fragments.indices.reversed()) {
        val child = fragments[i]
        if (isFragmentBackHandled(child)) {
            return true
        }
    }
    return false
}

fun Fragment.fragmentHandleBackPressed(): Boolean {
    return handleBackPress(childFragmentManager)
}

private fun isFragmentBackHandled(fragment: Fragment?): Boolean {
    return (fragment != null && fragment.isVisible
            && fragment.userVisibleHint
            && fragment is OnBackPressListener
            && (fragment as OnBackPressListener).onBackPressed())
}