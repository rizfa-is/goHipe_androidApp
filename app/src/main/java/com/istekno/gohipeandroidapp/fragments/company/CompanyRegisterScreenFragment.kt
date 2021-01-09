package com.istekno.gohipeandroidapp.fragments.company

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyRegisterScreenBinding
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment
import com.istekno.gohipeandroidapp.fragments.SelectRoleFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerRegisterResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*

class CompanyRegisterScreenFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val FIELD_DIGITS_ONLY = "Hanya boleh berisi numerik"
        const val FIELD_IS_NOT_VALID = "Email tidak valid"
        const val FIELD_MUST_MATCH = "Password harus sama"
    }
    
    private lateinit var binding: FragmentCompanyRegisterScreenBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_register_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)

        binding.tvComregisterfrgLoginHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment(), LoginScreenFragment::class.java.simpleName)?.commit()
        }

        binding.btnComregisterfrgRegister.setOnClickListener {
            registration()
        }
    }

    private fun registration() {
        val inputFullname = binding.etComregisterfrgFullname.text.toString()
        val inputEmail = binding.etComregisterfrgEmail.text.toString()
        val inputPhone = binding.etComregisterfrgPhone.text.toString()
        val inputCompany = binding.etComregisterfrgCompany.text.toString()
        val inputPosition = binding.etComregisterfrgPosition.text.toString()
        val inputPassword = binding.etComregisterfrgPassword.text.toString()
        val inputConfirmPassword = binding.etComregisterfrgConfirmPassword.text.toString()

        if (inputFullname.isEmpty()) {
            binding.etComregisterfrgFullname.error = FIELD_REQUIRED
            return
        }

        if (inputEmail.isEmpty()) {
            binding.etComregisterfrgEmail.error = FIELD_IS_NOT_VALID
            return
        }

        if (inputPassword.isEmpty()) {
            binding.etComregisterfrgPassword.error = FIELD_REQUIRED
            return
        }

        if (inputCompany.isEmpty()) {
            binding.etComregisterfrgCompany.error = FIELD_REQUIRED
            return
        }

        if (inputPosition.isEmpty()) {
            binding.etComregisterfrgPosition.error = FIELD_REQUIRED
            return
        }

        if (inputConfirmPassword.isEmpty()) {
            binding.etComregisterfrgConfirmPassword.error = FIELD_REQUIRED
            return
        }

        if (inputPhone.isEmpty()) {
            binding.etComregisterfrgPhone.error = FIELD_REQUIRED
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            binding.etComregisterfrgPhone.error = FIELD_DIGITS_ONLY
            return
        }

        if (inputPassword != inputConfirmPassword) {
            binding.etComregisterfrgConfirmPassword.error = FIELD_MUST_MATCH
            return
        }

        registerCompany(inputFullname, inputEmail, inputPhone, inputPassword, inputCompany, inputPosition)
    }

    private fun registerCompany(name: String, email: String, phone: String, password: String, company: String, position: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerCompany(name, email, phone, password, company, position)
                } catch (e: Throwable) {
                    val status = e.toString().split(" ")[2]
                    activity?.runOnUiThread {
                        if (status == "404") {
                            val msg = "Unfortunely, Email already exist!"
                            dialog.dialogCancel(context, msg) { DialogInterface.BUTTON_NEGATIVE }
                        }
                    }
                }
            }

            if (result is EngineerRegisterResponse) {
                dialog.dialogCancel(context, "Register Successful") {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment(), LoginScreenFragment::class.java.simpleName)?.commit()
                }
            }
        }
    }
}