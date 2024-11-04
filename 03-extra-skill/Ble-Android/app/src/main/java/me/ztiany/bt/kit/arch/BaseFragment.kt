package me.ztiany.bt.kit.arch

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.android.base.delegate.State
import com.android.base.delegate.fragment.FragmentDelegate
import com.android.base.delegate.fragment.FragmentDelegateOwner
import com.android.base.delegate.helper.FragmentDelegates
import timber.log.Timber

/**
 *基础 BaseFragment 封装。
 *
 * @author Ztiany
 */
open class BaseFragment : Fragment(), OnBackPressListener, FragmentDelegateOwner, LoadingViewHost {

    private var lastDialogShowTime: Long = 0

    private var loadingViewHost: LoadingViewHost? = null

    private val fragmentDelegates by lazy(LazyThreadSafetyMode.NONE) { FragmentDelegates(this) }

    protected val backPressedCallback by lazy(LazyThreadSafetyMode.NONE) {
        object : OnBackPressedCallback(false) {
            init {
                activity?.onBackPressedDispatcher?.addCallback(this@BaseFragment, this)
            }

            override fun handleOnBackPressed() = this@BaseFragment.handleOnBackPressed()
        }
    }

    private fun tag() = this.javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.tag(tag()).d("onAttach() called with: context = [$context]")
        fragmentDelegates.callOnAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(tag()).d("-->onCreate  savedInstanceState  =  $savedInstanceState")
        fragmentDelegates.callOnCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Timber.tag(tag()).d("-->onCreateView  savedInstanceState = %s", savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(tag()).d("-->onViewCreated  savedInstanceState = %s", savedInstanceState)
        fragmentDelegates.callOnViewCreated(view, savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.tag(tag()).d("-->onActivityCreated savedInstanceState  =  $savedInstanceState")
        fragmentDelegates.callOnActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(tag()).d("-->onStart")
        fragmentDelegates.callOnStart()
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(tag()).d("-->onResume")
        fragmentDelegates.callOnResume()
    }

    override fun onPause() {
        Timber.tag(tag()).d("-->onPause")
        fragmentDelegates.callOnPause()
        super.onPause()
    }

    override fun onStop() {
        Timber.tag(tag()).d("-->onStop")
        fragmentDelegates.callOnStop()
        super.onStop()
    }

    override fun onDestroyView() {
        Timber.tag(tag()).d("-->onDestroyView")
        fragmentDelegates.callOnDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.tag(tag()).d("-->onDestroy")
        fragmentDelegates.callOnDestroy()
        dismissLoadingDialog()
        super.onDestroy()
    }

    override fun onDetach() {
        Timber.tag(tag()).d("-->onDetach")
        fragmentDelegates.callOnDetach()
        super.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        fragmentDelegates.callOnSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Timber.tag(tag()).d("-->setUserVisibleHint = $isVisibleToUser")
        fragmentDelegates.callSetUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.tag(tag()).d("-->onHiddenChanged = $hidden")
        fragmentDelegates.callOnHiddenChanged(hidden)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fragmentDelegates.callOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentDelegates.callOnActivityResult(requestCode, resultCode, data)
    }

    @UiThread
    override fun addDelegate(fragmentDelegate: FragmentDelegate<*>) {
        fragmentDelegates.addDelegate(fragmentDelegate)
    }

    @UiThread
    override fun removeDelegate(fragmentDelegate: FragmentDelegate<*>): Boolean {
        return fragmentDelegates.removeDelegate(fragmentDelegate)
    }

    @UiThread
    override fun removeDelegateWhile(predicate: (FragmentDelegate<*>) -> Boolean) {
        fragmentDelegates.removeDelegateWhile(predicate)
    }

    @UiThread
    override fun findDelegate(predicate: (FragmentDelegate<*>) -> Boolean): FragmentDelegate<*>? {
        return fragmentDelegates.findDelegate(predicate)
    }

    override fun getCurrentState(): State {
        return fragmentDelegates.getCurrentState()
    }

    final override fun onBackPressed(): Boolean {
        return handleBackPressed() || fragmentHandleBackPressed()
    }

    /**
     * This is the traditional way to handle back press event. This method only works when its host
     * [Activity] is [BaseActivity] and the [BaseActivity] has enabled the traditional back press handling.
     * To enable the traditional back press handling, you need to call [BaseActivity.enableTraditionalBackPressHandling].
     *
     * return true if you have handled the back press event and prevent the default behavior.
     */
    protected open fun handleBackPressed() = false

    /**
     * If you want to handle back press event, you can override this method. But to make it work,
     * you need to call [enable] on the [backPressedCallback] to enable it. Once the back conditions are met,
     * you need to call [disable] on the [backPressedCallback] to disable it.
     */
    protected open fun handleOnBackPressed() = Unit

    private fun loadingView(): LoadingViewHost {
        val loadingViewImpl = loadingViewHost
        return if (loadingViewImpl != null) {
            loadingViewImpl
        } else {
            loadingViewHost = onCreateLoadingView() ?: LoadingViewHost.internalLoadingViewHostFactory?.invoke(requireContext())
            loadingViewHost ?: throw NullPointerException("you need to config LoadingViewFactory in Sword or implement onCreateLoadingView.")
        }
    }

    protected open fun onCreateLoadingView(): LoadingViewHost? {
        return null
    }

    override fun showLoadingDialog(): Dialog {
        lastDialogShowTime = System.currentTimeMillis()
        return loadingView().showLoadingDialog(true)
    }

    override fun showLoadingDialog(cancelable: Boolean): Dialog {
        lastDialogShowTime = System.currentTimeMillis()
        return loadingView().showLoadingDialog(cancelable)
    }

    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean): Dialog {
        lastDialogShowTime = System.currentTimeMillis()
        return loadingView().showLoadingDialog(message, cancelable)
    }

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean): Dialog {
        lastDialogShowTime = System.currentTimeMillis()
        return loadingView().showLoadingDialog(messageId, cancelable)
    }

    override fun dismissLoadingDialog() {
        loadingViewHost?.dismissLoadingDialog()
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)?) {
        dismissDialog(lastDialogShowTime, minimumMills, onDismiss)
    }

    override fun isLoadingDialogShowing(): Boolean {
        return loadingViewHost != null && loadingView().isLoadingDialogShowing()
    }

    override fun showMessage(message: CharSequence) {
        loadingView().showMessage(message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        loadingView().showMessage(messageId)
    }

}