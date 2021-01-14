package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerRegisterScreenBinding
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerRegisterResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*


class EngineerRegisterScreenFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val FIELD_DIGITS_ONLY = "Hanya boleh berisi numerik"
        const val FIELD_IS_NOT_VALID = "Email tidak valid"
        const val FIELD_MUST_MATCH = "Password harus sama"
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
            registration()
        }
    }

    private fun registration() {
        val inputFullname = binding.etEngregisterfrgFullname.text.toString().trim()
        val inputEmail = binding.etEngregisterfrgEmail.text.toString().trim()
        val inputPhone = binding.etEngregisterfrgPhone.text.toString().trim()
        val inputPassword = binding.etEngregisterfrgPassword.text.toString().trim()
        val inputConfirmPassword = binding.etEngregisterfrgConfirmPassword.text.toString().trim()

        if (inputFullname.isEmpty()) {
            binding.etEngregisterfrgFullname.error = FIELD_REQUIRED
            return
        }

        if (inputEmail.isEmpty()) {
            binding.etEngregisterfrgEmail.error = FIELD_IS_NOT_VALID
            return
        }

        if (inputPassword.isEmpty()) {
            binding.etEngregisterfrgPassword.error = FIELD_REQUIRED
            return
        }

        if (inputPhone.isEmpty()) {
            binding.etEngregisterfrgPhone.error = FIELD_REQUIRED
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            binding.etEngregisterfrgPhone.error = FIELD_DIGITS_ONLY
            return
        }

        if (inputPassword != inputConfirmPassword) {
            binding.etEngregisterfrgConfirmPassword.error = FIELD_MUST_MATCH
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

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}