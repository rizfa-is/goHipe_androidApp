package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListPortfolioRecycleViewAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerPortfolioBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.*
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerPortfolioFragment : Fragment() {

    companion object {
        const val HOME_DATA = "home_data"
    }

    private lateinit var binding: FragmentEngineerPortfolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_portfolio, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HOME_DATA)
        val dataShared = goHipePreferences.getEngineerPreference()

        showRecycleList(view)
        if (data != null) {
            getPortfolio(data.enID!!)
        } else {
            getPortfolio(dataShared.engID!!)
        }
    }

    private fun getPortfolio(enID: Long) {
        coroutineScope.launch {
            val listPortfolio = mutableListOf<PortfolioModel>()

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
                activity?.runOnUiThread {
                    (binding.rvPortofrg.adapter as ListPortfolioRecycleViewAdapter).setData(listPortfolio)
                }
            }
        }
    }

    private fun showRecycleList(view: View) {
        binding.rvPortofrg.apply {
            val rvAdapter = ListPortfolioRecycleViewAdapter()
            layoutManager = LinearLayoutManager(view.context)
            adapter = rvAdapter
        }
    }
}