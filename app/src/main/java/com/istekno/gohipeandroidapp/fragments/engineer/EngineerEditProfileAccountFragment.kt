package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileAccountBinding

class EngineerEditProfileAccountFragment : Fragment() {

    private val listDropdownJobtype = listOf("Freelance", "Fulltime")

    private lateinit var binding: FragmentEngineerEditProfileAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDropdownMenuAdapter(view)
    }

    private fun setDropdownMenuAdapter(view: View) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, listDropdownJobtype)
        (binding.itEngeditaccountfrgJobtype.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}