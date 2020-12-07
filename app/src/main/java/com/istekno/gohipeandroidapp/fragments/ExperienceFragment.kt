package com.istekno.gohipeandroidapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListExperienceRecycleViewAdapter
import com.istekno.gohipeandroidapp.data.Experience
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import kotlinx.android.synthetic.main.fragment_experience.*

class ExperienceFragment : Fragment() {

    private val listExperience = ArrayList<Experience>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experience, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listExperience.addAll(GoHipeDatabases.listExperiences)
        showRecycleList()
    }

    private fun showRecycleList() {
        rv_experifrg.apply {
            layoutManager = LinearLayoutManager(view?.context)
            adapter = ListExperienceRecycleViewAdapter(listExperience)
        }
    }
}