package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileAccountBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerDeleteResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerRegisterResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*

class EngineerEditProfileAccountFragment : Fragment() {

    private val listDropdownJobtype = listOf("Freelance", "Fulltime")

    private lateinit var binding: FragmentEngineerEditProfileAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)

        setDropdownMenuAdapter(view)
        viewListener()
    }

    private fun viewListener() {
        binding.btnComregisterfrgUpdate.setOnClickListener {
            update()
        }
    }

    private fun updateEngineer(name: String, email: String, phone: String, password: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.updateEngineer(6, name, email, phone, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerDeleteResponse) {
                Log.d("goHipe : ", result.toString())
            }
        }
    }

    private fun update() {
        val inputFullname = binding.etEngeditaccountfrgFullname.text.toString().trim()
        val inputEmail = binding.etEngeditaccountfrgEmail.text.toString().trim()
        val inputPhone = binding.etEngeditaccountfrgPhone.text.toString().trim()
        val inputPassword = binding.etEngeditaccountfrgPassword.text.toString().trim()
        val inputConfirmPassword = binding.etEngeditaccountfrgConfirmpass.text.toString().trim()

        if (inputPassword != inputConfirmPassword) {
            binding.etEngeditaccountfrgConfirmpass.error = EngineerRegisterScreenFragment.FIELD_MUST_MATCH
            return
        }

        updateEngineer(inputFullname, inputEmail, inputPhone, inputPassword)
//        saveData(inputFullname, inputEmail, inputPassword, inputPhone, true)

        dialog.dialogCancel(context, "Profile Updated") {
            val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
//            startActivity(sendIntent)
        }
    }

    private fun setDropdownMenuAdapter(view: View) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, listDropdownJobtype)
        (binding.itEngeditaccountfrgJobtype.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}