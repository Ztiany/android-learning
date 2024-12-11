package com.ztiany.androidx.jetpack.navigation.table.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.databinding.NavFragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var vb: NavFragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called with: savedInstanceState = $savedInstanceState")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState")
        return NavFragmentHomeBinding.inflate(inflater, container, false).apply {
            vb = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState")

        vb.btnOpenDetail.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_detail)
        }

        vb.btnOpenFeaturea.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_container).navigate(R.id.action_main_to_featurea)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
    }

}