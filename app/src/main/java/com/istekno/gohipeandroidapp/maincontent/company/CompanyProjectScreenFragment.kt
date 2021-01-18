package com.istekno.gohipeandroidapp.maincontent.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListProjectViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyProjectScreenBinding

class CompanyProjectScreenFragment(private val toolbar: MaterialToolbar, private val co: CoordinatorLayout, private val bottomNavigationView: BottomNavigationView,
                                   private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    private lateinit var binding: FragmentCompanyProjectScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar, co, bottomNavigationView)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_project_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPagerAdapter()
    }

    private fun setToolbar(toolbar: MaterialToolbar, co: CoordinatorLayout, bottomNavigationView: BottomNavigationView) {
        if (state) {
            co.visibility = View.VISIBLE
            rl.visibility = View.GONE
        } else {
            co.visibility = View.GONE
            rl.visibility = View.VISIBLE
        }

        bottomNavigationView.menu.findItem(R.id.mn_item_maincontent_project).isChecked = true
        toolbar.title = "Project"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }

    private fun setPagerAdapter() {
        val pagerAdapter = ListProjectViewPagerAdapter(childFragmentManager)
        binding.vpListProject.adapter = pagerAdapter
        binding.tlListProject.setupWithViewPager(binding.vpListProject)
    }
}