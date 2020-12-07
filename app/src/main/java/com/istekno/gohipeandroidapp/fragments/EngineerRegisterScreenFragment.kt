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
import kotlinx.android.synthetic.main.fragment_engineer_register_screen.*


class EngineerRegisterScreenFragment : Fragment() {

    companion object {
        val CODENAME1_ENG_REG_FULLNAME = "engineer_reg_fullname"
        val CODENAME2_ENG_REG_EMAIL = "engineer_reg_email"
        val CODENAME3_ENG_REG_PHONE = "engineer_reg_phone"
        val CODENAME4_ENG_REG_PASSWORD = "engineer_reg_password"
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
        return inflater.inflate(R.layout.fragment_engineer_register_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager
        var mFragment : Fragment

        tv_engregisterfrg_login_here.setOnClickListener {
            mFragment = LoginScreenFragment()
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container_logregact, mFragment, LoginScreenFragment::class.java.simpleName)
                commit()
            }
        }
        btn_engregisterfrg_register.setOnClickListener {
            registration(view)
        }
    }

    private fun registration(view: View) {
        val inputFullname = et_engregisterfrg_fullname.text.toString()
        val inputEmail = et_engregisterfrg_email.text.toString()
        val inputPhone = et_engregisterfrg_phone.text.toString()
        val inputPassword = et_engregisterfrg_password.text.toString()
        val inputConfirmPass = et_engregisterfrg_confirm_password.text.toString()

        val intent = Intent(context, ProfileScreenActivity::class.java)
        intent.putExtra("Codename Profile", 0)
        intent.putExtra(CODENAME1_ENG_REG_FULLNAME, inputFullname)
        intent.putExtra(CODENAME2_ENG_REG_EMAIL, inputEmail)
        intent.putExtra(CODENAME3_ENG_REG_PHONE, inputPhone)
        intent.putExtra(CODENAME4_ENG_REG_PASSWORD, inputPassword)

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