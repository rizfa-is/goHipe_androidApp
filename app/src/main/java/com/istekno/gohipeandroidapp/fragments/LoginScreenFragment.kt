package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.data.GoHipeDatabases
import kotlinx.android.synthetic.main.fragment_login_screen.*

class LoginScreenFragment : Fragment() {

    companion object {
        const val CODENAME1 = "loginEmail"
        const val CODENAME2 = "loginPassword"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        tv_loginfrg_forgot_password.setOnClickListener {
            mFragment = ForgotPasswordScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, ForgotPasswordScreenFragment::class.java.simpleName)
                commit()
            }
        }

        btn_loginfrg_login.setOnClickListener {
            login(view)
        }

        tv_loginfrg_register_here.setOnClickListener {
            mFragment = SelectRoleFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, SelectRoleFragment::class.java.simpleName)
                commit()
            }
        }
    }

    private fun login(view: View) {
        val listEmailENG = GoHipeDatabases.loginEmailEngineer
        val listPasswordENG = GoHipeDatabases.loginPasswordEngineer
        val listEmailCOMP = GoHipeDatabases.loginEmailCompany
        val listPasswordCOMP = GoHipeDatabases.loginPasswordCompany

        val inputEmail = et_loginfrg_email.text.toString()
        val inputPassword = et_loginfrg_password.text.toString()
        val intent = Intent(view.context, ProfileScreenActivity::class.java)
        intent.putExtra(CODENAME1, inputEmail)
        intent.putExtra(CODENAME2, inputPassword)

        if (listEmailENG.contains(inputEmail) && listPasswordENG.contains(inputPassword)) {
            startActivity(intent)
            activity?.finish()
        } else if (listEmailCOMP.contains(inputEmail) && listPasswordCOMP.contains(inputPassword)) {
            startActivity(intent)
            activity?.finish()
        } else if (inputEmail == "" || inputPassword == "") {
            Toast.makeText(view.context, "Please, input email & password", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(view.context, "Account haven't registered", Toast.LENGTH_LONG).show()
        }
    }
}