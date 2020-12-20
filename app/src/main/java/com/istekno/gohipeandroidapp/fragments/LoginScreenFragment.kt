package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainContentActivity
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.data.EngineerModel
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import com.istekno.gohipeandroidapp.databases.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentLoginScreenBinding
import kotlinx.android.synthetic.main.fragment_engineer_register_screen.*
import kotlinx.android.synthetic.main.fragment_login_screen.*

class LoginScreenFragment : Fragment() {

    companion object {
        const val CODENAME1 = "loginEmail"
        const val CODENAME2 = "loginPassword"
    }

    private lateinit var loginScreenBinding: FragmentLoginScreenBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)
        // Inflate the layout for this fragment
        return loginScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        loginScreenBinding.tvLoginfrgForgotPassword.setOnClickListener {
            mFragment = ForgotPasswordScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, ForgotPasswordScreenFragment::class.java.simpleName)
                commit()
            }
        }

        loginScreenBinding.btnLoginfrgLogin.setOnClickListener {
            login(view)
        }

        loginScreenBinding.tvLoginfrgRegisterHere.setOnClickListener {
            mFragment = SelectRoleFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, SelectRoleFragment::class.java.simpleName)
                commit()
            }
        }
    }

    private fun login(view: View) {
        val email = loginScreenBinding.etLoginfrgEmail.text.toString()
        val password = loginScreenBinding.etLoginfrgPassword.text.toString()

        goHipePreferences = GoHipePreferences(view.context)
        engineerModel = goHipePreferences.getEngineerPreference()

        Log.d("PREF : ", engineerModel.email.toString())

        if (email.isEmpty()) {
            loginScreenBinding.etLoginfrgEmail.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (password.isEmpty()) {
            loginScreenBinding.etLoginfrgPassword.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (engineerModel.email!!.contains(email) && engineerModel.password!!.contains(password)) {
            engineerModel.isLogin = true
            goHipePreferences.setEngineerPreference(engineerModel)
            startActivity(Intent(context, MainContentActivity::class.java))
            activity?.finish()
        }
    }
}