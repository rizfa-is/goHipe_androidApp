package com.istekno.gohipeandroidapp.ui.company.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.activity.MainScreenActivity
import com.istekno.gohipeandroidapp.models.CompanyPreferenceModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAccountSettingBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.ui.Dialog
import kotlinx.coroutines.*

class CompanyAccountSettingFragment(private val toolbar: MaterialToolbar): Fragment() {

    private lateinit var binding: FragmentCompanyAccountSettingBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var companyPreferenceModel: CompanyPreferenceModel
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

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
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        companyPreferenceModel = goHipePreferences.getCompanyPreference()

        viewListener(view)
    }

    private fun viewListener(view: View) {
        binding.btnCompsetfrgLogout.setOnClickListener {
            if (goHipePreferences.getCompanyPreference().isLogin) {
                dialog.dialog(view.context, "Are you sure to logout ?") {
                    companyPreferenceModel.isLogin = false
                    companyPreferenceModel.level = ""
                    goHipePreferences.setCompanyPreference(companyPreferenceModel)

                    startActivity(Intent(view.context, MainScreenActivity::class.java))
                    activity?.finishAffinity()
                }
            }
        }

        binding.btnCompsetfrgDeleteaccount.setOnClickListener {
            dialog.dialog(view.context, "Are you sure to delete account ?") {
                deleteEngineerAccount()

                startActivity(Intent(view.context, MainScreenActivity::class.java))
                activity?.finishAffinity()
            }
        }
    }

    private fun deleteEngineerAccount() {
        val id = goHipePreferences.getCompanyPreference().acID

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteCompany(id!!)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Setting"
    }
}