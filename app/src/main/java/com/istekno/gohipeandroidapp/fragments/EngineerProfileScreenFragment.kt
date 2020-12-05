package com.istekno.gohipeandroidapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.EngineerProfilePagerAdapter
import kotlinx.android.synthetic.main.fragment_engineer_profile_screen.*

class EngineerProfileScreenFragment : Fragment() {

    private val listAbility = arrayOf<String>("Android", "108 MP", "Creativity", "Route")
    private val listDrawable = listOf<Int>(R.drawable.ic_android, R.drawable.ic_camera, R.drawable.ic_idea, R.drawable.ic_location_track)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_engineer_profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newActivity = activity as AppCompatActivity
        newActivity.supportActionBar?.elevation = 0F

        val enginerProfilePagerAdapter = EngineerProfilePagerAdapter(context as Context, fragmentManager as FragmentManager)
        vp_engprofiact.adapter = enginerProfilePagerAdapter
        tl_engprofiact.setupWithViewPager(vp_engprofiact)

        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbility[i].toString()
            chip.setTextColor(resources.getColor(R.color.white))
            chip.chipIcon = resources.getDrawable(listDrawable[i])

            cg_enprofifrg_ability.addView(chip)
        }

        img_enprofifrg_favorite.setOnClickListener {
            if (!it.isSelected) {
                img_enprofifrg_favorite.setImageResource(R.drawable.ic_favorite_checked)
                img_enprofifrg_favorite.isSelected = true
            } else {
                img_enprofifrg_favorite.setImageResource(R.drawable.ic_favorite_unchecked)
                img_enprofifrg_favorite.isSelected = false
            }
        }
        topAppBar_engprofifrg.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }
}