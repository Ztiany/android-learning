package com.ztiany.androidx.jetpack.navigation.table.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ztiany.androidx.kotlin.R
import com.ztiany.androidx.kotlin.databinding.NavFragmentMessageBinding
import com.ztiany.androidx.kotlin.databinding.NavFragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var vb: NavFragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return NavFragmentProfileBinding.inflate(inflater, container, false).apply {
            vb = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vb.btnOpenDetail.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_detail)
        }
    }

}