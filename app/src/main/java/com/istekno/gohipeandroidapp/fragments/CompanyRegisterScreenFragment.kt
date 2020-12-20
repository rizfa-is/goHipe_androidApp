package com.istekno.gohipeandroidapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.MainContentActivity
import com.istekno.gohipeandroidapp.data.CompanyModel
import com.istekno.gohipeandroidapp.databases.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyRegisterScreenBinding
import com.istekno.gohipeandroidapp.utility.Dialog

class CompanyRegisterScreenFragment : Fragment() {

    private lateinit var binding: FragmentCompanyRegisterScreenBinding
    private lateinit var companyModel: CompanyModel
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, SelectRoleFragment(), SelectRoleFragment::class.java.simpleName)?.commit()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_register_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companyModel = CompanyModel()
        dialog = Dialog()

        binding.tvComregisterfrgLoginHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment(), LoginScreenFragment::class.java.simpleName)?.commit()
        }

        binding.btnComregisterfrgRegister.setOnClickListener {
            registration(view)
        }
    }

    private fun registration(view: View) {
        val inputFullname = binding.etComregisterfrgFullname.text.toString()
        val inputEmail = binding.etComregisterfrgEmail.text.toString()
        val inputPhone = binding.etComregisterfrgPhone.text.toString()
        val inputCompany = binding.etComregisterfrgCompany.text.toString()
        val inputPosition = binding.etComregisterfrgPosition.text.toString()
        val inputPassword = binding.etComregisterfrgPassword.text.toString()
        val inputConfirmPass = binding.etComregisterfrgConfirmPassword.text.toString()

        if (inputFullname.isEmpty()) {
            binding.etComregisterfrgFullname.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputEmail.isEmpty()) {
            binding.etComregisterfrgEmail.error = EngineerRegisterScreenFragment.FIELD_IS_NOT_VALID
            return
        }

        if (inputPassword.isEmpty()) {
            binding.etComregisterfrgPassword.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputCompany.isEmpty()) {
            binding.etComregisterfrgCompany.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputPosition.isEmpty()) {
            binding.etComregisterfrgPosition.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputConfirmPass.isEmpty()) {
            binding.etComregisterfrgConfirmPassword.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputPhone.isEmpty()) {
            binding.etComregisterfrgPhone.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            binding.etComregisterfrgPhone.error = EngineerRegisterScreenFragment.FIELD_DIGITS_ONLY
            return
        }

        saveData(inputFullname, inputEmail, inputPassword, inputCompany, inputPosition, inputPhone, true)
        dialog.dialog(context, "Register Successful") { startActivity(Intent(context, MainContentActivity::class.java)) }
    }

    private fun saveData(name: String, email: String, password: String, company: String, position: String, phone: String, isLogin: Boolean) {
        val userPreference = GoHipePreferences(context!!)

        companyModel.name = name
        companyModel.email = email
        companyModel.password = password
        companyModel.company = company
        companyModel.position = position
        companyModel.phone = phone.toLong()
        companyModel.isLogin = isLogin

        userPreference.setCompanyPreference(companyModel)
    }
}