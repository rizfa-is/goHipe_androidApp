package com.istekno.gohipeandroidapp.ui.engineer.profile.edit.portfolio

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.adapter.ListPortfolioRecycleViewAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAddupdatePortfolioDialogBinding
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfilePortfolioBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.PortfolioModel
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EngineerEditProfilePortfolioFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"

        private const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
        const val imageLink = "http://107.22.89.131:7000/image/"

        private val list = mutableListOf("Mobile App", "Web App")
    }

    private lateinit var binding: FragmentEngineerEditProfilePortfolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var imageName: MultipartBody.Part
    private var pathImage = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile_portfolio, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        val dataShared = goHipePreferences.getEngineerPreference()

        showRecycleList(view, dataShared.engID!!)
        getPortfolio(dataShared.engID!!)
        viewListener(view, dataShared.engID!!)
    }

    private fun viewListener(view: View, id: Long) {
        binding.fabEditportofrg.setOnClickListener {
            setFormDialog(view, 0, id)
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            binding.imageView.visibility = View.GONE
            binding.tvPortfolio.visibility = View.GONE
            getPortfolio(id)
        }
    }

    private fun getPortfolio(enID: Long) {
        coroutineScope.launch {
            val listPortfolio = mutableListOf<PortfolioModel>()

            binding.fabEditportofrg.visibility = View.GONE
            binding.rvEditportofrg.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false
            binding.pgEditportofrg.visibility = View.VISIBLE
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                for (i in 0 until result.database!!.size) {
                    result.database[i].enPortfolioList?.map {
                        listPortfolio.add(PortfolioModel(it.prID, it.enID, it.prApplication, it.prDesc, it.prLink, it.prRepo, it.prCompany, it.prRole, it.prImg))
                    }
                }
                listPortfolio.removeAll { it.enID != enID }

                if (listPortfolio.isEmpty()) {
                    binding.imageView.visibility = View.VISIBLE
                    binding.tvPortfolio.visibility = View.VISIBLE
                }

                (binding.rvEditportofrg.adapter as ListPortfolioRecycleViewAdapter).setData(listPortfolio)
                binding.fabEditportofrg.visibility = View.VISIBLE
                binding.rvEditportofrg.visibility = View.VISIBLE
                binding.pgEditportofrg.visibility = View.GONE
            }
        }
    }

    private fun addPortfolio(prApp: String, prDesc: String, prLink: String, prRepo: String, prComp: String, prRole: String) {
        coroutineScope.launch {
            val enId = goHipePreferences.getEngineerPreference().engID.toString()
            val app = prApp.toRequestBody("text/plain".toMediaTypeOrNull())
            val desc= prDesc.toRequestBody("text/plain".toMediaTypeOrNull())
            val link = prLink.toRequestBody("text/plain".toMediaTypeOrNull())
            val repo = prRepo.toRequestBody("text/plain".toMediaTypeOrNull())
            val company = prComp.toRequestBody("text/plain".toMediaTypeOrNull())
            val role = prRole.toRequestBody("text/plain".toMediaTypeOrNull())
            val id = enId.toRequestBody("text/plain".toMediaTypeOrNull())

            withContext(Dispatchers.IO) {
                try {
                    service.addPortfolio(id, app, desc, link, repo, company, role, imageName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updatePortfolio(type: Int, prID: Long, prApp: String, prDesc: String, prLink: String, prRepo: String, prComp: String, prRole: String) {
        coroutineScope.launch {
            val enId = goHipePreferences.getEngineerPreference().engID.toString()
            val app = prApp.toRequestBody("text/plain".toMediaTypeOrNull())
            val desc= prDesc.toRequestBody("text/plain".toMediaTypeOrNull())
            val link = prLink.toRequestBody("text/plain".toMediaTypeOrNull())
            val repo = prRepo.toRequestBody("text/plain".toMediaTypeOrNull())
            val company = prComp.toRequestBody("text/plain".toMediaTypeOrNull())
            val role = prRole.toRequestBody("text/plain".toMediaTypeOrNull())
            val id = enId.toRequestBody("text/plain".toMediaTypeOrNull())

            withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service.updatePortfolio(prID, id, app, desc, link, repo, company, role, imageName)
                    } else {
                        service.updatePortfolio(prID, id, app, desc, link, repo, company, role)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deletePortfolio(prID: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deletePortfolio(prID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showRecycleList(view: View, id: Long) {
        binding.rvEditportofrg.apply {
            val rvAdapter = ListPortfolioRecycleViewAdapter(0)
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(view.context)
            rvAdapter.onItemClickCallbak(object : ListPortfolioRecycleViewAdapter.OnItemClickCallback{
                override fun onUpdatePressed(portfolioModel: PortfolioModel) {
                    setFormDialog(view, 1, id, portfolioModel.prID, portfolioModel.prApplication, portfolioModel.prDesc,
                            portfolioModel.prLink, portfolioModel.prRepo, portfolioModel.prCompany, portfolioModel.prRole, portfolioModel.prImg)
                }

                override fun onDeletePressed(portfolioModel: PortfolioModel) {
                    dialog.dialog(view.context, "Are you sure delete ${portfolioModel.prApplication}") {
                        dialog.dialogCancel(view.context, "${portfolioModel.prApplication} deleted!") {
                            deletePortfolio(portfolioModel.prID)
                            binding.swipeRefresh.isRefreshing = true
                            getPortfolio(id)
                        }
                    }
                }
            })
            adapter = rvAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val customView = DataBindingUtil.inflate<FragmentEngineerAddupdatePortfolioDialogBinding>(layoutInflater, R.layout.fragment_engineer_addupdate_portfolio_dialog,
            null, false)

            val imageViewDialog = customView.imgAddport
            imageViewDialog.setImageURI(data?.data)
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

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun setFormDialog(view: View, type: Int, enId: Long, id: Long = 0, prApp: String? = "Empty", prDesc: String? = "Empty",
                              prLink: String? = "Empty", prRepo: String? = "Empty", prComp: String? = "Empty", prRole: String? = "Empty", prImage: String? = "Empty") {

        val dialogBuilder = AlertDialog.Builder(view.context)
        val customView = LayoutInflater.from(view.context).inflate(R.layout.fragment_engineer_addupdate_portfolio_dialog, null)
        val header = customView.findViewById<TextView>(R.id.tv_header)
        val appName = customView.findViewById<TextInputEditText>(R.id.et_addport_appname)
        val desc = customView.findViewById<TextInputEditText>(R.id.et_addport_desc)
        val link = customView.findViewById<TextInputEditText>(R.id.et_addport_link)
        val repo = customView.findViewById<TextInputEditText>(R.id.et_addport_repo)
        val company = customView.findViewById<TextInputEditText>(R.id.et_addport_comp)
        val role = customView.findViewById<AutoCompleteTextView>(R.id.et_addport_type)
        val roleTI = customView.findViewById<TextInputLayout>(R.id.it_addport_type)
        val img = customView.findViewById<ShapeableImageView>(R.id.img_addport)

        val btnAdd = customView.findViewById<MaterialButton>(R.id.btn_addport)
        val btnAddImage = customView.findViewById<ShapeableImageView>(R.id.edit_image_button)
        val cDialog = dialogBuilder.setView(customView).create()

        if (type == 1) {
            header.text = "Update portfolio"
            btnAdd.text = "Update"
            appName.setText(prApp)
            desc.setText(prDesc)
            link.setText(prLink)
            repo.setText(prRepo)
            company.setText(prComp)
            role.setText(prRole)

            if (!prImage.isNullOrEmpty()) {
                Glide.with(view.context).load(imageLink + prImage).into(img)
            }
        }

        setDropdownMenuAdapter(view, roleTI, list)
        cDialog.show()
        btnAddImage.setOnClickListener {
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

        btnAdd.setOnClickListener {
            val inputApp = appName.text.toString()
            val inputDesc = desc.text.toString()
            val inputLink = link.text.toString()
            val inputRepo = repo.text.toString()
            val inputComp = company.text.toString()
            val inputRole = role.text.toString()

            if (inputApp.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (inputDesc.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (inputLink.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (inputRepo.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (inputComp.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (inputRole.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (type == 1) {
                if (pathImage != "") {

                    updatePortfolio(1, id, inputApp, inputDesc, inputLink, inputRepo, inputComp, inputRole)

                    dialog.dialogCancel(context, "Success update portfolio!") {
                        cDialog.cancel()
                        binding.swipeRefresh.isRefreshing = true
                        getPortfolio(enId)
                    }

                } else {

                    updatePortfolio(0, id, inputApp, inputDesc, inputLink, inputRepo, inputComp, inputRole)

                    dialog.dialogCancel(context, "Success update portfolio!") {
                        cDialog.cancel()
                        binding.swipeRefresh.isRefreshing = true
                        getPortfolio(enId)
                    }
                }
            } else {

                if (pathImage != "") {

                    addPortfolio(inputApp, inputDesc, inputLink, inputRepo, inputComp, inputRole)

                    dialog.dialogCancel(context, "Success add portfolio!") {
                        cDialog.cancel()
                        binding.swipeRefresh.isRefreshing = true
                        getPortfolio(enId)
                    }

                } else {
                    Toast.makeText(view.context, "Please select portfolio image!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun setDropdownMenuAdapter(view: View, ti: TextInputLayout, list: MutableList<String>) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, list)
        (ti.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}