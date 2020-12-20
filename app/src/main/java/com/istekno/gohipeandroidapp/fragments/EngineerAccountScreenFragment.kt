package com.istekno.gohipeandroidapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.EngineerProfilePagerAdapter
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountScreenBinding
import kotlinx.android.synthetic.main.fragment_engineer_profile_screen.*

class EngineerAccountScreenFragment(private val toolbar: MaterialToolbar, private val bottomNav: BottomNavigationView) : Fragment() {

    private lateinit var engineerAccountScreenBinding: FragmentEngineerAccountScreenBinding

    private val listAbility = GoHipeDatabases.ability

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = true

        engineerAccountScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_screen, container, false)
        // Inflate the layout for this fragment
        return engineerAccountScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enginerAccountPagerAdapter = EngineerProfilePagerAdapter(view.context, childFragmentManager)
        engineerAccountScreenBinding.vpEngprofiact.adapter = enginerAccountPagerAdapter
        engineerAccountScreenBinding.tlEngprofiact.setupWithViewPager(engineerAccountScreenBinding.vpEngprofiact)

        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbility[i].toString()
            chip.setTextColor(resources.getColor(R.color.white))

            engineerAccountScreenBinding.cgEnaccfrgAbility.addView(chip)
        }

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.mn_maincontent_toolbar_setting) {
                startActivity(Intent(context, SettingScreenActivity::class.java))
            }
            false
        }
    }
}