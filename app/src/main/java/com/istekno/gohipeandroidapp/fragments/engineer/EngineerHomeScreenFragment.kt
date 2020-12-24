package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.TalentOfTheMonthAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerHomeScreenBinding
import com.istekno.gohipeandroidapp.models.MostPopular

class EngineerHomeScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView) : Fragment() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
    }

    private lateinit var binding: FragmentEngineerHomeScreenBinding
    private val listTop = ArrayList<MostPopular>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_home_screen, container, false)

        setToolbar(toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listTop.addAll(GoHipeDatabases.listEngineerTalentOfTheMonth)
        listTop.sortByDescending { it.project }

        showRecyclerList()
        toolbarListener()
    }

    private fun showRecyclerList() {
        binding.rvListCompany.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = TalentOfTheMonthAdapter(listTop, object : TalentOfTheMonthAdapter.OnItemClickCallback {
                override fun onItemClicked(mostPopular: MostPopular) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HOME_AUTH_KEY, 0)
                    startActivity(sendIntent)
                }
            })
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
        toolbar.visibility = View.GONE
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = true
        binding.topAppBarEngineerHomefrg.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = true
    }
}