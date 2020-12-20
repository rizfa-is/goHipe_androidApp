package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListExperienceRecycleViewAdapter
import com.istekno.gohipeandroidapp.data.Experience
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentExperienceBinding

class ExperienceFragment : Fragment() {

    private lateinit var binding: FragmentExperienceBinding

    private val listExperience = ArrayList<Experience>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listExperience.addAll(GoHipeDatabases.listExperiences)
        showRecycleList()
    }

    private fun showRecycleList() {
        binding.rvExperifrg.apply {
            layoutManager = LinearLayoutManager(view?.context)
            adapter = ListExperienceRecycleViewAdapter(listExperience)
        }
    }
}