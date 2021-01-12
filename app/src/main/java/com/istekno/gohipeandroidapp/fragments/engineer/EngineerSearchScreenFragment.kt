package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerSearchScreenBinding
import com.istekno.gohipeandroidapp.helpers.SearchProject
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import com.istekno.gohipeandroidapp.viewmodels.EngineerSearchProjectViewModel

class EngineerSearchScreenFragment(private val toolbar: MaterialToolbar, private val co: CoordinatorLayout,
                                   private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val PROJECT_DATA = "project_data"
        private val listFilter = listOf("All", "Name", "Deadline")
    }

    private lateinit var binding: FragmentEngineerSearchScreenBinding
    private lateinit var viewModel: EngineerSearchProjectViewModel
    private lateinit var service: GoHipeApiService
    private lateinit var searchProject: SearchProject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_search_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EngineerSearchProjectViewModel::class.java)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        searchProject = SearchProject()

        viewModel.setSearchService(service)
        viewModel.getAllProjectByCompanyID()

        searchProject.searchByUsername(binding.searchView)
        searchProject.setOnQueryListener(object : SearchProject.OnQueryTextListener {
            override fun onQueryChangeListener(query: String) {
                viewModel.getProjectByQuery(query)
            }

            override fun onQuerySubmitListener(query: String) {
                viewModel.getProjectByQuery(query)
            }
        })

        showRecyclerList()
        viewListener()
        subscribeLiveData()
    }

    private fun viewListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewModel.getAllProjectByCompanyID()
        }
    }

    private fun subscribeLiveData() {
        viewModel.projectAction.observe(this, {
            if (it) {
                binding.pgHomeengfrg.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
                binding.rvSearchListProject.visibility = View.GONE
            } else {
                binding.pgHomeengfrg.visibility = View.GONE
                binding.rvSearchListProject.visibility = View.VISIBLE
            }
        })

        viewModel.getListProject().observe(this, { it ->
            (binding.rvSearchListProject.adapter as ListSearchProjectAdapter).setData(it!!)
        })
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

    private fun chipViewInit(list: List<String>, view: View) {
        binding.cgFilter.isSingleSelection = true
        for (element in list) {
            val chip = Chip(view.context)

            chip.chipStartPadding = 100F
            chip.chipCornerRadius = 20F
            chip.text = element
            binding.cgFilter.addView(chip)
        }

        binding.cgFilter.setOnCheckedChangeListener { group, checkedId ->
            val titleOrNull = binding.cgFilter.findViewById<Chip>(checkedId)?.text
            Toast.makeText(group.context, titleOrNull ?: "No Choice", Toast.LENGTH_LONG).show()
        }
    }

    private fun setToolbar() {
        if (state) {
            co.visibility = View.GONE
            rl.visibility = View.GONE
        } else {
            co.visibility = View.GONE
            rl.visibility = View.VISIBLE
            toolbar.title = "Search"
        }
    }
}