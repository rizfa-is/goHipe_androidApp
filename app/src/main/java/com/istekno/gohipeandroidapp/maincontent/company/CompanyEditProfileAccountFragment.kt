package com.istekno.gohipeandroidapp.maincontent.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyEditProfileAccountBinding
import com.istekno.gohipeandroidapp.maincontent.engineer.EngineerEditProfileAccountFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.CompanyModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CompanyEditProfileAccountFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val FIELD_DIGITS_ONLY = "Number only"
        const val FIELD_IS_NOT_VALID = "Email format is not valid"

        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
        const val ACC_UPDATE_AUTH_KEY = "acc_update_auth_key"
        const val EDIT_PROFILE_AUTH_KEY2 = "edit_profile_auth_key2"
        const val EMPTY_DATA_AUTH_KEY = "empty_data_auth_key"
        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentCompanyEditProfileAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var imageName: MultipartBody.Part
    private lateinit var company: CompanyModelResponse
    private var pathImage = ""

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
        val dataEmpty = activity?.intent?.getParcelableExtra<CompanyModelResponse>(EMPTY_DATA_AUTH_KEY)

        if (dataEmpty != null) {
            company = dataEmpty
        } else if (data != null) {
            company = data
        }

        val fullname = binding.etComeditaccountfrgFullname
        val email = binding.etComeditaccountfrgEmail
        val companyEt = binding.etComeditaccountfrgCompany
        val position = binding.etComeditaccountfrgPosition
        val phone = binding.etComeditaccountfrgPhone
        val field = binding.etComeditaccountfrgField
        val location = binding.etComeditaccountfrgLocation
        val desc = binding.etComeditaccountfrgDesc
        val ig = binding.etComeditaccountfrgInsta
        val linkedin = binding.etComeditaccountfrgLinkedin

        fullname.setText(company.cpName)
        email.setText(company.cpEmail)
        companyEt.setText(company.cpCompany)
        position.setText(company.cpPosition)
        phone.setText(company.cpPhone)
        field.setText(company.cpField)
        location.setText(company.cpLocation)
        desc.setText(company.cpDesc)
        ig.setText(company.cpInsta)
        linkedin.setText(company.cpLinkedIn)

        if (!company.cpAvatar.isNullOrEmpty()) {
            Glide.with(view.context).load(EngineerEditProfileAccountFragment.imageLink + company.cpAvatar).into(binding.shapeableImageView2)
        }
    }

    private fun viewListener(view: View) {
        binding.btnComeditfrgUpdate.setOnClickListener {
            update(view)
        }
        binding.editImageButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.shapeableImageView2.setImageURI(data?.data)
            pathImage = getPath(context!!, data?.data!!)

            showToast(binding.root, pathImage)
            val file = File(pathImage)
            val reqFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            imageName = MultipartBody.Part.createFormData("image", file.name, reqFile)
        }
    }

    private fun getPath(context: Context, contentUri: Uri): String {
        var result = ""
        val image = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, image, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }

        return result
    }


    private fun updateCompany(type: Int, name: String, email: String, phone: String, company: String,
                              position: String, field: String, location: String, desc: String, ig: String, linkedin: String) {
        val id = goHipePreferences.getCompanyPreference().acID
        val cName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val cEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val cPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
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
                    if (type == 1) {
                        service.updateCompany(id!!, cName, cEmail, cPhone, cComp, cPos, cField, cLoc, cDesc, cIG, cLinkedin, imageName)
                    } else {
                        service.updateCompany(id!!, cName, cEmail, cPhone, cComp, cPos, cField, cLoc, cDesc, cIG, cLinkedin)
                    }
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
        val inputCompany = binding.etComeditaccountfrgCompany.text.toString().trim()
        val inputPosition = binding.etComeditaccountfrgPosition.text.toString().trim()
        val inputField = binding.etComeditaccountfrgField.text.toString().trim()
        val inputLocation = binding.etComeditaccountfrgLocation.text.toString().trim()
        val inputDesc = binding.etComeditaccountfrgDesc.text.toString().trim()
        val inputIG = binding.etComeditaccountfrgInsta.text.toString().trim()
        val inputLinkedin = binding.etComeditaccountfrgLinkedin.text.toString().trim()

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

        if (inputPhone.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            showToast(view, FIELD_DIGITS_ONLY)
            return
        }

        if (inputField.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputLocation.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputDesc.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputIG.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputLinkedin.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (pathImage != "") {

            updateCompany(1, inputFullname, inputEmail, inputPhone, inputCompany, inputPosition, inputField, inputLocation, inputDesc, inputIG, inputLinkedin)

            dialog.dialogCancel(context, "Success update account!") {
                val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                sendIntent.putExtra(ACC_UPDATE_AUTH_KEY, 1)
                startActivity(sendIntent)
            }

        } else {

            updateCompany(0, inputFullname, inputEmail, inputPhone, inputCompany, inputPosition, inputField, inputLocation, inputDesc, inputIG, inputLinkedin)

            dialog.dialogCancel(context, "Success update account!") {
                val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                sendIntent.putExtra(ACC_UPDATE_AUTH_KEY, 1)
                startActivity(sendIntent)
            }
        }
    }
    
    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Edit Profile"
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}