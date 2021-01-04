package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListExperienceRecycleViewAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerExperienceBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.*
import kotlinx.coroutines.*

class EngineerExperienceFragment : Fragment() {

    companion object {
        const val HOME_DATA = "home_data"
    }

    private lateinit var binding: FragmentEngineerExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_experience, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)

        val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HOME_DATA)
        showRecycleList(view)
        getExperience(data!!)
    }

    private fun getExperience(data: EngineerModelResponse) {
        coroutineScope.launch {
            val id = data.enID
            val listPortfolio = mutableListOf<ExperienceModel>()

            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                for (i in 0 until result.database!!.size) {
                    result.database[i].enExperienceList?.map {
                        listPortfolio.add(ExperienceModel(it.exID, it.enID, it.exRole, it.exCompany, it.exDesc, it.exStartDate, it.exEndDate))
                    }
                }

                listPortfolio.removeAll { it.enID != id }
                activity?.runOnUiThread {
                    (binding.rvExperifrg.adapter as ListExperienceRecycleViewAdapter).setData(listPortfolio)
                }
            }
        }
    }

    private fun showRecycleList(view: View) {
        binding.rvExperifrg.apply {
            val rvAdapter = ListExperienceRecycleViewAdapter()
            layoutManager = LinearLayoutManager(view.context)
            adapter = rvAdapter
        }
    }
}