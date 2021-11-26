package com.example.lr1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.lr1.R
import com.example.lr1.fragments.BaseKeyboardFragment.Companion.getAllViews
import com.example.lr1.MainActivity

class ScienceKeyboardFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.science_keyboard_fragment, container, false)

        view.getAllViews()
            .filterIsInstance<Button>()
            .forEach { it.setOnClickListener(activity as MainActivity?) }

        return view
    }
}