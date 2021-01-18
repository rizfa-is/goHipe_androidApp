package com.istekno.gohipeandroidapp.maincontent.company

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchEngineerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanySearchScreenBinding
import com.istekno.gohipeandroidapp.helpers.SearchProject
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.viewmodels.CompanySearchEngineerViewModel

class CompanySearchScreenFragment(private val co: CoordinatorLayout, private val rl: RelativeLayout,
                                  private val state: Boolean) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
        const val HOME_DATA = "home_data"
        private val listFilter = arrayOf("Default", "Name", "Ability", "Location", "Job Type")
    }

    private lateinit var binding: FragmentCompanySearchScreenBinding
    private lateinit var viewModel: CompanySearchEngineerViewModel
    private lateinit var service: GoHipeApiService
    private lateinit var searchProject: SearchProject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_search_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        viewModel = ViewModelProvider(this).get(CompanySearchEngineerViewModel::class.java)
        viewModel.setSearchService(service)
        searchProject = SearchProject()
        searchProject.searchByUsername(binding.searchView)

        chipViewInit()

        val checkedID = binding.cgFilter.checkedChipId
        if (checkedID == -1) {
            binding.cgFilter.check(0)
            viewModel.getAllEngineer("0")
        }

        viewListener(view)
        showRecyclerList()
        subscribeLiveData()
    }

    private fun viewListener(view: View) {
        binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
            val chip: Chip = view.findViewById(checkedId)
            val id = chip.id

            viewModel.getAllEngineer("$id")
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

                        viewModel.getEngineerByQuery(query,"$id")
                    }

                    viewModel.getEngineerByQuery(query,"$checkedID")
                }
            }

            override fun onQuerySubmitListener(query: String) {
                binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
                    val chip: Chip = view.findViewById(checkedId)
                    val id = chip.id
                    checkedID = id

                    viewModel.getEngineerByQuery(query,"$id")
                }

                viewModel.getEngineerByQuery(query,"$checkedID")
            }

            override fun onCloseListener() {
                binding.cgFilter.setOnCheckedChangeListener { _, checkedId ->
                    val chip: Chip = view.findViewById(checkedId)
                    val id = chip.id
                    checkedID = id

                    viewModel.getAllEngineer("$id")
                }

                viewModel.getAllEngineer("$checkedID")
            }
        })
    }

    private fun subscribeLiveData() {
        viewModel.engAction.observe(this, {
            if (it) {
                binding.pgHomeengfrg.visibility = View.VISIBLE
                binding.rvSearchListEngineer.visibility = View.GONE
                binding.imgDataNotFound.visibility = View.GONE
                binding.tvNotFound.visibility = View.GONE
            } else {
                binding.pgHomeengfrg.visibility = View.GONE
            }
        })

        viewModel.getAllData().observe(this, {
            (binding.rvSearchListEngineer.adapter as ListSearchEngineerAdapter).setDataEngineer(it)
        })

        viewModel.getListAbility().observe(this, {
            (binding.rvSearchListEngineer.adapter as ListSearchEngineerAdapter).setDataAbility(it)
        })

        viewModel.isFailedStatus.observe(this, {
            when (it) {
                "400" -> {
                    binding.pgHomeengfrg.visibility = View.GONE
                    binding.rvSearchListEngineer.visibility = View.GONE
                    binding.imgDataNotFound.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.VISIBLE
                }
                "ADD LIST PROJECT" -> {
                    binding.rvSearchListEngineer.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun chipViewInit() {
        for (element in listFilter) {
            val chip = layoutInflater.inflate(R.layout.item_chip_group_choice, binding.cgFilter, false) as Chip

            chip.id = listFilter.indexOf(element)
            chip.text = element
            binding.cgFilter.addView(chip)
        }
    }

    private fun showRecyclerList() {
        binding.rvSearchListEngineer.apply {
            val rvAdapter = ListSearchEngineerAdapter()
            rvAdapter.notifyDataSetChanged()

            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvAdapter.setOnItemClickCallback(object : ListSearchEngineerAdapter.OnItemClickCallback {
                override fun onItemClicked(engineerModelResponse: EngineerModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HOME_AUTH_KEY, 1)
                    sendIntent.putExtra(HOME_DATA, engineerModelResponse)
                    startActivity(sendIntent)
                }
            })
            adapter = rvAdapter
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