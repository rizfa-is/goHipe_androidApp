package com.istekno.gohipeandroidapp.fragments.company

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyEditProfileAccountBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerEditProfileAccountFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerRegisterScreenFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.CompanyModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class CompanyEditProfileAccountFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val REQUEST_CODE = 1000
        const val ACC_UPDATE_AUTH_KEY = "acc_update_auth_key"
        const val EDIT_PROFILE_AUTH_KEY2 = "edit_profile_auth_key2"
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentCompanyEditProfileAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var imageName: MultipartBody.Part
    private var dataImage = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_edit_profile_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        setText(view)
        viewListener(view)
    }

    private fun setText(view: View) {
        val data = activity?.intent?.getParcelableExtra<CompanyModelResponse>(EDIT_PROFILE_AUTH_KEY2)
        val fullname = binding.etComeditaccountfrgFullname
        val email = binding.etComeditaccountfrgEmail
        val company = binding.etComeditaccountfrgCompany
        val position = binding.etComeditaccountfrgPosition
        val phone = binding.etComeditaccountfrgPhone
        val field = binding.etComeditaccountfrgField
        val location = binding.etComeditaccountfrgLocation
        val desc = binding.etComeditaccountfrgDesc
        val ig = binding.etComeditaccountfrgInsta
        val linkedin = binding.etComeditaccountfrgLinkedin

        fullname.setText(data?.cpName)
        email.setText(data?.cpEmail)
        company.setText(data?.cpCompany)
        position.setText(data?.cpPosition)
        phone.setText(data?.cpPhone)
        field.setText(data?.cpField)
        location.setText(data?.cpLocation)
        desc.setText(data?.cpDesc)
        ig.setText(data?.cpInsta)
        linkedin.setText(data?.cpLinkedIn)
        Glide.with(view.context).load(EngineerEditProfileAccountFragment.imageLink + data?.cpAvatar).into(binding.shapeableImageView2)
    }

    private fun viewListener(view: View) {
        binding.btnComeditfrgUpdate.setOnClickListener {
            update(view)
        }
        binding.editImageButton.setOnClickListener {
            if (EasyPermissions.hasPermissions(view.context,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access image gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val dataResponse = data?.data?.path?.replace("/raw/".toRegex(), "")
            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), File(dataResponse!!))

            dataImage = dataResponse
            imageName = MultipartBody.Part.createFormData("image", File(dataResponse).name, requestBody)
            Glide.with(this).load(dataResponse).into(binding.shapeableImageView2)
        }
    }

    private fun updateCompany(name: String, email: String, phone: String, password: String, company: String,
                              position: String, field: String, location: String, desc: String, ig: String, linkedin: String) {
        val id = goHipePreferences.getCompanyPreference().acID
        val cName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val cEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val cPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val cPass = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val cComp = company.toRequestBody("text/plain".toMediaTypeOrNull())
        val cPos = position.toRequestBody("text/plain".toMediaTypeOrNull())
        val cField = field.toRequestBody("text/plain".toMediaTypeOrNull())
        val cLoc = location.toRequestBody("text/plain".toMediaTypeOrNull())
        val cDesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val cIG = ig.toRequestBody("text/plain".toMediaTypeOrNull())
        val cLinkedin = linkedin.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.updateCompany(id!!, cName, cEmail, cPhone, cPass, cComp, cPos, cField, cLoc, cDesc, cIG, cLinkedin, imageName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun update(view: View) {
        val inputFullname = binding.etComeditaccountfrgFullname.text.toString().trim()
        val inputEmail = binding.etComeditaccountfrgEmail.text.toString().trim()
        val inputPhone = binding.etComeditaccountfrgPhone.text.toString().trim()
        val inputPassword = binding.etComeditaccountfrgPassword.text.toString().trim()
        val inputConfirmPassword = binding.etComeditaccountfrgConfirmpass.text.toString().trim()
        val inputCompany = binding.etComeditaccountfrgCompany.text.toString().trim()
        val inputPosition = binding.etComeditaccountfrgPosition.text.toString().trim()
        val inputField = binding.etComeditaccountfrgField.text.toString().trim()
        val inputLocation = binding.etComeditaccountfrgLocation.text.toString().trim()
        val inputDesc = binding.etComeditaccountfrgDesc.text.toString().trim()
        val inputIG = binding.etComeditaccountfrgInsta.text.toString().trim()
        val inputLinkedin = binding.etComeditaccountfrgLinkedin.text.toString().trim()

        if (inputFullname.isEmpty()) {
            binding.etComeditaccountfrgFullname.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputEmail.isEmpty()) {
            binding.etComeditaccountfrgEmail.error = EngineerRegisterScreenFragment.FIELD_IS_NOT_VALID
            return
        }

        if (inputPassword.isEmpty()) {
            binding.etComeditaccountfrgPassword.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputPhone.isEmpty()) {
            binding.etComeditaccountfrgPhone.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputCompany.isEmpty()) {
            binding.etComeditaccountfrgCompany.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputPosition.isEmpty()) {
            binding.etComeditaccountfrgPosition.error = EngineerRegisterScreenFragment.FIELD_IS_NOT_VALID
            return
        }

        if (inputField.isEmpty()) {
            binding.etComeditaccountfrgField.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputLocation.isEmpty()) {
            binding.etComeditaccountfrgLocation.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputDesc.isEmpty()) {
            binding.etComeditaccountfrgDesc.error = EngineerRegisterScreenFragment.FIELD_IS_NOT_VALID
            return
        }

        if (inputIG.isEmpty()) {
            binding.etComeditaccountfrgInsta.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputLinkedin.isEmpty()) {
            binding.etComeditaccountfrgLinkedin.error = EngineerRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputPassword != inputConfirmPassword) {
            binding.etComeditaccountfrgConfirmpass.error = EngineerRegisterScreenFragment.FIELD_MUST_MATCH
            return
        }

        if (dataImage != "") {

            updateCompany(inputFullname, inputEmail, inputPhone, inputPassword, inputCompany, inputPosition, inputField, inputLocation, inputDesc, inputIG, inputLinkedin)

            dialog.dialogCancel(context, "Success update account!") {
                val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                sendIntent.putExtra(ACC_UPDATE_AUTH_KEY, 1)
                startActivity(sendIntent)
            }

        } else {
            Toast.makeText(view.context, "Please select account image!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Edit Profile"
    }
}