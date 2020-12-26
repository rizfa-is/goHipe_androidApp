package com.istekno.gohipeandroidapp.fragments.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyEditProjectScreenBinding

class CompanyEditProjectScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    private lateinit var binding: FragmentCompanyEditProjectScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_edit_project_screen, container, false)
        return binding.root
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Edit project"
    }
}