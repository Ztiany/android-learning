package com.ztiany.androidx.kotlin.coroutines.official.google.flow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ztiany.androidx.kotlin.databinding.KotlinFlowFragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OfficialFlowHomeFragment : Fragment() {

    private val viewModel by viewModels<OfficialFlowHomeViewModel>()

    private var layout: KotlinFlowFragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = KotlinFlowFragmentHomeBinding.inflate(inflater, container, false)
        setUpListener()
        return layout?.root
    }

    private fun setUpListener() {
        layout?.run {
            flowBtnLoadUser.setOnClickListener {
                viewModel.loadUser().observe(this@OfficialFlowHomeFragment.viewLifecycleOwner) {
                    Log.d(FLOW_TAG, "userName: $it")
                }
            }
        }
    }

    private fun subscribeViewModel() {
        viewModel.latestNews.observe(this) {
            Log.d(FLOW_TAG, "latestNews: $it")
        }

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d(FLOW_TAG, "repeatOnLifecycle")
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.uiState.collect {
                    when (it) {
                        is OfficialFlowHomeViewModel.LatestNewsUiState.Error -> {
                            Log.d(FLOW_TAG, "$it")
                        }
                        is OfficialFlowHomeViewModel.LatestNewsUiState.Success -> {
                            Log.d(FLOW_TAG, "$it")
                        }
                    }
                }
            }
        }
    }

}