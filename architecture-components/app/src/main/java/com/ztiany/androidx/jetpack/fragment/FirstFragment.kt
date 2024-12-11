package com.ztiany.androidx.jetpack.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.databinding.FragmentsFirstBinding

private const val TAG = "FirstFragment"

/**
 * 学习：
 *
 * 1. [Activity Result API](https://developer.android.com/training/basics/intents/result#register)
 * 2. [Back-Handling With OnBackPressedDispatcher](https://code.kiwi.com/rewriting-android-apps-back-handling-logic-c2419dcb873c)
 */
class FirstFragment : Fragment() {

    private lateinit var vb: FragmentsFirstBinding
    private lateinit var takePhotoLiveData: TakePhotoLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        takePhotoLiveData = TakePhotoLiveData(requireActivity().activityResultRegistry)
        takePhotoLiveData.observe(this) {
            Log.d(TAG, "takePhotoLiveData: $it")
        }

        setFragmentResultListener(SECOND_FRAGMENT_KEY) { requestKey, bundle ->
            Log.d(TAG, "setFragmentResultListener: state: ${lifecycle.currentState}")
            Log.d(TAG, "setFragmentResultListener: requestKey = $requestKey, bundle = $bundle")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentsFirstBinding.inflate(inflater, container, false).apply {
            vb = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.btnStartSecond.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                addToBackStack(SecondFragment::class.java.name)
                hide(this@FirstFragment)
                setMaxLifecycle(this@FirstFragment, Lifecycle.State.CREATED)
                add(R.id.fl_content, SecondFragment(), SecondFragment::class.toString())
            }
        }

        vb.btnReplaceSecond.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                addToBackStack(SecondFragment::class.java.name)
                replace(R.id.fl_content, SecondFragment(), SecondFragment::class.toString())
            }
        }

        vb.btnGetContent.setOnClickListener {
            takePhotoLiveData.launch("image/*")
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onStop()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}

/** 参考 https://juejin.cn/post/6844904106528604167 */
private class TakePhotoLiveData(private val registry: ActivityResultRegistry) : LiveData<Uri>() {

    private lateinit var takePhotoLauncher: ActivityResultLauncher<String>

    override fun onActive() {
        super.onActive()
        takePhotoLauncher = registry.register("key", ActivityResultContracts.GetContent()) { result ->
            value = result
        }
    }

    override fun onInactive() {
        super.onInactive()
        takePhotoLauncher.unregister()
    }

    fun launch(type: String) {
        takePhotoLauncher.launch(type)
    }

}