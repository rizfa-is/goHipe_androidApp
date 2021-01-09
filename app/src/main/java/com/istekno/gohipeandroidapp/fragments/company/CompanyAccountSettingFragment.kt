package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainScreenActivity
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAccountSettingBinding
import com.istekno.gohipeandroidapp.utility.Dialog

class CompanyAccountSettingFragment(private val toolbar: MaterialToolbar): Fragment() {

    private lateinit var binding: FragmentCompanyAccountSettingBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var companyModel: CompanyModel
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_account_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog()
        goHipePreferences = GoHipePreferences(view.context)
        companyModel = goHipePreferences.getCompanyPreference()

        binding.btnCompsetfrgLogout.setOnClickListener {
            if (goHipePreferences.getCompanyPreference().isLogin) {
                dialog.dialog(view.context, "Are you sure to logout ?") {
                    companyModel.isLogin = false
                    companyModel.level = ""
                    goHipePreferences.setCompanyPreference(companyModel)

                    startActivity(Intent(view.context, MainScreenActivity::class.java))
                    activity?.finish()
                }
            }
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Setting"
    }
}