package com.ztiany.androidx.jetpack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.ztiany.androidx.kotlin.databinding.FragmentsFirstBinding
import com.ztiany.androidx.kotlin.databinding.FragmentsSecondBinding

const val SECOND_FRAGMENT_KEY = "second_fragment_key"
const val SECOND_FRAGMENT_VALUE_KEY = "second_fragment_value_key"

class SecondFragment : Fragment() {

    private lateinit var vb: FragmentsSecondBinding

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            isEnabled = false
            Toast.makeText(requireContext(), "2-handleOnBackPressed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         requireActivity().onBackPressedDispatcher.addCallback(this) {
             Toast.makeText(requireContext(), "1-handleOnBackPressed", Toast.LENGTH_LONG).show()
         }
         */
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentsSecondBinding.inflate(inflater, container, false).apply {
            vb = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.btnSetResult.setOnClickListener {
            setFragmentResult(
                SECOND_FRAGMENT_KEY, bundleOf(
                    SECOND_FRAGMENT_VALUE_KEY to "Value from SecondFragment"
                )
            )
        }
    }

}