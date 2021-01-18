package com.istekno.gohipeandroidapp.maincontent.company

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyPreHiringProjectBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GetAllHire
import com.istekno.gohipeandroidapp.retrofit.GetAllProject
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyPreHiringProjectFragment : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val PROJECT_DATA = "project_data"
        const val BTN_EDIT_AUTH_KEY = "btn_edit_auth_key"
    }

    private lateinit var binding: FragmentCompanyPreHiringProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private lateinit var mutableProject: MutableList<ProjectModelResponse>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_pre_hiring_project, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        viewListener(view)
        showRecyclerList(view)
        getAllProjectByCompanyID()
    }

    private fun viewListener(view: View) {
        binding.fabAddproject.setOnClickListener {
            val sendIntent = Intent(view.context, SettingScreenActivity::class.java)
            sendIntent.putExtra(PROJECT_AUTH_KEY, 1)
            startActivity(sendIntent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            binding.imageView.visibility = View.GONE
            binding.tvAllInbox.visibility = View.GONE
            getAllProjectByCompanyID()
        }
    }

    private fun getAllProjectByCompanyID() {
        val cpID = goHipePreferences.getCompanyPreference().compID
        val listPjIdHire = mutableListOf<Long>()

        binding.fabAddproject.visibility = View.GONE
        binding.rvCompanyProject.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
        binding.pgComprojectfrg.visibility = View.VISIBLE

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectCompany()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            val result2 = withContext(Dispatchers.IO) {
                try {
                    service.getAllHire()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result2 is GetAllHire) {
                result2.database?.map {
                    if (it.cpID == cpID) listPjIdHire.add(it.pjID)
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }

                mutableProject = list!!.toMutableList()
                mutableProject.removeAll { it.cpID != cpID }

                if (mutableProject.isEmpty()) {
                    binding.imageView.visibility = View.VISIBLE
                    binding.tvAllInbox.visibility = View.VISIBLE
                } else {
                    for (i in 0 until listPjIdHire.size) {
                        mutableProject.removeIf { it.pjID == listPjIdHire[i] }
                    }

                    (binding.rvCompanyProject.adapter as ListSearchProjectAdapter).setData(mutableProject)
                }

                binding.fabAddproject.visibility = View.VISIBLE
                binding.rvCompanyProject.visibility = View.VISIBLE
                binding.pgComprojectfrg.visibility = View.GONE
            }
        }
    }

    private fun deleteProject(id: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteProject(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showRecyclerList(view: View) {
        binding.rvCompanyProject.apply {
            val rvAdapter = ListSearchProjectAdapter(1)
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(context)
            rvAdapter.onItemClickCallbak(object : ListSearchProjectAdapter.OnItemClickCallback {
                override fun onItemClicked(projectModelResponse: ProjectModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(PROJECT_AUTH_KEY, 1)
                    sendIntent.putExtra(PROJECT_DATA, projectModelResponse)
                    sendIntent.putExtra(BTN_EDIT_AUTH_KEY, 0)
                    startActivity(sendIntent)
                }

                override fun onDeleteClicked(projectModelResponse: ProjectModelResponse) {
                    dialog.dialog(view.context, "Are you sure to delete ${projectModelResponse.pjName}") {
                        deleteProject(projectModelResponse.pjID!!)
                    }
                }
            })
            adapter = rvAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}