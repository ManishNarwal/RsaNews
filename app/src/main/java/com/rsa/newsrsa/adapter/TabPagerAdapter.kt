package com.rsa.newsrsa.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rsa.newsrsa.ui.news.InterNationalNewsFragment
import com.rsa.newsrsa.ui.news.LocalNewsFragment
import com.rsa.newsrsa.ui.news.NationalNewsFragment

class TabPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LocalNewsFragment()
            1 -> NationalNewsFragment()
            2 -> InterNationalNewsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}