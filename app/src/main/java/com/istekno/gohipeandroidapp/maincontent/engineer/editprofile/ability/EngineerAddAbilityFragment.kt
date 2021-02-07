package com.istekno.gohipeandroidapp.maincontent.engineer.editprofile.ability

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAddAbilityBinding

class EngineerAddAbilityFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentEngineerAddAbilityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_add_ability, container, false)

        setToolbar(toolbar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    private fun setToolbar(toolbar: MaterialToolbar) {
        val authKeyEdit = activity?.intent?.getIntExtra(SettingScreenActivity.EDIT_PROFILE_AUTH_KEY, -1)
        val data = activity?.intent?.getParcelableExtra<EngineerEditProfileAbilityFragment.Companion.EditAbilityModel>(
            EngineerEditProfileAbilityFragment.EDIT_AB_KEY
        )

        if (authKeyEdit == 10) {
            toolbar.title = "Add Ability"
        } else {
            toolbar.title = "Edit Ability"

            binding.etAddabName.setText(data?.name)
            binding.btnAddab.text = "Update"
        }
    }
}