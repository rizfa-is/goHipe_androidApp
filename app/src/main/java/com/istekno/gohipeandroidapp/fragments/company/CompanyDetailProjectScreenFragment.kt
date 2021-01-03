package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyDetailProjectScreenBinding
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse

class CompanyDetailProjectScreenFragment : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val PROJECT_DATA = "project_data"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentCompanyDetailProjectScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_detail_project_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.getParcelableExtra<ProjectModelResponse>(PROJECT_DATA)

        binding.model = data
        binding.tvCompdetailprojectfrgDeadline.text = binding.model?.pjDeadline!!.split('T')[0]
        Glide.with(view.context).load(imageLink + data?.pjImage).into(binding.imgCompdetailprojectfrgAvatar)

        binding.btnComdetailprojectfrgEditproject.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(PROJECT_AUTH_KEY, 12)
            startActivity(sendIntent)
        }
    }
}