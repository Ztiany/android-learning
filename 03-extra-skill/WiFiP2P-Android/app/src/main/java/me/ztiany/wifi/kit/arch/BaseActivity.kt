package me.ztiany.wifi.kit.arch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.base.delegate.State
import com.android.base.delegate.activity.ActivityDelegate
import com.android.base.delegate.activity.ActivityDelegateOwner
import com.android.base.delegate.helper.ActivityDelegates
import timber.log.Timber

/**
 * A base class of [AppCompatActivity] which provides:
 *
 * 1. Encapsulation of common processes.
 * 2. An implementation of [ActivityDelegateOwner] to manage [ActivityDelegate].
 * 3. [onBackPressed] event dispatch, priority is given to [Fragment] to handle.
 *
 * @author Ztiany
 */
abstract class BaseActivity : AppCompatActivity(), ActivityDelegateOwner {

    private val activityDelegates by lazy(LazyThreadSafetyMode.NONE) { ActivityDelegates(this) }

    private var traditionalBackPressHandlingEnabled = false

    private fun tag() = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag(tag()).d("---->onCreate before call super")
        initialize(savedInstanceState)
        activityDelegates.callOnCreateBeforeSetContentView(savedInstanceState)
        super.onCreate(savedInstanceState)
        Timber.tag(tag()).d("---->onCreate after call super  bundle = $savedInstanceState")

        when (val layout = provideLayout()) {
            is View -> setContentView(layout)
            is Int -> setContentView(layout)
            null -> Timber.d("layout() return null layout")
            else -> throw IllegalArgumentException("layout() return type no support, layout = $layout")
        }

        activityDelegates.callOnCreateAfterSetContentView(savedInstanceState)
        setUpLayout(savedInstanceState)
    }

    override fun onRestart() {
        Timber.tag(tag()).d("---->onRestart before call super")
        super.onRestart()
        Timber.tag(tag()).d("---->onRestart after call super  ")
        activityDelegates.callOnRestart()
    }

    override fun onStart() {
        Timber.tag(tag()).d("---->onStart before call super")
        super.onStart()
        Timber.tag(tag()).d("---->onStart after call super")
        activityDelegates.callOnStart()
    }

    override fun onResume() {
        Timber.tag(tag()).d("---->onResume before call super")
        super.onResume()
        Timber.tag(tag()).d("---->onResume after call super")
        activityDelegates.callOnResume()
    }

    override fun onPause() {
        Timber.tag(tag()).d("---->onPause before call super")
        activityDelegates.callOnPause()
        super.onPause()
        Timber.tag(tag()).d("---->onPause after call super  ")
    }

    override fun onStop() {
        Timber.tag(tag()).d("---->onStop before call super")
        activityDelegates.callOnStop()
        super.onStop()
        Timber.tag(tag()).d("---->onStop after call super")
    }

    override fun onDestroy() {
        Timber.tag(tag()).d("---->onDestroy before call super")
        activityDelegates.callOnDestroy()
        super.onDestroy()
        Timber.tag(tag()).d("---->onDestroy after call super")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        activityDelegates.callOnPostCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityDelegates.callOnSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        activityDelegates.callOnRestoreInstanceState(savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityDelegates.callOnActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activityDelegates.callOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        activityDelegates.callOnResumeFragments()
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface impl
    ///////////////////////////////////////////////////////////////////////////
    @UiThread
    final override fun addDelegate(activityDelegate: ActivityDelegate<*>) {
        activityDelegates.addDelegate(activityDelegate)
    }

    @UiThread
    final override fun removeDelegate(activityDelegate: ActivityDelegate<*>): Boolean {
        return activityDelegates.removeDelegate(activityDelegate)
    }

    @UiThread
    override fun removeDelegateWhile(predicate: (ActivityDelegate<*>) -> Boolean) {
        activityDelegates.removeDelegateWhile(predicate)
    }

    @UiThread
    final override fun findDelegate(predicate: (ActivityDelegate<*>) -> Boolean): ActivityDelegate<*>? {
        return activityDelegates.findDelegate(predicate)
    }

    override fun getCurrentState(): State {
        return activityDelegates.getCurrentState()
    }

    /**
     * Before calling super.onCreate and setContentView
     *
     * @param savedInstanceState state
     */
    protected open fun initialize(savedInstanceState: Bundle?) {}

    /**
     * provide a layoutId (int) or layout (View)
     *
     * @return layoutId
     */
    protected open fun provideLayout(): Any? = null

    /**
     * after calling setContentView
     */
    protected abstract fun setUpLayout(savedInstanceState: Bundle?)

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (traditionalBackPressHandlingEnabled && activityHandleBackPress(this)) {
            Timber.d("onBackPressed() called but child fragment handle it")
        } else {
            handleOnBackPressed()
        }
    }

    /**
     * @see OnBackPressListener
     */
    protected open fun handleOnBackPressed() {
        super.onBackPressed()
    }

    /**
     * Enable traditional back pressed handling.
     */
    fun enableTraditionalBackPressHandling() {
        traditionalBackPressHandlingEnabled = true
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun isDestroyed(): Boolean {
        return if (Build.VERSION.SDK_INT >= 17) {
            super.isDestroyed()
        } else {
            getCurrentState() === State.DESTROY
        }
    }

}