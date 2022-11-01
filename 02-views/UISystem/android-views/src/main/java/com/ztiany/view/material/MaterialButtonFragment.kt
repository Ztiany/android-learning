package com.ztiany.view.material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/** 参考 https://blog.csdn.net/yechaoa/article/details/117197876?spm=1001.2014.3001.5501 */
class MaterialButtonFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply {
            text = "TODO"
            textSize = 40F
        }
    }

}