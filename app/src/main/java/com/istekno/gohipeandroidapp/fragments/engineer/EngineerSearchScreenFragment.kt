package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerSearchScreenBinding
import com.istekno.gohipeandroidapp.models.SearchProject

class EngineerSearchScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentEngineerSearchScreenBinding
    private val listTop = ArrayList<SearchProject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

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
                    Toast.makeText(context, "Selected ${searchProject.name}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.visibility = View.GONE
    }
}