package com.ztiany.androidx.jetpack.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.common.ifNull

private const val TAG = "LifecycleActivity"

private class Observer(private val tag: String) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "$tag-onCreate() called with: owner = $owner")
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d(TAG, "$tag-onStart() called with: owner = $owner")
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "$tag-onResume() called with: owner = $owner")
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "$tag-onPause() called with: owner = $owner")
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d(TAG, "$tag-onStop() called with: owner = $owner")
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "$tag-onDestroy() called with: owner = $owner")
        super.onDestroy(owner)
    }
}

class LifecycleActivity : AppCompatActivity() {

    private val observerA = Observer("A-1")
    private val observerP = Observer("P")
    private var observerAllA: Application.ActivityLifecycleCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        savedInstanceState.ifNull {
            supportFragmentManager.commit {
                add(R.id.fl_content, LifecycleHomeFragment())
            }
        }

        lifecycle.addObserver(observerA)
        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
        ProcessLifecycleOwner.get().lifecycle.addObserver(observerP)
    }

    override fun onStart() {
        super.onStart()
        lifecycle.removeObserver(observerA)
        lifecycle.addObserver(Observer("A-2"))
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            observerAllA = object : Application.ActivityLifecycleCallbacks {

                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    super.onActivityPreCreated(activity, savedInstanceState)
                    Log.d(TAG, "onActivityPreCreated() called with: activity = $activity, savedInstanceState = $savedInstanceState")
                }

                override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
                    super.onActivityPostCreated(activity, savedInstanceState)
                    Log.d(TAG, "onActivityPostCreated() called with: activity = $activity, savedInstanceState = $savedInstanceState")
                }

                override fun onActivityPreStarted(activity: Activity) {
                    super.onActivityPreStarted(activity)
                    Log.d(TAG, "onActivityPreStarted() called with: activity = $activity")
                }

                override fun onActivityPostStarted(activity: Activity) {
                    super.onActivityPostStarted(activity)
                    Log.d(TAG, "onActivityPostStarted() called with: activity = $activity")
                }

                override fun onActivityPreResumed(activity: Activity) {
                    super.onActivityPreResumed(activity)
                    Log.d(TAG, "onActivityPreResumed() called with: activity = $activity")
                }

                override fun onActivityPostResumed(activity: Activity) {
                    super.onActivityPostResumed(activity)
                    Log.d(TAG, "onActivityPostResumed() called with: activity = $activity")
                }

                override fun onActivityPrePaused(activity: Activity) {
                    super.onActivityPrePaused(activity)
                    Log.d(TAG, "onActivityPrePaused() called with: activity = $activity")
                }

                override fun onActivityPostPaused(activity: Activity) {
                    super.onActivityPostPaused(activity)
                    Log.d(TAG, "onActivityPostPaused() called with: activity = $activity")
                }

                override fun onActivityPreStopped(activity: Activity) {
                    super.onActivityPreStopped(activity)
                    Log.d(TAG, "onActivityPreStopped() called with: activity = $activity")
                }

                override fun onActivityPostStopped(activity: Activity) {
                    super.onActivityPostStopped(activity)
                    Log.d(TAG, "onActivityPostStopped() called with: activity = $activity")
                }

                override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
                    super.onActivityPreSaveInstanceState(activity, outState)
                    Log.d(TAG, "onActivityPreSaveInstanceState() called with: activity = $activity, outState = $outState")
                }

                override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
                    super.onActivityPostSaveInstanceState(activity, outState)
                    Log.d(TAG, "onActivityPostSaveInstanceState() called with: activity = $activity, outState = $outState")
                }

                override fun onActivityPreDestroyed(activity: Activity) {
                    super.onActivityPreDestroyed(activity)
                    Log.d(TAG, "onActivityPreDestroyed() called with: activity = $activity")
                }

                override fun onActivityPostDestroyed(activity: Activity) {
                    super.onActivityPostDestroyed(activity)
                    Log.d(TAG, "onActivityPostDestroyed() called with: activity = $activity")
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    Log.d(TAG, "onActivityCreated() called with: activity = $activity, savedInstanceState = $savedInstanceState")
                }

                override fun onActivityStarted(activity: Activity) {
                    Log.d(TAG, "onActivityStarted() called with: activity = $activity")
                }

                override fun onActivityResumed(activity: Activity) {
                    Log.d(TAG, "onActivityResumed() called with: activity = $activity")
                }

                override fun onActivityPaused(activity: Activity) {
                    Log.d(TAG, "onActivityPaused() called with: activity = $activity")
                }

                override fun onActivityStopped(activity: Activity) {
                    Log.d(TAG, "onActivityStopped() called with: activity = $activity")
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                    Log.d(TAG, "onActivitySaveInstanceState() called with: activity = $activity, outState = $outState")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    Log.d(TAG, "onActivityDestroyed() called with: activity = $activity")
                }
            }
            observerAllA?.let(::registerActivityLifecycleCallbacks)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            observerAllA?.let(::unregisterActivityLifecycleCallbacks)
        }
    }

}