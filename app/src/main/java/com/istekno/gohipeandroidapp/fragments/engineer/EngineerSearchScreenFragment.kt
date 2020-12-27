package com.istekno.gohipeandroidapp.fragments.engineer

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
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerSearchScreenBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyProjectScreenFragment
import com.istekno.gohipeandroidapp.models.SearchProject

class EngineerSearchScreenFragment(private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val PROJECT_AUTH_KEY = "project_auth_key"
    }

    private lateinit var binding: FragmentEngineerSearchScreenBinding
    private val listTop = ArrayList<SearchProject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_search_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listTop.addAll(GoHipeDatabases.listSearchProject)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.rvSearchListProject.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListSearchProjectAdapter(listTop, object : ListSearchProjectAdapter.OnItemClickCallback {
                override fun onItemClicked(searchProject: SearchProject) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(PROJECT_AUTH_KEY, 0)
                    startActivity(sendIntent)
                }

                override fun onDeleteClicked() {
                    TODO()
                }
            }, 0)
        }
    }

    private fun setToolbar() {
        co.visibility = View.GONE
    }
}