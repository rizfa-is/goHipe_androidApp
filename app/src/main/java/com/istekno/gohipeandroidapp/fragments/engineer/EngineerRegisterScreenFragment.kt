package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerRegisterScreenBinding
import com.istekno.gohipeandroidapp.fragments.LoginScreenFragment
import com.istekno.gohipeandroidapp.fragments.SelectRoleFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerRegisterModelRequest
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.LoginResponse
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
    private lateinit var engineerModel: EngineerModel
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, SelectRoleFragment())?.commit()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_register_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        engineerModel = EngineerModel()
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient()!!.create(GoHipeApiService::class.java)

        binding.tvEngregisterfrgLoginHere.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frame_container_logregact, LoginScreenFragment())?.commit()
        }
        binding.btnEngregisterfrgRegister.setOnClickListener {
            registerEngineer("Rock D Xebec", "rockdxx@gmail.com", "089786546321", "rockD11")
            registration()
        }
    }

    private fun registerEngineer(name: String, email: String, phone: String, password: String) {
        val registerModel = EngineerRegisterModelRequest(name, email, phone, password)

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.registerEngineer(registerModel)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is LoginResponse) {
                Log.d("goHipe : ", result.toString())
                val listResponse = result.database
            }
        }
    }

    private fun registration() {
        val inputFullname = binding.etEngregisterfrgFullname.text.toString()
        val inputEmail = binding.etEngregisterfrgEmail.text.toString()
        val inputPhone = binding.etEngregisterfrgPhone.text.toString()
        val inputPassword = binding.etEngregisterfrgPassword.text.toString()
        val inputConfirmPassword = binding.etEngregisterfrgConfirmPassword.text.toString()

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

        saveData(inputFullname, inputEmail, inputPassword, inputPhone, true)

        dialog.dialogCancel(context, "Register Successful") {
            val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
            startActivity(sendIntent)
        }
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

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}