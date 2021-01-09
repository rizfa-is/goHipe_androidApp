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
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerExperienceFragment : Fragment() {

    companion object {
        const val HOME_DATA = "home_data"
    }

    private lateinit var binding: FragmentEngineerExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_experience, container, false)
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
            getExperience(data.enID!!)
        } else {
            getExperience(dataShared.engID!!)
        }
    }

    private fun getExperience(enID: Long) {
        coroutineScope.launch {
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

                listPortfolio.removeAll { it.enID != enID }
                activity?.runOnUiThread {
                    (binding.rvExperifrg.adapter as ListExperienceRecycleViewAdapter).setData(listPortfolio)
                }
            }
        }
    }

    private fun showRecycleList(view: View) {
        binding.rvExperifrg.apply {
            val rvAdapter = ListExperienceRecycleViewAdapter(1)
            layoutManager = LinearLayoutManager(view.context)
            adapter = rvAdapter
        }
    }
}