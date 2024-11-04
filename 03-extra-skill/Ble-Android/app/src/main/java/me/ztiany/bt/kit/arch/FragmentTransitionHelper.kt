package me.ztiany.bt.kit.arch

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.base.delegate.fragment.FragmentDelegate
import java.lang.ref.WeakReference

typealias TransitionEndAction = () -> Unit

class FragmentTransitionHelper : FragmentDelegate<Fragment> {

    private lateinit var host: Fragment

    private var fragmentTransitions: FragmentTransitions = FragmentConfig.defaultFragmentTransitions()

    private val handler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    private val enterTransitionEndActions by lazy(LazyThreadSafetyMode.NONE) { mutableListOf<TransitionEndAction>() }

    private var isFirstEnter = true

    private var enterTransitionEnded = false

    private var isUsingSharedElement = false

    private var animationDisabled = false

    private var originalBackground: Drawable? = null

    private var originalElevation = 0F

    private var onCreateAnimationIsCalled = false
    private var enterTransitionEndNotified = false

    override fun onAttachToFragment(fragment: Fragment) {
        host = fragment
    }

    fun changeTransitions(transitions: FragmentTransitions) {
        this.fragmentTransitions = transitions
    }

    fun disableTransitions() {
        animationDisabled = true
    }

    fun markSharedElementUsed(used: Boolean) {
        isUsingSharedElement = used
    }

    private val noneAnimFixed: Animation
        get() = object : Animation() {}

    private fun getViewOriginAttribute(view: View?) {
        view ?: return
        originalBackground = view.background
        originalElevation = view.elevation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enterTransitionEnded = savedInstanceState?.getBoolean(KEY_FOR_TRANSITION_STATE) ?: false
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_FOR_TRANSITION_STATE, enterTransitionEnded)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // for those fragments that don't have any animation or are added as a root fragment.
        if (!onCreateAnimationIsCalled) {
            notifyFragmentEnterTransitionEnded()
        }
    }

    fun onCreateAnimation(targetView: View?, transit: Int, enter: Boolean): Animation? {
        onCreateAnimationIsCalled = true
        targetView ?: return null

        if (animationDisabled) {
            return noneAnimFixed
        }
        val context = targetView.context

        getViewOriginAttribute(targetView)

        return when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> {
                if (enter) {
                    fragmentTransitions.makeOpenEnterAnimation(context)?.apply {
                        setOpenEnterViewAttribute(WeakReference(targetView), this)
                    } ?: noneAnimFixed.apply {
                        notifyFragmentEnterTransitionEnded()
                    }
                } else {
                    fragmentTransitions.makeOpenExitAnimation(context)?.apply {
                        setOpenExitViewAttribute(WeakReference(targetView), this)
                    } ?: noneAnimFixed
                }
            }

            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> {
                if (enter) {
                    fragmentTransitions.makeCloseEnterAnimation(context)?.apply {
                        setCloseEnterViewAttribute(WeakReference(targetView), this)
                    } ?: noneAnimFixed
                } else {
                    fragmentTransitions.makeCloseExitAnimation(context)?.apply {
                        setCloseExitViewAttribute(WeakReference(targetView), this)
                    } ?: noneAnimFixed
                }
            }

            else -> {
                if (isUsingSharedElement && enter) {
                    notifyFragmentEnterTransitionEnded()
                }
                return null
            }
        }
    }

    private fun setCloseExitViewAttribute(targetView: WeakReference<View?>, animation: Animation) {
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                targetView.get()?.let {
                    fragmentTransitions.makeCloseExitAttributes(it.context)?.applyToView(it)
                }
            }

            override fun onAnimationEnd(animation: Animation?) {
                targetView.get()?.let {
                    it.background = originalBackground
                    it.elevation = originalElevation
                }
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
    }

    private fun setCloseEnterViewAttribute(targetView: WeakReference<View?>, animation: Animation) {
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                targetView.get()?.let {
                    fragmentTransitions.makeCloseEnterAttributes(it.context)?.applyToView(it)
                }
            }

            override fun onAnimationEnd(animation: Animation?) {
                targetView.get()?.let {
                    it.background = originalBackground
                    it.elevation = originalElevation
                }
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
    }

    private fun setOpenExitViewAttribute(targetView: WeakReference<View?>, animation: Animation) {
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                targetView.get()?.let {
                    fragmentTransitions.makeOpenExitAttributes(it.context)?.applyToView(it)
                }
            }

            override fun onAnimationEnd(animation: Animation?) {
                targetView.get()?.let {
                    it.background = originalBackground
                    it.elevation = originalElevation
                }
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
    }

    private fun setOpenEnterViewAttribute(targetView: WeakReference<View?>, animation: Animation) {
        // AnimationListener is not reliable.
        handler.postDelayed(transitionEndedNotifierAction, animation.duration)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                targetView.get()?.let {
                    fragmentTransitions.makeOpenEnterAttributes(it.context)?.applyToView(it)
                }
            }

            override fun onAnimationEnd(animation: Animation?) {
                targetView.get()?.let {
                    it.background = originalBackground
                    it.elevation = originalElevation
                }
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        })
    }

    private fun com.android.base.fragment.anim.TransitingAttribute.applyToView(targetView: View) {
        val original = originalBackground
        if (original == null || (original is ColorDrawable && original.color == android.graphics.Color.TRANSPARENT)) {
            background?.let { targetView.background = it }
        }
        targetView.elevation = originalElevation + elevation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            isFirstEnter = false
        }
    }

    override fun onDestroyView() {
        handler.removeCallbacks(transitionEndedNotifierAction)
    }

    private val transitionEndedNotifierAction = Runnable {
        notifyFragmentEnterTransitionEnded()
    }

    private fun notifyFragmentEnterTransitionEnded() {
        if (enterTransitionEndNotified) {
            return
        }

        enterTransitionEndNotified = true
        enterTransitionEnded = true

        with(enterTransitionEndActions.listIterator()) {
            while (hasNext()) {
                next().invoke()
                remove()
            }
        }
    }

    fun invokeOnEnterTransitionEnd(action: TransitionEndAction) {
        if (enterTransitionEnded || !isFirstEnter) {
            action()
            return
        }
        enterTransitionEndActions.add(action)
    }

    companion object {
        private const val KEY_FOR_TRANSITION_STATE = "enterTransitionEnded"
    }

}