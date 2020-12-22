package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerSearchScreenBinding

class EngineerSearchScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentEngineerSearchScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_search_screen, container, false)
        return binding.root
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.visibility = View.GONE
    }
}