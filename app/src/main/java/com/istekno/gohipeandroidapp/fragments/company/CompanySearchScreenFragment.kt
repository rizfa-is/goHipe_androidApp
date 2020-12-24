package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListSearchEngineerAdapter
import com.istekno.gohipeandroidapp.adapter.ScouterOfTheMonthAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentCompanySearchScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerHomeScreenFragment
import com.istekno.gohipeandroidapp.models.ScouterTop
import com.istekno.gohipeandroidapp.models.User

class CompanySearchScreenFragment(private val toolbar: MaterialToolbar) : Fragment() {

    private lateinit var binding: FragmentCompanySearchScreenBinding
    private val listTop = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        toolbarListener(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_search_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listTop.addAll(GoHipeDatabases.listSearchEngineer)

        showRecyclerList()
        toolbarListener(toolbar)
    }

    private fun showRecyclerList() {
        binding.rvSearchListEngineer.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ListSearchEngineerAdapter(listTop, object : ListSearchEngineerAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(EngineerHomeScreenFragment.HOME_AUTH_KEY, 1)
                    startActivity(sendIntent)
                }
            })
        }
    }

    private fun toolbarListener(toolbar: MaterialToolbar) {
        toolbar.visibility = View.GONE
    }
}