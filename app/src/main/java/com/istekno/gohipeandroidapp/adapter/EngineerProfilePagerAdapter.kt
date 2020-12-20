package com.istekno.gohipeandroidapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.fragments.ExperienceFragment
import com.istekno.gohipeandroidapp.fragments.PortfolioFragment

class EngineerProfilePagerAdapter(private val mContext: Context, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(R.string.portfolio_tab_title, R.string.experience_tab_title)

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 -> fragment  = PortfolioFragment()
            1 -> fragment = ExperienceFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }
}