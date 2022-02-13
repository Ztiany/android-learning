package com.ztiany.androidx.jetpack.viewmodel

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ztiany.androidx.common.TargetFragment
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.databinding.ViewmodelFragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "ViewModelHomeFragment"
private const val EVENT_TAG = "EVENT_TAG"

@AndroidEntryPoint
class ViewModelHomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private var binding: ViewmodelFragmentHomeBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach() called with: context = $context")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
        subscribeViewModel()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewmodelFragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")
        subscribeViewModelFlow()

        binding?.run {

            viewmodelBtnLoad.setOnClickListener {
                viewModel.doSomething()
                viewModel.doSomethingLiveData().observe(viewLifecycleOwner) {
                    Log.d(EVENT_TAG, "liveData-viewLifecycleOwner-$it")
                }
                viewModel.doSomethingSingleLiveData().observe(viewLifecycleOwner) {
                    Log.d(EVENT_TAG, "singleLiveData-viewLifecycleOwner-$it")
                }
            }

            viewmodelBtnRecreate.setOnClickListener {
                activity?.recreate()
            }

            viewmodelBtnReplaceTarget.setOnClickListener {
                requireActivity().supportFragmentManager.commit {
                    replace(R.id.fl_content, TargetFragment(), TargetFragment::class.qualifiedName)
                        .addToBackStack(TargetFragment::class.qualifiedName)
                }
            }

            viewmodelBtnAddTarget.setOnClickListener {
                requireActivity().supportFragmentManager.commit {
                    add(R.id.fl_content, TargetFragment(), TargetFragment::class.qualifiedName)
                        .addToBackStack(TargetFragment::class.qualifiedName)
                        .setMaxLifecycle(this@ViewModelHomeFragment, Lifecycle.State.STARTED)
                        .hide(this@ViewModelHomeFragment)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun subscribeViewModelFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedFlowRepeat0.onEach {
                    Log.d(EVENT_TAG, "sharedFlow0-repeated-view-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "sharedFlow0-repeated-view-onCompletion")
                }.launchIn(this)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedFlowRepeat1.onEach {
                    Log.d(EVENT_TAG, "sharedFlow1-repeated-view-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "sharedFlow1-repeated-view-onCompletion")
                }.launchIn(this)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowRepeat.onEach {
                    Log.d(EVENT_TAG, "stateFlow-repeated-view-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "stateFlow-repeated-view-onCompletion")
                }.launchIn(this)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.channelRepeat.onEach {
                    Log.d(EVENT_TAG, "channel-repeated-view-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "channel-repeated-view-onCompletion")
                }.launchIn(this)
            }
        }
    }

    private fun subscribeViewModel() {
        viewModel.liveData.observe(this) {
            Log.d(EVENT_TAG, "liveData-$it")
        }

        viewModel.singleLiveData.observe(this) {
            Log.d(EVENT_TAG, "singleLiveData-$it")
        }

        viewModel.sharedFlow0.onEach {
            Log.d(EVENT_TAG, "sharedFlow0-$it")
        }.launchIn(lifecycleScope)

        viewModel.sharedFlow1.onEach {
            Log.d(EVENT_TAG, "sharedFlow1-$it")
        }.launchIn(lifecycleScope)

        viewModel.stateFlow.onEach {
            Log.d(EVENT_TAG, "stateFlow-$it")
        }.launchIn(lifecycleScope)

        viewModel.channel.onEach {
            Log.d(EVENT_TAG, "channel-$it")
        }.launchIn(lifecycleScope)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedFlowRepeat0.onEach {
                    Log.d(EVENT_TAG, "sharedFlow0-repeated-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "sharedFlow0-repeated-onCompletion")
                }.launchIn(this)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedFlowRepeat1.onEach {
                    Log.d(EVENT_TAG, "sharedFlow1-repeated-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "sharedFlow1-repeated-onCompletion")
                }.launchIn(this)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowRepeat.onEach {
                    Log.d(EVENT_TAG, "stateFlow-repeated-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "stateFlow-repeated-onCompletion")
                }.launchIn(this)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.channelRepeat.onEach {
                    Log.d(EVENT_TAG, "channel-repeated-$it")
                }.onCompletion {
                    Log.d(EVENT_TAG, "channel-repeated-onCompletion")
                }.launchIn(this)
            }
        }
    }

}