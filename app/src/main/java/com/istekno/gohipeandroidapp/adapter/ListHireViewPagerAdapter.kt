package com.istekno.gohipeandroidapp.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.company.CompanyApprovedHireFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyOnWaitingHireFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyRejectedHireFragment

class ListHireViewPagerAdapter(private val mContext: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.on_waiting, R.string.approved, R.string.rejected)
    private var fragment = arrayOf(CompanyOnWaitingHireFragment(), CompanyApprovedHireFragment(), CompanyRejectedHireFragment())

    fun setFargmentList(fragmentList: Array<Fragment>) {
        fragment = fragmentList
    }

    fun getFragmentList() : Array<Fragment> {
        return fragment
    }

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment {
        return getFragmentList()[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }
}