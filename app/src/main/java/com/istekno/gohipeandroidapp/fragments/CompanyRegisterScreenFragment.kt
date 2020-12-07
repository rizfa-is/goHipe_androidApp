package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import kotlinx.android.synthetic.main.fragment_company_register_screen.*

class CompanyRegisterScreenFragment : Fragment() {

    companion object {
        val CODENAME1_COMP_REG_FULLNAME = "company_reg_fullname"
        val CODENAME2_COMP_REG_EMAIL = "company_reg_email"
        val CODENAME3_COMP_REG_PHONE = "company_reg_phone"
        val CODENAME4_COMP_REG_PASSWORD = "company_reg_password"
        val CODENAME5_COMP_REG_COMPANY = "company_reg_company"
        val CODENAME6_COMP_REG_POSITION = "company_reg_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mFragment = SelectRoleFragment()
                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frame_container_logregact, mFragment, SelectRoleFragment::class.java.simpleName)
                    commit()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_register_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        tv_comregisterfrg_login_here.setOnClickListener {
            mFragment = LoginScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, LoginScreenFragment::class.java.simpleName)
                commit()
            }
        }
        btn_comregisterfrg_register.setOnClickListener {
            registration(view)
        }
    }

    private fun registration(view: View) {
        val inputFullname = et_comregisterfrg_fullname.text.toString()
        val inputEmail = et_comregisterfrg_email.text.toString()
        val inputPhone = et_comregisterfrg_phone.text.toString()
        val inputCompany = et_comregisterfrg_company.text.toString()
        val inputPosition = et_comregisterfrg_position.text.toString()
        val inputPassword = et_comregisterfrg_password.text.toString()
        val inputConfirmPass = et_comregisterfrg_confirm_password.text.toString()

        val intent = Intent(context, ProfileScreenActivity::class.java)
        intent.putExtra("Codename Profile", 1)
        intent.putExtra(CODENAME1_COMP_REG_FULLNAME, inputFullname)
        intent.putExtra(CODENAME2_COMP_REG_EMAIL, inputEmail)
        intent.putExtra(CODENAME3_COMP_REG_PHONE, inputPhone)
        intent.putExtra(CODENAME4_COMP_REG_PASSWORD, inputPassword)
        intent.putExtra(CODENAME5_COMP_REG_COMPANY, inputCompany)
        intent.putExtra(CODENAME6_COMP_REG_POSITION, inputPosition)

        if (inputFullname == "" || inputEmail == "" || inputPhone == "" || inputPassword == "") {
            Toast.makeText(view.context, "Please, all form must be filled", Toast.LENGTH_LONG).show()
        } else if (inputPassword != inputConfirmPass) {
            Toast.makeText(view.context, "Make sure you inputed right password", Toast.LENGTH_LONG).show()
        } else {
            startActivity(intent)
            activity?.finish()
        }
    }
}