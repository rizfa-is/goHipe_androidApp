package com.istekno.gohipeandroidapp.ui.company.register

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyRegisterScreenBinding
import com.istekno.gohipeandroidapp.ui.login.LoginScreenFragment
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.CompanyRegisterResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.ui.Dialog
import kotlinx.coroutines.*

class CompanyRegisterScreenFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val FIELD_DIGITS_ONLY = "Number only"
        const val FIELD_IS_NOT_VALID = "Email format is not valid"
        const val FIELD_MUST_MATCH = "Password must match"
        const val FIELD_LENGTH = "Password min. 8 characters"
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
        val inputConfirmPassword = binding.etComregisterfrgConfirmPassword.text.toString()

        if (inputFullname.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputEmail.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (!inputEmail.contains('@') || !inputEmail.contains('.')) {
            showToast(view, FIELD_IS_NOT_VALID)
            return
        }

        if (inputCompany.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputPosition.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputPassword.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputPassword.length < 8) {
            showToast(view, FIELD_LENGTH)
            return
        }

        if (inputPhone.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            showToast(view, FIELD_DIGITS_ONLY)
            return
        }

        if (inputPassword != inputConfirmPassword) {
            showToast(view, FIELD_MUST_MATCH)
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

            if (result is CompanyRegisterResponse) {
                dialog.dialogCancel(context, "Register Successful") {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment(), LoginScreenFragment::class.java.simpleName)?.commit()
                }
            }
        }
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}