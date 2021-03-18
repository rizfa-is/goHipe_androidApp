package com.istekno.gohipeandroidapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.ui.engineer.experience.EngineerExperienceFragment
import com.istekno.gohipeandroidapp.ui.engineer.portfolio.EngineerPortfolioFragment

class EngineerProfileViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Portfolio", "Experience")
    private val fragment = arrayOf(EngineerPortfolioFragment(), EngineerExperienceFragment())

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment = fragment[position]

    override fun getPageTitle(position: Int): CharSequence = tabTitles[position]
}