package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentLoginScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerRegisterScreenFragment
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.utility.Dialog

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel
    private lateinit var companyModel: CompanyModel
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()

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

        if (engineerModel.email == email && engineerModel.password == password) {
            engineerModel.isLogin = true
            goHipePreferences.setEngineerPreference(engineerModel)

            dialog.dialog(context, "Login Successful") {
                val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                startActivity(sendIntent)
                activity?.finish()
            }
        } else if (companyModel.email == email && companyModel.password == password) {
            companyModel.isLogin = true
            goHipePreferences.setCompanyPreference(companyModel)

            dialog.dialog(context, "Login Successful") {
                val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                startActivity(sendIntent)
                activity?.finish()
            }
        } else {
            dialog.dialogCancel(context, "Email or Password Incorrect")
        }
    }
}