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
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentLoginScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerRegisterScreenFragment
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.LoginModelRequest
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient()!!.create(GoHipeApiService::class.java)

        clickListener(view)
    }

    private fun clickListener(view: View) {
        binding.tvLoginfrgForgotPassword.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, ForgotPasswordScreenFragment())?.commit()
        }

        binding.btnLoginfrgLogin.setOnClickListener {
            loginEngineer("nicoarc12@gmail.com","arcOhara99")
            login(view)
        }

        binding.tvLoginfrgRegisterHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, SelectRoleFragment())?.commit()
        }
    }

    private fun loginEngineer(email: String, password: String) {
        val loginModel = LoginModelRequest(email, password)

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginAccount(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is LoginResponse) {
                Log.d("goHipe : ", result.toString())
                val listResponse = result.database
            }
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

        if (engineerModel.email == email && engineerModel.password == password) {
            engineerModel.isLogin = true
            goHipePreferences.setEngineerPreference(engineerModel)

            dialog.dialogCancel(context, "Login Successful") {
                val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                startActivity(sendIntent)
                activity?.finish()
            }
        } else if (companyModel.email == email && companyModel.password == password) {
            companyModel.isLogin = true
            goHipePreferences.setCompanyPreference(companyModel)

            dialog.dialogCancel(context, "Login Successful") {
                val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                startActivity(sendIntent)
                activity?.finish()
            }
        } else {
            dialog.dialogCancel(context, "Email or Password Incorrect") { DialogInterface.BUTTON_NEGATIVE }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}