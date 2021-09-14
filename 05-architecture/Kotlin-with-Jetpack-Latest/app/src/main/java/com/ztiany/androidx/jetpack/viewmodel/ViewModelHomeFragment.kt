package com.ztiany.androidx.jetpack.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ztiany.androidx.kotlin.databinding.ViewmodelFragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewModelHomeFragment : Fragment() {

    private val viewModel by viewModels<DemoViewModel>()

    private var binding: ViewmodelFragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ViewmodelFragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            viewmodelBtnLoad.setOnClickListener {

            }
            viewmodelBtnSave.setOnClickListener {

            }
            viewmodelBtnRecreate.setOnClickListener {
                activity?.recreate()
            }
        }
    }

    private fun subscribeViewModel() {
        viewModel
    }

}