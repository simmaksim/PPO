package com.example.lr1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.lr1.fragments.BaseKeyboardFragment
import com.example.lr1.fragments.ScienceKeyboardFragment

class MainActivityAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(tmp: Int): Fragment {
        when(tmp) {
            0 -> {
                return BaseKeyboardFragment()
            }
            1 -> {
                return ScienceKeyboardFragment()
            }
        }
        return BaseKeyboardFragment()
    }

}