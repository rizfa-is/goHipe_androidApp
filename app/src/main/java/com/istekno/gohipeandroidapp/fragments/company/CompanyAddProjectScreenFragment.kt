package com.istekno.gohipeandroidapp.fragments.company

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAddProjectScreenBinding
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyRegisterScreenBinding
import com.istekno.gohipeandroidapp.models.ImagePickerModel
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GeneralResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.vincent.filepicker.Constant
import com.vincent.filepicker.Constant.MAX_NUMBER
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CompanyAddProjectScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val FIELD_DIGITS_ONLY = "Hanya boleh berisi numerik"
        const val FIELD_IS_NOT_VALID = "Email tidak valid"
        const val FIELD_MUST_MATCH = "Password harus sama"
    }

    private lateinit var binding: FragmentCompanyAddProjectScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var imageName: MultipartBody.Part

    private val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_add_project_screen, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        retrieveDate(view)
        viewListener(view)
    }

    private fun viewListener(view: View) {
        binding.editImageButton.setOnClickListener {

            if (EasyPermissions.hasPermissions(view.context,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, Constant.REQUEST_CODE_PICK_IMAGE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access image gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.btnCompaddprojectfrgAdd.setOnClickListener {
            addProject()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){

            val dataResponse = data.data.toString()
            val path = dataResponse

            Log.e("data",path)
            // membuat request body yang berisi file dari picked image.
            val requestBody = File(path).asRequestBody("multipart".toMediaTypeOrNull())

            // mengoper value dari requestbody sekaligus membuat form data untuk upload. dan juga mengambil nama dari picked image
            imageName = MultipartBody.Part.createFormData("image", path)

            // mempilkan image yang akan diupload dengan glide ke imgUpload.
            Glide.with(this).load(path).into(binding.imgAddproject)

        }
    }

    private fun addProject() {
        val inputName = binding.etCompaddprojectfrgName.text.toString()
        val inputDesc = binding.etCompaddprojectfrgDesc.text.toString()
        val inputDeadline = binding.etCompaddprojectfrgDeadline.text.toString()

        if (inputName.isEmpty()) {
            binding.etCompaddprojectfrgName.error = CompanyRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        if (inputDesc.isEmpty()) {
            binding.etCompaddprojectfrgDesc.error = CompanyRegisterScreenFragment.FIELD_IS_NOT_VALID
            return
        }

        if (inputDeadline.isEmpty()) {
            binding.etCompaddprojectfrgDeadline.error = CompanyRegisterScreenFragment.FIELD_REQUIRED
            return
        }

        addProjectService(inputName, inputDesc, inputDeadline)

        dialog.dialogCancel(context, "Add project successful") {
            fragmentManager?.popBackStack()
        }
    }

    private fun addProjectService(iName: String, iDesc: String, iDeadline: String) {
        val name = iName.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc= iDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        val deadline = iDeadline.toRequestBody("text/plain".toMediaTypeOrNull())
        val cpID = goHipePreferences.getCompanyPreference().compID.toString()
        val compID = cpID.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.addProject(compID, name, desc, deadline, imageName).enqueue(object : retrofit2.Callback<GeneralResponse> {
                        override fun onResponse(
                            call: Call<GeneralResponse>,
                            response: Response<GeneralResponse>
                        ) {
                            Toast.makeText(context,response.body()?.message,Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                            Toast.makeText(context,"Connection error", Toast.LENGTH_SHORT).show()
                            Log.d("ON FAILURE",t.toString())
                        }
                    })
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun retrieveDate(view: View) {
        val date =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(binding.etCompaddprojectfrgDeadline)
            }

        binding.etCompaddprojectfrgDeadline.setOnFocusChangeListener { _, _ ->
            DatePickerDialog(view.context, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLabel(edittext: TextInputEditText) {
        val myFormat = "YYYY-MM-DD" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        edittext.setText(sdf.format(myCalendar.time))
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Add project"
    }
}