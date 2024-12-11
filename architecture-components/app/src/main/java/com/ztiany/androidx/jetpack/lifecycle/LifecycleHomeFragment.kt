package com.ztiany.androidx.jetpack.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ztiany.androidx.kotlin.databinding.LifecycleFragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

class LifecycleHomeFragment : Fragment() {

    private var binding: LifecycleFragmentHomeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LifecycleFragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

}