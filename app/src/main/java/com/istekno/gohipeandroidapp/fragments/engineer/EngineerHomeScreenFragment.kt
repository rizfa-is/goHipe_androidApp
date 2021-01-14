package com.istekno.gohipeandroidapp.fragments.engineer

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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ScouterOfTheMonthAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerHomeScreenBinding
import com.istekno.gohipeandroidapp.models.ScouterTop
import com.istekno.gohipeandroidapp.models.User
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerHomeScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView,
                                 private val co: CoordinatorLayout, private val rl: RelativeLayout, private val state: Boolean) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
    }

    private lateinit var binding: FragmentEngineerHomeScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    private val listTop = ArrayList<ScouterTop>()

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
//        showRecyclerList2()
        getEngineerInfo()
        viewListener()
    }

    @SuppressLint("SetTextI18n")
    fun getEngineerInfo() {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().acID

            binding.svHomeeng.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false
            binding.pgHomeeng.visibility = View.VISIBLE
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

            binding.svHomeeng.visibility = View.VISIBLE
            binding.pgHomeeng.visibility = View.GONE
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

//    private fun showRecyclerList2() {
//        val rvAdapter = SkillfulTalentAdapter()
//
//        binding.rvListCompany2.apply {
//            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//            rvAdapter.setData(listSkillful)
//            rvAdapter.setOnitemClickCallback(object : SkillfulTalentAdapter.OnItemClickCallback {
//                override fun onItemClicked(user: User) {
//                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
//                    sendIntent.putExtra(CompanyHomeScreenFragment.HOME_AUTH_KEY, 0)
//                    startActivity(sendIntent)
//                }
//            })
//            adapter = rvAdapter
//        }
//    }

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
            getEngineerInfo()
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
}