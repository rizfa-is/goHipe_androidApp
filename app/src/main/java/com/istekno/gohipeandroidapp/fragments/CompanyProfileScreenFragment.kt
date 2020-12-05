package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istekno.gohipeandroidapp.R
import kotlinx.android.synthetic.main.fragment_company_profile_screen.*

class CompanyProfileScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_comprofifrg_favorite.setOnClickListener {
            if (!it.isSelected) {
                img_comprofifrg_favorite.setImageResource(R.drawable.ic_favorite_checked)
                img_comprofifrg_favorite.isSelected = true
            } else {
                img_comprofifrg_favorite.setImageResource(R.drawable.ic_favorite_unchecked)
                img_comprofifrg_favorite.isSelected = false
            }
        }
    }
}