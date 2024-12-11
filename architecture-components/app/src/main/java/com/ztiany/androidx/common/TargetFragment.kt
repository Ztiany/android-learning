package com.ztiany.androidx.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ztiany.androidx.kotlin.databinding.ViewmodelFragmentTargetBinding

class TargetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ViewmodelFragmentTargetBinding.inflate(inflater, container, false).root
    }

}