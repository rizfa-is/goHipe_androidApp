package com.istekno.gohipeandroidapp.fragments.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyProjectScreenBinding
import com.istekno.gohipeandroidapp.models.SearchProject

class CompanyProjectScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentCompanyProjectScreenBinding
    private val listTop = ArrayList<SearchProject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_project_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listTop.addAll(GoHipeDatabases.listSearchProject)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.rvCompanyProject.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListSearchProjectAdapter(listTop, object : ListSearchProjectAdapter.OnItemClickCallback {
                override fun onItemClicked(searchProject: SearchProject) {
                    TODO()
                }
            })
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.visibility = View.VISIBLE
        toolbar.title = "Project"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }
}