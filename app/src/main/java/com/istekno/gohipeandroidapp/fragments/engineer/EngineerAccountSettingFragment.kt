package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainScreenActivity
import com.istekno.gohipeandroidapp.models.EngineerPreferenceModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountSettingBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*

class EngineerAccountSettingFragment(private val toolbar: MaterialToolbar): Fragment() {

    private lateinit var binding: FragmentEngineerAccountSettingBinding
    private lateinit var engineerPreferenceModel: EngineerPreferenceModel
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()
        goHipePreferences = GoHipePreferences(view.context)
        engineerPreferenceModel = goHipePreferences.getEngineerPreference()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)

        viewListener(view)
    }

    private fun viewListener(view: View) {
        binding.btnEngsetfrgLogout.setOnClickListener {
            if (goHipePreferences.getEngineerPreference().isLogin) {
                dialog.dialog(view.context, "Are you sure to logout ?") {
                    engineerPreferenceModel.isLogin = false
                    engineerPreferenceModel.level = ""
                    goHipePreferences.setEngineerPreference(engineerPreferenceModel)

                    startActivity(Intent(view.context, MainScreenActivity::class.java))
                    activity?.finishAffinity()
                }
            }
        }

        binding.btnEngsetfrgDeleteaccount.setOnClickListener {
            dialog.dialog(view.context, "Are you sure to delete account ?") {
                deleteEngineerAccount()

                startActivity(Intent(view.context, MainScreenActivity::class.java))
                activity?.finishAffinity()
            }
        }
    }

    private fun deleteEngineerAccount() {
        val id = goHipePreferences.getEngineerPreference().acID

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteEngineer(id!!)
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