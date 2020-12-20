package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.EngineerProfilePagerAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerDetailProfileScreenBinding

class EngineerDetailProfileScreenFragment(private val fullname : String? = null, private val email : String? = null, private val password : String? = null) : Fragment() {

    private lateinit var binding: FragmentEngineerDetailProfileScreenBinding

    private val listAbility = GoHipeDatabases.ability

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_detail_profile_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager(view)
        chipViewInit(view)
        changeText(email, fullname)
        favoriteState()
    }

    private fun setViewPager(view: View) {
        val enginerProfilePagerAdapter = EngineerProfilePagerAdapter(view.context, childFragmentManager)
        binding.vpEngprofiact.adapter = enginerProfilePagerAdapter
        binding.tlEngprofiact.setupWithViewPager(binding.vpEngprofiact)
    }

    private fun chipViewInit(view: View) {
        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbility[i].toString()
            chip.setTextColor(resources.getColor(R.color.white))

            binding.cgEnprofifrgAbility.addView(chip)
        }
    }

    private fun favoriteState() {
        binding.imgEnprofifrgFavorite.setOnClickListener {
            if (!it.isSelected) {
                binding.imgEnprofifrgFavorite.setImageResource(R.drawable.ic_favorite_checked)
                binding.imgEnprofifrgFavorite.isSelected = true
            } else {
                binding.imgEnprofifrgFavorite.setImageResource(R.drawable.ic_favorite_unchecked)
                binding.imgEnprofifrgFavorite.isSelected = false
            }
        }
    }

    private fun changeText(emailNew: String?, fullname: String?) {
        binding.tvEnprofifrgEmail.text = emailNew
        binding.tvEnprofifrgName.text = fullname
    }
}