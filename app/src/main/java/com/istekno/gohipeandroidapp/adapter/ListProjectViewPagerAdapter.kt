package com.istekno.gohipeandroidapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.maincontent.company.CompanyCompletedProjectFragment
import com.istekno.gohipeandroidapp.maincontent.company.CompanyOnHiringProjectFragment
import com.istekno.gohipeandroidapp.maincontent.company.CompanyPreHiringProjectFragment

class ListProjectViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Pre Hiring", "On Hiring", "Completed")
    private val fragment = arrayOf(CompanyPreHiringProjectFragment(), CompanyOnHiringProjectFragment(), CompanyCompletedProjectFragment())

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment = fragment[position]

    override fun getPageTitle(position: Int): CharSequence = tabTitles[position]
}