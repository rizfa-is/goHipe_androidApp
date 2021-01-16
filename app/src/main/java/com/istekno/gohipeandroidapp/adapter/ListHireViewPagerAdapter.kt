package com.istekno.gohipeandroidapp.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.company.CompanyOnProgressHireFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyOnWaitingHireFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyFinishedHireFragment

class ListHireViewPagerAdapter(private val mContext: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.on_waiting, R.string.on_progress, R.string.finished)
    private var fragment = arrayOf(CompanyOnWaitingHireFragment(), CompanyOnProgressHireFragment(), CompanyFinishedHireFragment())

    fun setFargmentList(fragmentList: Array<Fragment>) {
        fragment = fragmentList
    }

    private fun getFragmentList() : Array<Fragment> = fragment

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment = getFragmentList()[position]

    override fun getPageTitle(position: Int): CharSequence = mContext.resources.getString(tabTitles[position])
}