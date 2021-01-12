package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchEngineerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanySearchScreenBinding
import com.istekno.gohipeandroidapp.helpers.SearchProject
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.*
import com.istekno.gohipeandroidapp.viewmodels.CompanySearchEngineerViewModel

class CompanySearchScreenFragment(private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
        const val HOME_DATA = "home_data"
    }

    private lateinit var binding: FragmentCompanySearchScreenBinding
    private lateinit var viewModel: CompanySearchEngineerViewModel
    private lateinit var service: GoHipeApiService
    private lateinit var searchProject: SearchProject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(co)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_search_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CompanySearchEngineerViewModel::class.java)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        searchProject = SearchProject()

        viewModel.setSearchService(service)
        viewModel.getAllEngineer()

        searchProject.searchByUsername(binding.searchView)
        searchProject.setOnQueryListener(object : SearchProject.OnQueryTextListener {
            override fun onQueryChangeListener(query: String) {
                viewModel.getEngineerByQuery(query)
            }

            override fun onQuerySubmitListener(query: String) {
                viewModel.getEngineerByQuery(query)
            }
        })

        showRecyclerList()
        viewListener()
        subscribeLiveData()
    }

    private fun viewListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewModel.getAllEngineer()
        }
    }

    private fun subscribeLiveData() {
        viewModel.engAction.observe(this, {
            if (it) {
                binding.pgHomeengfrg.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
                binding.rvSearchListEngineer.visibility = View.GONE
            } else {
                binding.pgHomeengfrg.visibility = View.GONE
                binding.rvSearchListEngineer.visibility = View.VISIBLE
            }
        })

        viewModel.getAllData().observe(this, { it ->
            (binding.rvSearchListEngineer.adapter as ListSearchEngineerAdapter).setDataEngineer(it)
        })

        viewModel.getListAbility().observe(this, { it ->
            (binding.rvSearchListEngineer.adapter as ListSearchEngineerAdapter).setDataAbility(it)
        })
    }

    private fun showRecyclerList() {
        binding.rvSearchListEngineer.apply {
            val rvAdapter = ListSearchEngineerAdapter()
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

    private fun setToolbar(co: CoordinatorLayout) {
        co.visibility = View.GONE
    }
}