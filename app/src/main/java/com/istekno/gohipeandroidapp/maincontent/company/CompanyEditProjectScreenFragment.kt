package com.istekno.gohipeandroidapp.maincontent.company

import android.app.Activity
import android.app.DatePickerDialog
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
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyEditProjectScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CompanyEditProjectScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"

        const val EDIT_PROJECT_AUTH_KEY = "edit_project_auth_key"
        const val imageLink = "http://107.22.89.131:7000/image/"

        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var binding: FragmentCompanyEditProjectScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var imageName: MultipartBody.Part
    private val myCalendar: Calendar = Calendar.getInstance()
    private var pathImage = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_edit_project_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        val data = activity?.intent?.getParcelableExtra<ProjectModelResponse>(EDIT_PROJECT_AUTH_KEY)

        setText(view, data!!)
        viewListener(view, data)
    }

    private fun viewListener(view: View, data: ProjectModelResponse) {
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
        binding.btnCompeditprojectfrgAdd.setOnClickListener {
            update(data.pjID!!, view, data)
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
            binding.imgEditproject.setImageURI(data?.data)
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

    private fun update(pjID: Long, view: View, data: ProjectModelResponse) {
        val project = binding.etCompeditprojectfrgName.text.toString()
        val desc = binding.etCompeditprojectfrgDesc.text.toString()
        val deadline = binding.etCompeditprojectfrgDeadline.text.toString()

        if (project.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (desc.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (deadline.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (pathImage != "") {

            updateProject(1, view, pjID, project, desc, deadline)
        } else {

            updateProject(0, view, pjID, project, desc, deadline)
        }
    }

    private fun updateProject(type: Int, view: View, pjID: Long, pjName: String, pjDesc: String, pjDeadline: String) {
        coroutineScope.launch {
            val project = pjName.toRequestBody("text/plain".toMediaTypeOrNull())
            val desc = pjDesc.toRequestBody("text/plain".toMediaTypeOrNull())
            val deadline = pjDeadline.toRequestBody("text/plain".toMediaTypeOrNull())

            withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service.updateProject(pjID, project, desc, deadline, imageName)
                    } else {
                        service.updateProject(pjID, project, desc, deadline)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            dialog.dialogCancel(view.context, "Update hire successful!") {
                activity?.onBackPressed()
            }
        }
    }

    private fun setText(view: View, data: ProjectModelResponse) {
        val project = binding.etCompeditprojectfrgName
        val desc = binding.etCompeditprojectfrgDesc
        val date = binding.etCompeditprojectfrgDeadline
        val pjDeadline = data.pjDeadline!!.split('T')[0]
        val year = pjDeadline.split('-')[0].toInt()
        val month = pjDeadline.split('-')[1].toInt()
        val day = pjDeadline.split('-')[2].toInt()

        date.setText(pjDeadline)
        project.setText(data.pjName)
        desc.setText(data.pjDesc)

        if (!data.pjImage.isNullOrEmpty()) {
            Glide.with(view.context).load(imageLink + data.pjImage).into(binding.imgEditproject)
        }

        retrieveDate(view, binding.imgSelectdate, binding.etCompeditprojectfrgDeadline, year, month, day)
    }

    private fun retrieveDate(view: View, imgDate: ImageView, editText: TextInputEditText, yearN: Int, month: Int, day: Int) {
        val date: DatePickerDialog.OnDateSetListener

        date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(editText)
        }

        imgDate.setOnClickListener {
            val dateNew = editText.text.toString().split('-')
            val yearNew = dateNew[0].toInt()
            val monthNew = dateNew[1].toInt()
            val dayNew = dateNew[2].toInt()

            if (yearNew == yearN && monthNew == month && dayNew == day) {
                DatePickerDialog(view.context, date,
                        yearN, month - 1,
                        day
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
        toolbar.title = "Edit project"
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}