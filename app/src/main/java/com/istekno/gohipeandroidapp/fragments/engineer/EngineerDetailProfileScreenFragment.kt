package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.EngineerProfileViewPagerAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerDetailProfileScreenBinding
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse

class EngineerDetailProfileScreenFragment : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
        const val HOME_DATA = "home_data"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentEngineerDetailProfileScreenBinding

    private val listAbility = GoHipeDatabases.ability

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_detail_profile_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HOME_DATA)
        binding.model = data
        Glide.with(view.context).load(imageLink + data?.enAvatar).into(binding.imgEnprofifrgAvatar)


        binding.btnEngprofifrgHire.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(HIRE_AUTH_KEY, 0)
            sendIntent.putExtra(HIRE_DATA, data)
            startActivity(sendIntent)
        }

        setViewPager()
        chipViewInit(view)
        favoriteState()
    }

    private fun setViewPager() {
        val enginerAccountPagerAdapter = EngineerProfileViewPagerAdapter(childFragmentManager)
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
}