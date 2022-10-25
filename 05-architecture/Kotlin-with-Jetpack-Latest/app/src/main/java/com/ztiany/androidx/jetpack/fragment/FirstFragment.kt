package com.ztiany.androidx.jetpack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.databinding.FragmentsFirstBinding

class FirstFragment : Fragment() {

    private lateinit var vb: FragmentsFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                add(R.id.fl_content, SecondFragment(), SecondFragment::class.toString())
            }
        }
    }

}