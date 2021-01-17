package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

class EngineerSearchScreenFragment(private val co: CoordinatorLayout, private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val PROJECT_DATA = "project_data"
        private val listFilter = arrayOf("Default", "Name", "Deadline")
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
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        viewModel = ViewModelProvider(this).get(EngineerSearchProjectViewModel::class.java)
        viewModel.setSearchService(service)
        searchProject = SearchProject()
        searchProject.searchByUsername(binding.searchView)

        chipViewInit()

        val checkedID = binding.cgFilter.checkedChipId
        if (checkedID == -1) {
            binding.cgFilter.check(0)
            viewModel.getAllProjectByCompanyID(0)
        }

        viewListener(view)
        showRecyclerList()
        subscribeLiveData()
    }

    private fun viewListener(view: View) {
        binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
            val chip: Chip = view.findViewById(checkedId)
            val id = chip.id

            viewModel.getAllProjectByCompanyID(id)
        }

        searchViewListener(view)
    }

    private fun searchViewListener(view: View) {
        var checkedID = binding.cgFilter.checkedChipId

        searchProject.setOnQueryListener(object : SearchProject.OnQueryTextListener {
            override fun onQueryChangeListener(query: String) {
                if (query.length >= 3) {
                    binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
                        val chip: Chip = view.findViewById(checkedId)
                        val id = chip.id
                        checkedID = id

                        viewModel.getProjectByQuery(query,id)
                    }

                    viewModel.getProjectByQuery(query,checkedID)
                }
            }

            override fun onQuerySubmitListener(query: String) {
                binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
                    val chip: Chip = view.findViewById(checkedId)
                    val id = chip.id
                    checkedID = id

                    viewModel.getProjectByQuery(query,id)
                }

                viewModel.getProjectByQuery(query,checkedID)
            }

            override fun onCloseListener() {
                binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
                    val chip: Chip = view.findViewById(checkedId)
                    val id = chip.id
                    checkedID = id

                    viewModel.getAllProjectByCompanyID(id)
                }

                viewModel.getAllProjectByCompanyID(checkedID)
            }
        })
    }

    private fun subscribeLiveData() {
        viewModel.projectAction.observe(this, {
            if (it) {
                binding.pgHomeengfrg.visibility = View.VISIBLE
                binding.rvSearchListProject.visibility = View.GONE
                binding.imgDataNotFound.visibility = View.GONE
                binding.tvNotFound.visibility = View.GONE
            } else {
                binding.pgHomeengfrg.visibility = View.GONE
            }
        })

        viewModel.listProject.observe(this, {

            (binding.rvSearchListProject.adapter as ListSearchProjectAdapter).setData(it!!)
        })

        viewModel.isFailedStatus.observe(this, {
            when (it) {
                "400" -> {
                    binding.pgHomeengfrg.visibility = View.GONE
                    binding.rvSearchListProject.visibility = View.GONE
                    binding.imgDataNotFound.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.VISIBLE
                }
                "ADD LIST PROJECT" -> {
                    binding.rvSearchListProject.visibility = View.VISIBLE
                }
            }
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

    private fun chipViewInit() {
        for (element in listFilter) {
            val chip = layoutInflater.inflate(R.layout.item_chip_group_choice, binding.cgFilter, false) as Chip

            chip.id = listFilter.indexOf(element)
            chip.text = element
            binding.cgFilter.addView(chip)
        }
    }

    private fun setToolbar() {
        if (state) {
            co.visibility = View.GONE
            rl.visibility = View.GONE
        } else {
            co.visibility = View.GONE
            rl.visibility = View.VISIBLE
        }
    }
}