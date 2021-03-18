package com.istekno.gohipeandroidapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.ui.engineer.profile.edit.ability.EngineerEditProfileAbilityFragment
import com.istekno.gohipeandroidapp.ui.engineer.profile.edit.experience.EngineerEditProfileExperienceFragment
import com.istekno.gohipeandroidapp.ui.engineer.profile.edit.portfolio.EngineerEditProfilePortfolioFragment
import com.istekno.gohipeandroidapp.ui.engineer.profile.edit.profile.EngineerEditProfileAccountFragment

class ListEditProfileViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Account", "Ability", "Portfolio", "Experience")
    private val fragment = arrayOf(EngineerEditProfileAccountFragment(), EngineerEditProfileAbilityFragment(), EngineerEditProfilePortfolioFragment(), EngineerEditProfileExperienceFragment())

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment = fragment[position]

    override fun getPageTitle(position: Int): CharSequence = tabTitles[position]

}