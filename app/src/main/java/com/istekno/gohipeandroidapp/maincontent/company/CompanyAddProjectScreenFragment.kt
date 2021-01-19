package com.istekno.gohipeandroidapp.maincontent.company

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAddProjectScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
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
import java.text.SimpleDateFormat
import java.util.*


class CompanyAddProjectScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val PROJECT_ADD_AUTH_KEY = "project_add_auth_key"
        const val REQUEST_CODE = 1000
    }

    private lateinit var binding: FragmentCompanyAddProjectScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var imageName: MultipartBody.Part
    private var dataImage = ""

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

        retrieveDate(view, binding.imgSelectdate, binding.etCompaddprojectfrgDeadline)
        viewListener(view)
    }

    private fun viewListener(view: View) {
        binding.editImageButton.setOnClickListener {

            if (EasyPermissions.hasPermissions(view.context,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access image gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.btnCompaddprojectfrgAdd.setOnClickListener {
            addProject(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val dataResponse = data?.data?.path?.replace("/raw/".toRegex(), "")
            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), File(dataResponse!!))

            dataImage = dataResponse
            imageName = MultipartBody.Part.createFormData("image", File(dataResponse).name, requestBody)
            Glide.with(this).load(dataResponse).into(binding.imgAddproject)
        }
    }

    private fun addProject(view: View) {
        val inputName = binding.etCompaddprojectfrgName.text.toString()
        val inputDesc = binding.etCompaddprojectfrgDesc.text.toString()
        val inputDeadline = binding.etCompaddprojectfrgDeadline.text.toString()

        if (inputName.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputDesc.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputDeadline.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (dataImage != "") {
            addProjectService(inputName, inputDesc, inputDeadline)

            dialog.dialogCancel(context, "Add project successful") {
                val sendIntent = Intent(view.context, CompanyMainContentActivity::class.java)
                sendIntent.putExtra(PROJECT_ADD_AUTH_KEY, 1)
                startActivity(sendIntent)
            }
        } else {
            Toast.makeText(view.context, "Please select project image!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProjectService(iName: String, iDesc: String, iDeadline: String) {
        val cpID = goHipePreferences.getCompanyPreference().compID.toString()
        val name = iName.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc= iDesc.toRequestBody("text/plain".toMediaTypeOrNull())
        val deadline = iDeadline.toRequestBody("text/plain".toMediaTypeOrNull())
        val compID = cpID.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.addProject(compID, name, desc, deadline, imageName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun retrieveDate(view: View, imgDate: ImageView, editText: TextInputEditText) {
        val date =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(binding.etCompaddprojectfrgDeadline)
            }

        imgDate.setOnClickListener {
            val et = editText.text.toString()
            val initDate: List<Int>
            var yearNew = 0
            var monthNew = 0
            var dayNew = 0

            if (et.isNotEmpty()) {
                initDate = editText.text.toString().split('-').map { it.toInt() }
                yearNew = initDate[0]
                monthNew = initDate[1]
                dayNew = initDate[2]
            }

            if (yearNew == 0 && monthNew == 0 && dayNew == 0) {
                DatePickerDialog(view.context, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            } else {
                DatePickerDialog(view.context, date,
                        yearNew, monthNew - 1,
                        dayNew
                ).show()
            }
        }
    }

    private fun updateLabel(editText: TextInputEditText) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        editText.setText(dateFormat.format(myCalendar.time))
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = "Add project"
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}