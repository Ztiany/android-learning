package me.ztiany.wifi.kit.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding

/**
 *@author Ztiany
 */
abstract class BaseUIFragment<VB : ViewBinding> : BaseFragment() {

    protected val fragmentTransitionHelper = FragmentTransitionHelper().also {
        addDelegate(it)
    }

    private val reuseView by lazy(LazyThreadSafetyMode.NONE) { ReusableView() }

    private var _vb: VB? = null
    protected val vb: VB
        get() = checkNotNull(_vb) {
            "access this after onCreateView() is called."
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val factory = {
            _vb = provideViewBinding(inflater, container, savedInstanceState) ?: inflateBindingWithParameterizedType(layoutInflater, container, false)
            vb.root
        }
        return reuseView.createView(factory)
    }

    protected open fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB? = null

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (reuseView.isNotTheSameView(view)) {
            internalOnSetupCreatedView(view, savedInstanceState)
            vb.onSetupCreatedView(view, savedInstanceState)
        }
        super.onViewCreated(view, savedInstanceState)
        onViewPrepared(view, savedInstanceState)
    }

    internal open fun internalOnSetupCreatedView(view: View, savedInstanceState: Bundle?) {}

    /**
     * Called when the view is prepared. If [setReuseView] is called and passes true as the parameter, it will be called just once.
     *
     * @param view view of fragment.
     */
    protected open fun VB.onSetupCreatedView(view: View, savedInstanceState: Bundle?) {}

    /** Called when after [onSetupCreatedView] is called. */
    protected open fun onViewPrepared(view: View, savedInstanceState: Bundle?) {}

    /**
     * If you do heavy work in [onSetupCreatedView] or [onViewPrepared], you can use this method to delay the execution of the code,
     * or the heavy work during enter transition may cause the UI to freeze.
     */
    protected fun invokeOnEnterTransitionEnd(action: TransitionEndAction) {
        fragmentTransitionHelper.invokeOnEnterTransitionEnd(action)
    }

    /**
     * Call it before [onCreateView] is called.
     */
    protected fun setReuseView(reuseTheView: Boolean) {
        reuseView.reuseTheView = reuseTheView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (reuseView.destroyView()) {
            _vb = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return fragmentTransitionHelper.onCreateAnimation(view, transit, enter)
    }

    fun withVB(block: VB.() -> Unit) {
        _vb?.apply(block)
    }

}