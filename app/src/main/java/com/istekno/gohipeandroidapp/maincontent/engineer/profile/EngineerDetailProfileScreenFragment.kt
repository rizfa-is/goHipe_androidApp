package com.istekno.gohipeandroidapp.maincontent.engineer.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.EngineerProfileViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerDetailProfileScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.*
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerDetailProfileScreenFragment : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
        const val HIRE_DATA2 = "hire_data2"
        const val HOME_DATA = "home_data"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentEngineerDetailProfileScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var mutableProject: MutableList<ProjectModelResponse>
    private var listProject = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_detail_profile_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HOME_DATA)
        binding.model = data
        Glide.with(view.context).load(imageLink + data?.enAvatar).into(binding.imgEnprofifrgAvatar)


        setViewPager()
        getAllData(data!!, view)
        favoriteState()
        viewListener(view, data)
    }

    private fun viewListener(view: View, data: EngineerModelResponse) {
        binding.btnEngprofifrgHire.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(HIRE_AUTH_KEY, 0)
            sendIntent.putExtra(HIRE_DATA, data)
            sendIntent.putExtra(HIRE_DATA2, listProject)
            startActivity(sendIntent)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            binding.cgEnprofifrgAbility.removeAllViews()
            getAllData(data, view)
        }
    }

    private fun setViewPager() {
        val enginerAccountPagerAdapter = EngineerProfileViewPagerAdapter(childFragmentManager)
        binding.vpEngprofiact.adapter = enginerAccountPagerAdapter
        binding.tlEngprofiact.setupWithViewPager(binding.vpEngprofiact)
    }

    private fun getAllData(data: EngineerModelResponse, view: View) {
        coroutineScope.launch {
            val id = data.enID
            val cpID = goHipePreferences.getCompanyPreference().compID
            val listAbility = mutableListOf<AbilityModel>()
            val listPjIdHire = mutableListOf<Long>()

            binding.pgEngprofifrg.visibility = View.VISIBLE
            binding.swipeRefresh.isRefreshing = false
            binding.svEngprofifrg.visibility = View.GONE
            binding.btnEngprofifrgHire.visibility = View.GONE
            binding.imgEnprofifrgFavorite.visibility = View.GONE

            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            val result2 = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectCompany()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            val result3 = withContext(Dispatchers.IO) {
                try {
                    service.getAllHire()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result3 is GetAllHire) {
                result3.database?.map {
                    if (it.cpID == cpID && it.enID == data.enID && (it.hrStatus == "Approve" || it.hrStatus == "Reject")) listPjIdHire.add(it.pjID)
                }
            }

            if (result2 is GetAllProject) {
                val list = result2.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }

                mutableProject = list!!.toMutableList()
                mutableProject.removeAll { it.cpID != cpID }

                for (i in 0 until listPjIdHire.size) {
                    mutableProject.removeIf { it.pjID == listPjIdHire[i] }
                }

                mutableProject.map {
                    listProject.add(it.pjName.toString())
                }
            }

            if (result is EngineerGetByIDResponse) {
                for (i in 0 until result.database!!.size) {
                    result.database[i].enAbilityList?.map {
                        listAbility.add(AbilityModel(it.abID, it.enID, it.abName))
                    }
                }

                listAbility.removeAll { it.enID != id }
                chipViewInit(view, listAbility)
            }

            binding.pgEngprofifrg.visibility = View.GONE
            binding.svEngprofifrg.visibility = View.VISIBLE
            binding.btnEngprofifrgHire.visibility = View.VISIBLE
            binding.imgEnprofifrgFavorite.visibility = View.VISIBLE
        }
    }

    private fun chipViewInit(view: View, listAbility: MutableList<AbilityModel>) {
        for (i in 0 until listAbility.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbility[i].abName
            chip.setTextColor(resources.getColor(R.color.white))

            binding.cgEnprofifrgAbility.addView(chip)
        }
    }

    private fun favoriteState() {
        binding.imgEnprofifrgFavorite.setOnClickListener {
            if (!it.isSelected) {
                binding.imgEnprofifrgFavorite.setImageResource(R.drawable.ic_favorite_checked)
                binding.imgEnprofifrgFavorite.isSelected = true
            } else {
                binding.imgEnprofifrgFavorite.setImageResource(R.drawable.ic_favorite_unchecked)
                binding.imgEnprofifrgFavorite.isSelected = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}