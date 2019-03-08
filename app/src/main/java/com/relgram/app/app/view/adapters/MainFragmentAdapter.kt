package com.relgram.app.app.view.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.relgram.app.app.view.fragments.*

class MainFragmentAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return UserFragment()
            1 -> return SearchFragment()
            2 -> return HomeFragment()
            3 -> return FavoriteFragment()
            4 -> return ChatFragment()
        }
        return HomeFragment()
    }

    override fun getCount(): Int {
        return 5
    }
}