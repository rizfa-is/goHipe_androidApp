package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyProjectScreenBinding
import com.istekno.gohipeandroidapp.models.SearchProject

class CompanyProjectScreenFragment(private val toolbar: MaterialToolbar, private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
    }

    private lateinit var binding: FragmentCompanyProjectScreenBinding
    private val listTop = ArrayList<SearchProject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar, co)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_project_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddproject.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(PROJECT_AUTH_KEY, 1)
            startActivity(sendIntent)
        }

        listTop.addAll(GoHipeDatabases.listSearchProject)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.rvCompanyProject.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListSearchProjectAdapter(listTop, object : ListSearchProjectAdapter.OnItemClickCallback {
                override fun onItemClicked(searchProject: SearchProject) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(PROJECT_AUTH_KEY, 1)
                    startActivity(sendIntent)
                }

                override fun onDeleteClicked() {
                    Toast.makeText(context, "Selected Delete Project", Toast.LENGTH_SHORT).show()
                }
            }, 1)
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar, co: CoordinatorLayout) {
        co.visibility = View.VISIBLE
        toolbar.title = "Project"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}