package com.istekno.gohipeandroidapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerExperienceFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerPortfolioFragment

class EngineerProfileViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Portfolio", "Experience")
    private val fragment = arrayOf(EngineerPortfolioFragment(), EngineerExperienceFragment())

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }
}