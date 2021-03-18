package com.istekno.gohipeandroidapp.ui.engineer.profile.edit.profile

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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.engineer.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileAccountBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EngineerEditProfileAccountFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val FIELD_DIGITS_ONLY = "Number only"
        const val FIELD_IS_NOT_VALID = "Email format is not valid"

        const val ACC_UPDATE_AUTH_KEY = "acc_update_auth_key"
        const val EDIT_PROFILE_AUTH_KEY2 = "edit_profile_auth_key2"
        const val EMPTY_DATA_AUTH_KEY = "empty_data_auth_key"
        const val imageLink = "http://107.22.89.131:7000/image/"

        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
        private val listDropdownJobType = listOf("Freelance", "Fulltime")
    }

    private lateinit var binding: FragmentEngineerEditProfileAccountBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var imageName: MultipartBody.Part
    private lateinit var engineer: EngineerModelResponse
    private var pathImage = ""

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
        goHipePreferences = GoHipePreferences(view.context)

        setText(view)
        viewListener(view)
    }

    private fun setText(view: View) {
        val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(
            EDIT_PROFILE_AUTH_KEY2
        )
        val dataEmpty = activity?.intent?.getParcelableExtra<EngineerModelResponse>(
            EMPTY_DATA_AUTH_KEY
        )

        if (dataEmpty != null) {
            engineer = dataEmpty
        } else if (data != null) {
            engineer = data
        }

        val fullname = binding.etEngeditaccountfrgFullname
        val email = binding.etEngeditaccountfrgEmail
        val jobTitle = binding.etEngeditaccountfrgJobtitle
        val jobType = binding.etEngeditaccountfrgJobtype
        val phone = binding.etEngeditaccountfrgPhone
        val location = binding.etEngeditaccountfrgLocation
        val desc = binding.etEngeditaccountfrgDescription
        val ig = binding.etEngeditaccountfrgInstagram
        val github = binding.etEngeditaccountfrgGithub
        val gitlab = binding.etEngeditaccountfrgGitlab

        fullname.setText(engineer.enName)
        email.setText(engineer.enEmail)
        jobTitle.setText(engineer.enJobTitle)
        jobType.setText(engineer.enJobType)
        phone.setText(engineer.enPhone)
        location.setText(engineer.enLocation)
        desc.setText(engineer.enDesc)
        ig.setText(engineer.enIG)
        github.setText(engineer.enGithub)
        gitlab.setText(engineer.enGitlab)

        if (!engineer.enAvatar.isNullOrEmpty()) {
            Glide.with(view.context).load(imageLink + engineer.enAvatar).placeholder(R.drawable.ic_avatar_en).into(binding.shapeableImageView2)
        }

        setDropdownMenuAdapter(view)
    }

    private fun viewListener(view: View) {
        binding.btnEngregisterfrgUpdate.setOnClickListener {
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

    private fun updateEngineer(type: Int, name: String, email: String, phone: String, jTitle: String,
                               jType: String, location: String, desc: String, ig: String, github: String, gitlab: String) {
        val id = goHipePreferences.getEngineerPreference().acID
        val eName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val eEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val ePhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val eJTitle = jTitle.toRequestBody("text/plain".toMediaTypeOrNull())
        val eJType = jType.toRequestBody("text/plain".toMediaTypeOrNull())
        val eLoc = location.toRequestBody("text/plain".toMediaTypeOrNull())
        val eDesc = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val eIG = ig.toRequestBody("text/plain".toMediaTypeOrNull())
        val eGithub = github.toRequestBody("text/plain".toMediaTypeOrNull())
        val eGitlab = gitlab.toRequestBody("text/plain".toMediaTypeOrNull())

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service.updateEngineer(id!!, eName, eEmail, ePhone, eJTitle, eJType, eLoc, eDesc, eIG, eGithub, eGitlab, imageName)
                    } else {
                        service.updateEngineer(id!!, eName, eEmail, ePhone, eJTitle, eJType, eLoc, eDesc, eIG, eGithub, eGitlab)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun update(view: View) {
        val inputFullname = binding.etEngeditaccountfrgFullname.text.toString().trim()
        val inputEmail = binding.etEngeditaccountfrgEmail.text.toString().trim()
        val inputPhone = binding.etEngeditaccountfrgPhone.text.toString().trim()
        val inputJobTitle = binding.etEngeditaccountfrgJobtitle.text.toString().trim()
        val inputJobType = binding.etEngeditaccountfrgJobtype.text.toString().trim()
        val inputLocation = binding.etEngeditaccountfrgLocation.text.toString().trim()
        val inputDesc = binding.etEngeditaccountfrgDescription.text.toString().trim()
        val inputIg = binding.etEngeditaccountfrgInstagram.text.toString().trim()
        val inputGithub = binding.etEngeditaccountfrgGithub.text.toString().trim()
        val inputGitlab = binding.etEngeditaccountfrgGitlab.text.toString().trim()

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

        if (inputPhone.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (!TextUtils.isDigitsOnly(inputPhone)) {
            showToast(view, FIELD_DIGITS_ONLY)
            return
        }

        if (inputJobTitle.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputJobType.isEmpty()) {
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

        if (inputIg.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputGithub.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (inputGitlab.isEmpty()) {
            showToast(view, FIELD_REQUIRED)
            return
        }

        if (pathImage != "") {

            updateEngineer(1, inputFullname, inputEmail, inputPhone, inputJobTitle, inputJobType, inputLocation, inputDesc, inputIg, inputGithub, inputGitlab)

            dialog.dialogCancel(context, "Success update account!") {
                val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                sendIntent.putExtra(ACC_UPDATE_AUTH_KEY, 0)
                startActivity(sendIntent)
            }

        } else {

            updateEngineer(0, inputFullname, inputEmail, inputPhone, inputJobTitle, inputJobType, inputLocation, inputDesc, inputIg, inputGithub, inputGitlab)

            dialog.dialogCancel(context, "Success update account!") {
                val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                sendIntent.putExtra(ACC_UPDATE_AUTH_KEY, 0)
                startActivity(sendIntent)
            }
        }
    }

    private fun setDropdownMenuAdapter(view: View) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, listDropdownJobType)
        (binding.itEngeditaccountfrgJobtype.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}