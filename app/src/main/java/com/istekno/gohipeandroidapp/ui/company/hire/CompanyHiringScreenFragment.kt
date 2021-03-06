package com.istekno.gohipeandroidapp.ui.company.hire

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
import com.istekno.gohipeandroidapp.ui.adapter.ListHireViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyHiringScreenBinding

class CompanyHiringScreenFragment(private val toolbar: MaterialToolbar, private val co: CoordinatorLayout, private val bottomNavigationView: BottomNavigationView,
                                  private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    private lateinit var binding: FragmentCompanyHiringScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_hiring_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPagerAdapter(view)
    }

    private fun setPagerAdapter(view: View) {
        val pagerAdapter = ListHireViewPagerAdapter(view.context, childFragmentManager)
        binding.vpListHire.adapter = pagerAdapter
        binding.tlListHire.setupWithViewPager(binding.vpListHire)
    }

    private fun setToolbar() {
        if (state) {
            co.visibility = View.VISIBLE
            rl.visibility = View.GONE
        } else {
            co.visibility = View.GONE
            rl.visibility = View.VISIBLE
        }

        bottomNavigationView.menu.findItem(R.id.mn_item_maincontent_hiring).isChecked = true
        toolbar.title = "Hiring"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}