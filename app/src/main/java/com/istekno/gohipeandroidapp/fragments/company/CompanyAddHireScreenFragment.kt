package com.istekno.gohipeandroidapp.fragments.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAddHireScreenBinding

class CompanyAddHireScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    private val listDropdownJobtype = listOf("Buku Tani", "Aplikasi Anak Rantau", "Mau Makan Murah", "Travelion")

    private lateinit var binding: FragmentCompanyAddHireScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_add_hire_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDropdownMenuAdapter(view)
    }

    private fun setDropdownMenuAdapter(view: View) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, listDropdownJobtype)
        (binding.itComhirenowfrgProject.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = ""
    }
}