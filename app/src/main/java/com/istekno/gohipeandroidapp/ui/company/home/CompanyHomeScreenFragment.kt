package com.istekno.gohipeandroidapp.ui.company.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.activity.ProfileScreenActivity
import com.istekno.gohipeandroidapp.ui.adapter.SkillfulTalentAdapter
import com.istekno.gohipeandroidapp.ui.adapter.TalentOfTheMonthAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyHomeScreenBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.*
import com.istekno.gohipeandroidapp.ui.company.chat.CompanyChatScreenFragment
import com.istekno.gohipeandroidapp.ui.company.notification.CompanyNotificationScreenFragment
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyHomeScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView,
                                private val co: CoordinatorLayout, private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
        const val HOME_DATA = "home_data"
    }

    private lateinit var binding: FragmentCompanyHomeScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_home_screen, container, false)

        setToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        showRecyclerList()
        showRecyclerList2()

        getEngineerDetail()
        viewListener()
    }

    @SuppressLint("SetTextI18n")
    private fun getEngineerDetail() {
        coroutineScope.launch {
            val id = goHipePreferences.getCompanyPreference().acID
            val mutableEng = mutableListOf<EngineerModelResponse>()
            val mutableAb = mutableListOf<AbilityM>()

            binding.svHomecomp.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false
            binding.pgHomecomp.visibility = View.VISIBLE
            withContext(Dispatchers.IO) {
                try {
                    val result1 = service.getCompanyByID(id!!.toLong())
                    val result2 = service.getEngineerByFilter("2")

                    result2.database?.map {
                        if (it.enJobTitle?.isNotEmpty() == true && it.enAbilityList?.isNotEmpty() == true) {
                            mutableEng.add(EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar))
                            mutableAb.add(AbilityM(it.enAbilityList))
                        }
                    }

                    activity?.runOnUiThread {
                        binding.textViewHello.text = "Hai ${result1.database!![0].cpName!!.split(" ")[0]}"
                        (binding.rvListEngineer.adapter as TalentOfTheMonthAdapter).setData(mutableEng)
                        (binding.rvListEngineer2.adapter as SkillfulTalentAdapter).setData(mutableEng, mutableAb)
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            binding.svHomecomp.visibility = View.VISIBLE
            binding.pgHomecomp.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        binding.rvListEngineer.apply {
            val rvAdapter = TalentOfTheMonthAdapter()
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvAdapter.setOnItemClickCallback(object : TalentOfTheMonthAdapter.OnItemClickCallback {
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

    private fun showRecyclerList2() {
        binding.rvListEngineer2.apply {
            val rvAdapter = SkillfulTalentAdapter()
            rvAdapter.notifyDataSetChanged()

            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvAdapter.setOnItemClickCallback(object : SkillfulTalentAdapter.OnItemClickCallback {
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

    private fun viewListener() {
        binding.topAppBarCompanyHomefrg.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mn_maincontent_toolbar_chat -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_maincontent, CompanyChatScreenFragment(toolbar, bottomNavigationView, co))?.addToBackStack(null)?.commit()
                }
                R.id.mn_maincontent_toolbar_notification -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_maincontent, CompanyNotificationScreenFragment(toolbar, bottomNavigationView, co))?.addToBackStack(null)?.commit()
                }
            }
            false
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            getEngineerDetail()
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

        bottomNavigationView.visibility = View.VISIBLE
        binding.topAppBarCompanyHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        binding.topAppBarCompanyHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        binding.topAppBarCompanyHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        binding.topAppBarCompanyHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}