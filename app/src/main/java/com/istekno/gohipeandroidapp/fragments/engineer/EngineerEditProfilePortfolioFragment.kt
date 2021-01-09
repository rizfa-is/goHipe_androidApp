package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListPortfolioRecycleViewAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfilePortfolioBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.PortfolioModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerEditProfilePortfolioFragment : Fragment() {

    private lateinit var binding: FragmentEngineerEditProfilePortfolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

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

        val dataShared = goHipePreferences.getEngineerPreference()

        showRecycleList(view)
        getPortfolio(dataShared.engID!!)
    }

    private fun getPortfolio(enID: Long) {
        coroutineScope.launch {
            val listPortfolio = mutableListOf<PortfolioModel>()

            binding.fabEditportofrg.visibility = View.GONE
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
                (binding.rvEditportofrg.adapter as ListPortfolioRecycleViewAdapter).setData(listPortfolio)
                binding.fabEditportofrg.visibility = View.VISIBLE
                binding.pgEditportofrg.visibility = View.GONE
            }
        }
    }

    private fun showRecycleList(view: View) {
        binding.rvEditportofrg.apply {
            val rvAdapter = ListPortfolioRecycleViewAdapter(0)
            layoutManager = LinearLayoutManager(view.context)
            adapter = rvAdapter
        }
    }
}