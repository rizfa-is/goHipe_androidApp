package com.istekno.gohipeandroidapp.fragments.company

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
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyAddHireScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GetAllProject
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyAddHireScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    private val listDropdownJobtype = listOf("Buku Tani", "Aplikasi Anak Rantau", "Mau Makan Murah", "Travelion")

    private lateinit var binding: FragmentCompanyAddHireScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

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


        getAllProjectByCompanyID(view)
        binding.btnComhirenowfrgUpdate.setOnClickListener {
            val project = binding.etComhirenowfrgProject.text.toString()
            Toast.makeText(view.context, "Project: $project", Toast.LENGTH_SHORT).show()
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
                setDropdownMenuAdapter(view, listProject)
            }
        }
    }

    private fun setDropdownMenuAdapter(view: View, list: MutableList<String>) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, list)
        (binding.itComhirenowfrgProject.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = ""
    }
}