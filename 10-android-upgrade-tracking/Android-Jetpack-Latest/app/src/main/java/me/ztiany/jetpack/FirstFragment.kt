package me.ztiany.jetpack

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_first.setOnClickListener {
            Log.d(TAG, "${button_first.background}")
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        textview_zero.setOnClickListener {
            Log.d(TAG, "${textview_first.background}")
            Toast.makeText(requireContext(), "textview_zero", Toast.LENGTH_LONG).show()
        }

        textview_first.setOnClickListener {
            Log.d(TAG, "${textview_first.background}")
            Toast.makeText(requireContext(), "textview_first", Toast.LENGTH_LONG).show()
        }

        button_second.setOnClickListener {
            Toast.makeText(requireContext(), "button_second", Toast.LENGTH_LONG).show()
        }

        button_third.setOnClickListener {
            Toast.makeText(requireContext(), "button_third", Toast.LENGTH_LONG).show()
        }

    }
}