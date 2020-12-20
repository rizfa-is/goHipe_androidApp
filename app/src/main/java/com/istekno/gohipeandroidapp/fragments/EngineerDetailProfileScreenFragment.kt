package com.istekno.gohipeandroidapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.EngineerProfilePagerAdapter
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import kotlinx.android.synthetic.main.fragment_engineer_profile_screen.*

class EngineerDetailProfileScreenFragment(
        private val fullname : String? = null,
        private val email : String? = null,
        private val password : String? = null
    ) : Fragment() {

    private val listAbility = GoHipeDatabases.ability
    private val listDrawable = GoHipeDatabases.iconAbility

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_engineer_profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enginerProfilePagerAdapter = EngineerProfilePagerAdapter(view.context, childFragmentManager)
        vp_engprofiact.adapter = enginerProfilePagerAdapter
        tl_engprofiact.setupWithViewPager(vp_engprofiact)

        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbility[i].toString()
            chip.setTextColor(resources.getColor(R.color.white))

            cg_enprofifrg_ability.addView(chip)
        }

        changeText(email, password, fullname)

        img_enprofifrg_favorite.setOnClickListener {
            if (!it.isSelected) {
                img_enprofifrg_favorite.setImageResource(R.drawable.ic_favorite_checked)
                img_enprofifrg_favorite.isSelected = true
            } else {
                img_enprofifrg_favorite.setImageResource(R.drawable.ic_favorite_unchecked)
                img_enprofifrg_favorite.isSelected = false
            }
        }
    }

    private fun changeText(emailNew: String?, passwordNew: String?, fullname: String?) {
        tv_enprofifrg_email.text = emailNew
        tv_enprofifrg_name.text = fullname
    }
}