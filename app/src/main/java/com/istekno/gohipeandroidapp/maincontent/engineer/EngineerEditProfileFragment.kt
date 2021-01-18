package com.istekno.gohipeandroidapp.maincontent.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListEditProfileViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileBinding

class EngineerEditProfileFragment(private val toolbar: MaterialToolbar): Fragment() {

    private lateinit var binding: FragmentEngineerEditProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPagerAdapter()
    }

    private fun setPagerAdapter() {
        val pagerAdapter = ListEditProfileViewPagerAdapter(childFragmentManager)
        binding.vpListEditprofile.offscreenPageLimit = 3
        binding.vpListEditprofile.adapter = pagerAdapter
        binding.tlListEditprofile.setupWithViewPager(binding.vpListEditprofile)
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Edit Profile"
    }
}