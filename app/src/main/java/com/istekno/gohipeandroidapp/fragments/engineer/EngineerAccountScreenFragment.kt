package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.EngineerProfilePagerAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountScreenBinding

class EngineerAccountScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
    }

    private lateinit var binding: FragmentEngineerAccountScreenBinding

    private val listAbility = GoHipeDatabases.ability

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.mn_maincontent_toolbar_setting) {
                val sendIntent = Intent(context, SettingScreenActivity::class.java)
                sendIntent.putExtra(SETTING_AUTH_KEY, 0)
                startActivity(sendIntent)
            }
            false
        }

        setViewPager(view)
        chipViewInit(view)
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = true
        toolbar.title = "My Account"
    }

    private fun setViewPager(view: View) {
        val enginerAccountPagerAdapter = EngineerProfilePagerAdapter(view.context, childFragmentManager)
        binding.vpEngprofiact.adapter = enginerAccountPagerAdapter
        binding.tlEngprofiact.setupWithViewPager(binding.vpEngprofiact)
    }

    private fun chipViewInit(view: View) {
        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbility[i].toString()
            chip.setTextColor(resources.getColor(R.color.white))

            binding.cgEnaccfrgAbility.addView(chip)
        }
    }
}