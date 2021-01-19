package com.istekno.gohipeandroidapp.maincontent.engineer

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
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerRegisterScreenBinding
import com.istekno.gohipeandroidapp.maincontent.LoginScreenFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerRegisterResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*


class EngineerRegisterScreenFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val FIELD_DIGITS_ONLY = "Number only"
        const val FIELD_IS_NOT_VALID = "Email format is not valid"
        const val FIELD_MUST_MATCH = "Password must match"
        const val FIELD_LENGTH = "Password min. 8 characters"
    }

    private lateinit var binding: FragmentEngineerRegisterScreenBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_register_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)

        binding.tvEngregisterfrgLoginHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment())?.commit()
        }
        binding.btnEngregisterfrgRegister.setOnClickListener {
            registration(view)
        }
    }

    private fun registration(view: View) {
        val inputFullname = binding.etEngregisterfrgFullname.text.toString().trim()
        val inputEmail = binding.etEngregisterfrgEmail.text.toString().trim()
        val inputPhone = binding.etEngregisterfrgPhone.text.toString().trim()
        val inputPassword = binding.etEngregisterfrgPassword.text.toString().trim()
        val inputConfirmPassword = binding.etEngregisterfrgConfirmPassword.text.toString().trim()

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

        registerEngineer(inputFullname, inputEmail, inputPhone, inputPassword)
    }

    private fun registerEngineer(name: String, email: String, phone: String, password: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerEngineer(name, email, phone, password)
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
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment())?.commit()
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