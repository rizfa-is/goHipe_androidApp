package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyDetailHireScreenBinding

class CompanyDetailHireScreenFragment(private val hireStatus: Int?) : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"

        private const val APPROVED = "APPROVED"
        private const val REJECTED = "REJECTED"
    }

    private lateinit var binding: FragmentCompanyDetailHireScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_detail_hire_screen, container, false)

        detailStatus(hireStatus)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnComdetailprojectfrgEditproject.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(HIRE_AUTH_KEY, 20)
            startActivity(sendIntent)
        }
    }

    private fun detailStatus(hireStatus: Int?) {
        when (hireStatus) {
            1 -> {
                binding.btnComdetailprojectfrgEditproject.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = APPROVED
            }
            2 -> {
                binding.btnComdetailprojectfrgEditproject.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = REJECTED
                binding.tvComdetailprojectfrgHireStatus.setTextColor(Color.RED)
            }
        }
    }
}