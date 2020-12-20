package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istekno.gohipeandroidapp.R
import kotlinx.android.synthetic.main.fragment_company_profile_screen.*

class CompanyDetailProfileScreenFragment(
        private val fullname : String? = null,
        private val email : String? = null,
        private val position : String? = null,
        private val password : String? = null
    ) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeText(fullname, email, position, password)
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

    private fun changeText(fullname: String?, emailNew: String?, positon: String?, passwordNew: String?) {
        tv_comprofifrg_email.text = emailNew
        tv_comprofifrg_name.text = fullname
        if (positon != null) {
            tv_comprofifrg_job.text = positon
        } else {
            tv_comprofifrg_job.text = "Android Developer"
        }
    }
}