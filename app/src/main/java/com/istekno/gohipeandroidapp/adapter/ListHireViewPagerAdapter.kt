package com.istekno.gohipeandroidapp.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.company.ApprovedHireFragment
import com.istekno.gohipeandroidapp.fragments.company.OnWaitingHireFragment
import com.istekno.gohipeandroidapp.fragments.company.RejectedHireFragment

class ListHireViewPagerAdapter(private val mContext: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.on_waiting, R.string.approved, R.string.rejected)

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OnWaitingHireFragment()
            1 -> fragment = ApprovedHireFragment()
            2 -> fragment = RejectedHireFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }
}