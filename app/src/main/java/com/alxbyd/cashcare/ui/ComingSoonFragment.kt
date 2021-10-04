package com.alxbyd.cashcare.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.alxbyd.cashcare.R

class ComingSoonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coming_soon, container, false)
        view.findViewById<TextView>(R.id.fragment_name).text = requireArguments().getString("name")
        return view
    }

    companion object {
        private const val FRAGMENT_NAME = "name"
        fun newInstance(name: String) = ComingSoonFragment().apply {
            arguments = bundleOf(
                FRAGMENT_NAME to name
            )
        }
    }

}