package com.example.nasaapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nasaapp.ui.fragments.PictureOfTheDayFragment
import java.lang.IllegalArgumentException

class SimpleTabSPAdapter(private val fragment: Fragment, val lastDaysCounter : Int = 1) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        if ( lastDaysCounter < 1) throw IllegalArgumentException("Invalid lastDaysCounter param")
        return lastDaysCounter
    }

    override fun createFragment(position: Int) = PictureOfTheDayFragment.newInstance(position)
}