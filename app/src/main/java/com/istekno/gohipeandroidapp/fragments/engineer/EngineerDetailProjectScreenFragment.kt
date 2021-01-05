package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerDetailProjectScreenBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyDetailProjectScreenFragment
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse

class EngineerDetailProjectScreenFragment : Fragment() {

    companion object {
        const val PROJECT_DATA = "project_data"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentEngineerDetailProjectScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_detail_project_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.getParcelableExtra<ProjectModelResponse>(PROJECT_DATA)

        binding.model = data
        binding.tvEngdetailprojectfrgDeadline.text = binding.model?.pjDeadline!!.split('T')[0]
        Glide.with(view.context).load(imageLink + data?.pjImage).into(binding.imgEngdetailprojectfrgAvatar)
    }
}