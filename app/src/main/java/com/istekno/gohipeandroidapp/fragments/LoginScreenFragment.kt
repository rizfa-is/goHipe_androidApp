package com.istekno.gohipeandroidapp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.erdin.arkaandroidtwo.remote.HeaderInterceptor
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentLoginScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerRegisterScreenFragment
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.retrofit.CompanyGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.LoginResponse
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel
    private lateinit var companyModel: CompanyModel
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var headerInterceptor: HeaderInterceptor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companyModel = CompanyModel()
        engineerModel = EngineerModel()
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient()!!.create(GoHipeApiService::class.java)
        headerInterceptor = HeaderInterceptor()

        clickListener(view)
    }

    private fun clickListener(view: View) {
        binding.tvLoginfrgForgotPassword.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, ForgotPasswordScreenFragment())?.commit()
        }

        binding.btnLoginfrgLogin.setOnClickListener {
            login(view)
        }

        binding.tvLoginfrgRegisterHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, SelectRoleFragment())?.commit()
        }
    }

    private fun login(view: View) {
        val email = binding.etLoginfrgEmail.text.toString()
        val password = binding.etLoginfrgPassword.text.toString()

        goHipePreferences = GoHipePreferences(view.context)
        engineerModel = goHipePreferences.getEngineerPreference()
        companyModel = goHipePreferences.getCompanyPreference()

        if (email.isEmpty()) {
            binding.etLoginfrgEmail.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (password.isEmpty()) {
            binding.etLoginfrgPassword.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        checkLogin(email,password)
    }

    private fun checkLogin(email: String, password: String) {
        coroutineScope.launch {
            var resultA: LoginResponse
            var resultB: CompanyGetByIDResponse
            var resultC: EngineerGetByIDResponse

            withContext(Dispatchers.IO) {
                try {
                    resultA = service.loginAccount(email, password)
                    saveData(resultA.database?.level!!, null, null, resultA.database?.token)
                    headerInterceptor.role = resultA.database?.level!!

                    Log.d("goHipe : ", resultA.toString())
                    val responseStatus = resultA.success

                    if (responseStatus && resultA.database?.level == "Company") {

                        resultB = service.getCompanyByID(resultA.database!!.acID)
                        loginAction(resultA.database?.level!!, resultA, resultB, null)

                    } else if (responseStatus && resultA.database?.level == "Engineer") {

                        resultC = service.getEngineerByID(resultA.database!!.acID)
                        loginAction(resultA.database?.level!!, resultA, null, resultC)

                    } else {
                        dialog.dialogCancel(context, resultA.message) { DialogInterface.BUTTON_NEGATIVE }
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loginAction(role: String, modelA: LoginResponse, modelB: CompanyGetByIDResponse?, modelC: EngineerGetByIDResponse?) {
        when (role) {
            "Company" -> {
                saveData(role, modelA.database?.acID.toString(), modelB?.database!![0].cpID, modelA.database?.token!!)

                dialog.dialogCancel(context, "Login Successful") {
                    val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                    startActivity(sendIntent)
                    activity?.finish()
                }
            }
            "Engineer" -> {
                saveData(role, modelA.database?.acID.toString(), modelC?.database!![0].enID, modelA.database?.token!!)

                dialog.dialogCancel(context, "Login Successful") {
                    val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                    startActivity(sendIntent)
                    activity?.finish()
                }
            }
        }
    }

    private fun saveData(role: String, acID: String?, roleID: String?, token: String?) {
        val userPreference = GoHipePreferences(context!!)

        when (role) {
            "Company" -> {
                companyModel.acID = acID
                companyModel.compID = roleID
                companyModel.token = token
                companyModel.isLogin = true

                userPreference.setCompanyPreference(companyModel)
            }
            "Engineer" -> {
                engineerModel.acID = acID
                engineerModel.engID = roleID
                engineerModel.token = token
                engineerModel.isLogin = true

                userPreference.setEngineerPreference(engineerModel)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}