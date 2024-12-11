package com.ztiany.androidx.jetpack.livedata

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.ztiany.androidx.kotlin.databinding.LivedataFragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LiveDataHomeFragment"

@AndroidEntryPoint
class LiveDataHomeFragment : Fragment() {

    private val viewModel by viewModels<LiveDataViewModel>()

    private var binding: LivedataFragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = LivedataFragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            livedataBtnStart.setOnClickListener {
                viewModel.setUserId(1000)
            }

            livedataBtnLoad.setOnClickListener {
                processLoadedUser(viewModel.loadUser())
            }
        }
    }


    private fun subscribeViewModel() {
        viewModel.userDetail.observe(this) {
            Log.d(TAG, "observe.userDetail: $it")
        }
    }

    private fun processLoadedUser(loadUser: LiveData<User>) {
        loadUser.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe.loadUser: $it")
        }
    }

}