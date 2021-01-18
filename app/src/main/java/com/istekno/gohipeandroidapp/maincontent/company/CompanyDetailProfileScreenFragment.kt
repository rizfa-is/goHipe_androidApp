package com.istekno.gohipeandroidapp.maincontent.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyDetailProfileScreenBinding

class CompanyDetailProfileScreenFragment(private val fullname : String? = null, private val email : String? = null, private val position : String? = null, private val password : String? = null) : Fragment() {

    private lateinit var binding: FragmentCompanyDetailProfileScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_detail_profile_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteState()
        changeText(fullname, email, position, password)
    }

    private fun favoriteState() {
        binding.imgComprofifrgFavorite.setOnClickListener {
            if (!it.isSelected) {
                binding.imgComprofifrgFavorite.setImageResource(R.drawable.ic_favorite_checked)
                binding.imgComprofifrgFavorite.isSelected = true
            } else {
                binding.imgComprofifrgFavorite.setImageResource(R.drawable.ic_favorite_unchecked)
                binding.imgComprofifrgFavorite.isSelected = false
            }
        }
    }

    private fun changeText(fullname: String?, emailNew: String?, positon: String?, passwordNew: String?) {
        binding.tvComprofifrgEmail.text = emailNew
        binding.tvComprofifrgName.text = fullname
        if (positon != null) {
            binding.tvComprofifrgJob.text = positon
        } else {
            binding.tvComprofifrgJob.text = "Android Developer"
        }
    }
}