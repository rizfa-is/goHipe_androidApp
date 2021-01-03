package com.istekno.gohipeandroidapp.fragments.engineer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ScouterOfTheMonthAdapter
import com.istekno.gohipeandroidapp.adapter.SkillfulTalentAdapter
import com.istekno.gohipeandroidapp.adapter.TalentOfTheMonthAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerHomeScreenBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyHomeScreenFragment
import com.istekno.gohipeandroidapp.models.ScouterTop
import com.istekno.gohipeandroidapp.models.User
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerHomeScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView, private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
    }

    private lateinit var binding: FragmentEngineerHomeScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    private val listTop = ArrayList<ScouterTop>()
    private val listSkillful = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_home_screen, container, false)

        setToolbar(toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        listTop.addAll(GoHipeDatabases.listScouterOfTheMonth)
        listTop.sortByDescending { it.engineer_hired }
        listSkillful.addAll(GoHipeDatabases.listSearchEngineer)

        showRecyclerList()
        showRecyclerList2()
        getCompanyInfo()
        toolbarListener()
    }

    @SuppressLint("SetTextI18n")
    fun getCompanyInfo() {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().acID

            withContext(Dispatchers.IO) {
                try {
                    val result1 = service.getEngineerByID(id!!.toLong())

                    activity?.runOnUiThread {
                        binding.textViewHello.text = "Hai ${result1.database!![0].enName.split(" ")[0]}"
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showRecyclerList() {
        binding.rvListCompany.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = ScouterOfTheMonthAdapter(listTop, object : ScouterOfTheMonthAdapter.OnItemClickCallback {
                override fun onItemClicked(scouterTop: ScouterTop) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HOME_AUTH_KEY, 0)
                    startActivity(sendIntent)
                }
            })
        }
    }

    private fun showRecyclerList2() {
        val rvAdapter = SkillfulTalentAdapter()

        binding.rvListCompany2.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvAdapter.setData(listSkillful)
            rvAdapter.setOnitemClickCallback(object : SkillfulTalentAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(CompanyHomeScreenFragment.HOME_AUTH_KEY, 0)
                    startActivity(sendIntent)
                }
            })
            adapter = rvAdapter
        }
    }

    private fun toolbarListener() {
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
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        bottomNavigationView.visibility = View.VISIBLE
        co.visibility = View.GONE
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = true
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = true
    }
}