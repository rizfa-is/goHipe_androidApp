package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerSearchScreenBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyProjectScreenFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GetAllProject
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerSearchScreenFragment(private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val PROJECT_DATA = "project_data"
    }

    private lateinit var binding: FragmentEngineerSearchScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_search_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)

        showRecyclerList()
        getAllProjectByCompanyID()
    }

    private fun getAllProjectByCompanyID() {
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

                (binding.rvSearchListProject.adapter as ListSearchProjectAdapter).setData(list!!)
            }
        }
    }

    private fun showRecyclerList() {
        binding.rvSearchListProject.apply {
            val rvAdapter = ListSearchProjectAdapter(0)
            layoutManager = LinearLayoutManager(context)
            rvAdapter.onItemClickCallbak(object : ListSearchProjectAdapter.OnItemClickCallback {
                override fun onItemClicked(projectModelResponse: ProjectModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(PROJECT_AUTH_KEY, 0)
                    sendIntent.putExtra(PROJECT_DATA, projectModelResponse)
                    startActivity(sendIntent)
                }

                override fun onDeleteClicked(projectModelResponse: ProjectModelResponse) {
                    TODO()
                }
            })
            adapter = rvAdapter
        }
    }

    private fun setToolbar() {
        co.visibility = View.GONE
    }
}