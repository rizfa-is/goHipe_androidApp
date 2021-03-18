package com.istekno.gohipeandroidapp.ui.company.project

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAddProjectScreenBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.ui.company.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CompanyAddProjectScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val PROJECT_ADD_AUTH_KEY = "project_add_auth_key"

        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var binding: FragmentCompanyAddProjectScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var imageName: MultipartBody.Part
    private var pathImage = ""

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

        binding.btnCompaddprojectfrgAdd.setOnClickListener {
            addProject(view)
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
            binding.imgAddproject.setImageURI(data?.data)
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

        if (pathImage != "") {
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