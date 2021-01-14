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
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.models.EngineerPreferenceModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentLoginScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerRegisterScreenFragment
import com.istekno.gohipeandroidapp.models.CompanyPreferenceModel
import com.istekno.gohipeandroidapp.retrofit.*
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*

class LoginScreenFragment : Fragment() {

    companion object {
        const val CHECK_PROFILE_AUTH_KEY = "check_profile_auth_key"
    }

    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerPreferenceModel: EngineerPreferenceModel
    private lateinit var companyPreferenceModel: CompanyPreferenceModel
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inheritClassAssigned(view)
        clickListener(view)
    }

    private fun inheritClassAssigned(view: View) {
        companyPreferenceModel = CompanyPreferenceModel()
        engineerPreferenceModel = EngineerPreferenceModel()
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
    }

    private fun clickListener(view: View) {
        binding.tvLoginfrgForgotPassword.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, ForgotPasswordScreenFragment())?.addToBackStack(null)?.commit()
        }

        binding.btnLoginfrgLogin.setOnClickListener {
            login(view)
        }

        binding.tvLoginfrgRegisterHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, SelectRoleFragment())?.addToBackStack(null)?.commit()
        }
    }

    private fun login(view: View) {
        val email = binding.etLoginfrgEmail.text.toString()
        val password = binding.etLoginfrgPassword.text.toString()

        goHipePreferences = GoHipePreferences(view.context)
        engineerPreferenceModel = goHipePreferences.getEngineerPreference()
        companyPreferenceModel = goHipePreferences.getCompanyPreference()

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
                    saveData(resultA.database?.level!!, null, null, resultA.database?.token, false)

                    Log.d("goHipe : ", resultA.toString())
                    val responseStatus = resultA.success

                    if (responseStatus && resultA.database?.level == "Company") {

                        resultB = service.getCompanyByID(resultA.database!!.acID)
                        loginAction(resultA.database?.level!!, resultA, resultB, null)

                    } else if (responseStatus && resultA.database?.level == "Engineer") {

                        resultC = service.getEngineerByID(resultA.database!!.acID)
                        loginAction(resultA.database?.level!!, resultA, null, resultC)

                    }

                } catch (e: Throwable) {
                    val status = e.toString().split(" ")[2]
                    activity?.runOnUiThread {
                        if (status == "403") {
                            val msg = "Sorry, Wrong Password!"
                            dialog.dialogCancel(context, msg) { DialogInterface.BUTTON_NEGATIVE }
                        } else if (status == "404") {
                            val msg = "Unfortunely, Your email is not registered!"
                            dialog.dialogCancel(context, msg) { DialogInterface.BUTTON_NEGATIVE }
                        }
                    }
                }
            }
        }
    }

    private fun loginAction(role: String, modelA: LoginResponse, modelB: CompanyGetByIDResponse?, modelC: EngineerGetByIDResponse?) {
        when (role) {
            "Company" -> {
                val list = modelB?.database?.map {
                    CompanyModelResponse(it.cpID, it.cpName, it.cpEmail, it.cpPhone, it.cpCompany, it.cpPosition, it.cpField, it.cpLocation, it.cpDesc,
                            it.cpInsta, it.cpLinkedIn, it.cpAvatar)
                }

                saveData(role, modelA.database?.acID, modelB?.database!![0].cpID, modelA.database?.token!!, true)

                activity?.runOnUiThread {
                    dialog.dialogCancel(context, "Login Successful") {
                        val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                        sendIntent.putExtra(CHECK_PROFILE_AUTH_KEY, list?.get(0))
                        startActivity(sendIntent)
                        activity?.finish()
                    }
                }
            }

            "Engineer" -> {
                val list = modelC?.database?.map {
                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                }

                saveData(role, modelA.database?.acID, modelC?.database!![0].enID, modelA.database?.token!!, true)

                activity?.runOnUiThread {
                    dialog.dialogCancel(context, "Login Successful") {
                        val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                        sendIntent.putExtra(CHECK_PROFILE_AUTH_KEY, list?.get(0))
                        startActivity(sendIntent)
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun saveData(role: String, acID: Long?, roleID: Long?, token: String?, isLogin: Boolean) {
        val userPreference = GoHipePreferences(context!!)

        when (role) {
            "Company" -> {
                companyPreferenceModel.acID = acID
                companyPreferenceModel.compID = roleID
                companyPreferenceModel.level = role
                companyPreferenceModel.token = token
                companyPreferenceModel.isLogin = isLogin

                userPreference.setCompanyPreference(companyPreferenceModel)
            }
            "Engineer" -> {
                engineerPreferenceModel.acID = acID
                engineerPreferenceModel.engID = roleID
                engineerPreferenceModel.level = role
                engineerPreferenceModel.token = token
                engineerPreferenceModel.isLogin = isLogin

                userPreference.setEngineerPreference(engineerPreferenceModel)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}