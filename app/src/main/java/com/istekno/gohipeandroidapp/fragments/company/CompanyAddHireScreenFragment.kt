package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.CompanyMainContentActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAddHireScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GetAllProject
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyAddHireScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val HIRE_ADD_AUTH_KEY = "hire_add_auth_key"
        const val HIRE_DATA = "hire_data"
    }

    private lateinit var binding: FragmentCompanyAddHireScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_add_hire_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()


        val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HIRE_DATA)

        getAllProjectByCompanyID(view)
        setTextInput(data)

        binding.btnComhirenowfrgUpdate.setOnClickListener {
            val project = binding.etComhirenowfrgProject.text.toString()

            hire(view, data?.enID!!)
            Toast.makeText(view.context, "Project: $project", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTextInput(data: EngineerModelResponse?) {
        val engIDTextInput = binding.etComhirenowfrgEngid
        val engIDFormat = "(ID: ${data?.enID}) ${data?.enName}"

        engIDTextInput.setText(engIDFormat)
        engIDTextInput.setTextColor(Color.rgb(96, 96, 96))
        engIDTextInput.isEnabled = false
    }

    private fun hire(view: View, enID: Long) {
        val project = binding.etComhirenowfrgProject.text.toString()
        val msg = binding.etComhirenowfrgMessage.text.toString()
        val price = binding.etComhirenowfrgPrice.text.toString()

        if (project.isEmpty()) {
            binding.etComhirenowfrgProject.error = FIELD_REQUIRED
            return
        }

        if (msg.isEmpty()) {
            binding.etComhirenowfrgMessage.error = FIELD_REQUIRED
            return
        }

        if (price.isEmpty()) {
            binding.etComhirenowfrgPrice.error = FIELD_REQUIRED
            return
        }

        postHire(enID, project, price, msg)
        dialog.dialog(view.context, "Add project successful!") {
            val sendIntent = Intent(view.context, CompanyMainContentActivity::class.java)
            sendIntent.putExtra(HIRE_ADD_AUTH_KEY, 1)
            startActivity(sendIntent)
        }
    }

    private fun postHire(enID: Long, pjName: String, pjPrice: String, pjMessage: String) {
        var mutable: MutableList<ProjectModelResponse>
        val cpID = goHipePreferences.getCompanyPreference().compID
        val listProject = mutableListOf<String>()
        val listProjectID = mutableListOf<Long>()

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectCompany()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }
                mutable = list!!.toMutableList()
                mutable.removeAll { it.cpID != cpID }
                mutable.map {
                    listProjectID.add(it.pjID!!.toLong())
                    listProject.add(it.pjName!!)
                }
            }

            val pjID = listProjectID[listProject.indexOf(pjName)]
            Log.e("pjID", pjID.toString())

            Log.e("listProject", listProject.toString())
            Log.e("listProjectID", listProjectID.toString())
            withContext(Dispatchers.IO) {
                try {
                    service.addHire(enID, pjID, pjPrice, pjMessage)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getAllProjectByCompanyID(view: View) {
        val cpID = goHipePreferences.getCompanyPreference().compID
        var mutable: MutableList<ProjectModelResponse>
        val listProject = mutableListOf<String>()

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectCompany()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }
                mutable = list!!.toMutableList()
                mutable.removeAll { it.cpID != cpID }
                mutable.map {
                    listProject.add(it.pjName!!)
                }
                setDropdownMenuAdapter(view, binding.itComhirenowfrgProject, listProject)
            }
        }
    }

    private fun setDropdownMenuAdapter(view: View, ti: TextInputLayout, list: MutableList<String>) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, list)
        (ti.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = ""
    }
}