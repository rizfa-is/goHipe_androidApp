package com.istekno.gohipeandroidapp.fragments.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListHireViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyHiringScreenBinding

class CompanyHiringScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentCompanyHiringScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

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

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.visibility = View.VISIBLE
        toolbar.title = "Hiring"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}