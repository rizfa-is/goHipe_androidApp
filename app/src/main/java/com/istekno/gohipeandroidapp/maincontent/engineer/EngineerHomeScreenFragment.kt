package com.istekno.gohipeandroidapp.maincontent.engineer

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
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ProjectOfTheMonthAdapter
import com.istekno.gohipeandroidapp.adapter.SkillfulTalentAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerHomeScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.*
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerHomeScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView,
                                 private val co: CoordinatorLayout, private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
        const val HOME_AUTH_KEY = "home_auth_key"
        const val HOME_DATA = "home_data"
    }

    private lateinit var binding: FragmentEngineerHomeScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_home_screen, container, false)

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
        getDataInfo()
        viewListener()
    }

    @SuppressLint("SetTextI18n")
    fun getDataInfo() {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().acID
            val mutableEng = mutableListOf<EngineerModelResponse>()
            val mutableAb = mutableListOf<AbilityM>()
            val mutablePro = mutableListOf<HireModelResponse>()

            binding.svHomeeng.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false
            binding.pgHomeeng.visibility = View.VISIBLE
            withContext(Dispatchers.IO) {
                try {
                    val result1 = service.getEngineerByID(id!!.toLong())
                    val result2 = service.getEngineerByFilter("2")
                    val result3 = service.getAllHire()

                    result2.database?.map {
                        if (it.enJobTitle?.isNotEmpty() == true && it.enAbilityList?.isNotEmpty() == true) {
                            mutableEng.add(EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar))
                            mutableAb.add(AbilityM(it.enAbilityList))
                        }
                    }

                    result3.database?.map {
                        if (it.hrStatus != "wait" && it.hrStatus != "progress" && it.pjImage?.isNotEmpty() == true) {
                            mutablePro.add(HireModelResponse(it.hrID, it.cpID, it.enID, it.pjID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage, it.hrPrice, it.hrMessage, it.hrStatus, it.hrDateConfirm, it.hrCreatedAt))
                        }
                    }

                    mutablePro.sortBy { it.hrPrice }
                    for (i in 0 until mutablePro.size) {
                        if (i >= 3) {
                            mutablePro.removeAt(i)
                        }
                    }

                    activity?.runOnUiThread {
                        binding.textViewHello.text = "Hai ${result1.database!![0].enName.split(" ")[0]}"
                        (binding.rvListProject.adapter as ProjectOfTheMonthAdapter).setData(mutablePro)
                        (binding.rvListEngineer.adapter as SkillfulTalentAdapter).setData(mutableEng, mutableAb)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            binding.svHomeeng.visibility = View.VISIBLE
            binding.pgHomeeng.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        binding.rvListProject.apply {
            val rvAdapter = ProjectOfTheMonthAdapter()
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvAdapter.setOnItemClickCallback(object : ProjectOfTheMonthAdapter.OnItemClickCallback {
                override fun onItemClicked(hireModelResponse: HireModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HIRE_AUTH_KEY, 15)
                    sendIntent.putExtra(HIRE_DATA, hireModelResponse)
                    startActivity(sendIntent)
                }
            })
            adapter = rvAdapter
        }
    }

    private fun showRecyclerList2() {
        binding.rvListEngineer.apply {
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
        binding.topAppBarEngineerHomefrg.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mn_maincontent_toolbar_chat -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_maincontent, EngineerChatScreenFragment(toolbar, bottomNavigationView))?.addToBackStack(null)?.commit()
                }
                R.id.mn_maincontent_toolbar_notification -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_maincontent, EngineerNotificationScreenFragment(toolbar, bottomNavigationView))?.addToBackStack(null)?.commit()
                }
            }
            false
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            getDataInfo()
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
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}