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
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileExperienceBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.ExperienceModel
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerEditProfileExperienceFragment : Fragment() {

    private lateinit var binding: FragmentEngineerEditProfileExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile_experience, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        val dataShared = goHipePreferences.getEngineerPreference()

        showRecycleList(view)
        getExperience(dataShared.engID!!)
    }

    private fun getExperience(enID: Long) {
        coroutineScope.launch {
            val listExperiences = mutableListOf<ExperienceModel>()

            binding.fabEditexperifrg.visibility = View.GONE
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
                        listExperiences.add(ExperienceModel(it.exID, it.enID, it.exRole, it.exCompany, it.exDesc, it.exStartDate, it.exEndDate))
                    }
                }

                listExperiences.removeAll { it.enID != enID }
                (binding.rvEditexperifrg.adapter as ListExperienceRecycleViewAdapter).setData(listExperiences)
                binding.fabEditexperifrg.visibility = View.VISIBLE
                binding.pgEditexperifrg.visibility = View.GONE
            }
        }
    }

    private fun addExperience(abName: String) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().engID

            withContext(Dispatchers.IO) {
                try {
                    service.addABility(id!!.toLong(), abName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateExperience(abID: Long, abName: String) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().engID

            withContext(Dispatchers.IO) {
                try {
                    service.updateABility(abID, id!!.toLong(), abName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteExperience(abID: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteAbility(abID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun showRecycleList(view: View) {
        binding.rvEditexperifrg.apply {
            val rvAdapter = ListExperienceRecycleViewAdapter(0)
            layoutManager = LinearLayoutManager(view.context)
            adapter = rvAdapter
        }
    }
}