package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainScreenActivity
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountSettingBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerDeleteResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*

class EngineerAccountSettingFragment(private val toolbar: MaterialToolbar): Fragment() {

    private lateinit var binding: FragmentEngineerAccountSettingBinding
    private lateinit var engineerModel: EngineerModel
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
        engineerModel = goHipePreferences.getEngineerPreference()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient()!!.create(GoHipeApiService::class.java)

        viewListener(view)
    }

    private fun viewListener(view: View) {
        binding.btnEngsetfrgLogout.setOnClickListener {
            if (goHipePreferences.getEngineerPreference().isLogin) {
                engineerModel.isLogin = false
                goHipePreferences.setEngineerPreference(engineerModel)

                dialog.dialog(view.context, "Are you sure to logout ?") {
                    startActivity(Intent(view.context, MainScreenActivity::class.java))
                    activity?.finish()
                }
            }
        }

        binding.btnEngsetfrgDeleteaccount.setOnClickListener {
            deleteEngineerAccount()
        }
    }

    private fun deleteEngineerAccount() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.deleteEngineer(5)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerDeleteResponse) {
                Log.d("GoHipe : ", result.toString())
            }
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Setting"
    }
}