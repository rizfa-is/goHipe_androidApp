package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainContentActivity
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.data.EngineerModel
import com.istekno.gohipeandroidapp.databases.GoHipePreferences
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.android.synthetic.main.fragment_engineer_register_screen.*


class EngineerRegisterScreenFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val FIELD_DIGITS_ONLY = "Hanya boleh berisi numerik"
        const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    private lateinit var engineerModel: EngineerModel
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, SelectRoleFragment())?.commit()
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
        engineerModel = EngineerModel()
        dialog = Dialog()

        tv_engregisterfrg_login_here.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment())?.commit()
        }
        btn_engregisterfrg_register.setOnClickListener {
            registration()
        }
    }

    private fun registration() {
        val inputFullname = et_engregisterfrg_fullname.text.toString()
        val inputEmail = et_engregisterfrg_email.text.toString()
        val inputPhone = et_engregisterfrg_phone.text.toString()
        val inputPassword = et_engregisterfrg_password.text.toString()

        if (inputFullname.isEmpty()) {
            et_engregisterfrg_fullname.error = FIELD_REQUIRED
            return
        }

        if (inputEmail.isEmpty()) {
            et_engregisterfrg_email.error = FIELD_IS_NOT_VALID
            return
        }

        if (inputPassword.isEmpty()) {
            et_engregisterfrg_password.error = FIELD_REQUIRED
            return
        }

        if (inputPhone.isEmpty()) {
            et_engregisterfrg_phone.error = FIELD_REQUIRED
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            et_engregisterfrg_phone.error = FIELD_DIGITS_ONLY
            return
        }

        saveData(inputFullname, inputEmail, inputPassword, inputPhone, true)
        startActivity(Intent(context, MainContentActivity::class.java))
        dialog.dialog(context, "Register Successful")
    }

    private fun saveData(name: String, email: String, password: String, phone: String, isLogin: Boolean) {
        val userPreference = GoHipePreferences(context!!)

        engineerModel.name = name
        engineerModel.email = email
        engineerModel.password = password
        engineerModel.phone = phone.toLong()
        engineerModel.isLogin = isLogin

        userPreference.setEngineerPreference(engineerModel)
    }
}