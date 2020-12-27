package com.istekno.gohipeandroidapp.fragments.company

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListHireAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyApprovedHireBinding
import com.istekno.gohipeandroidapp.models.HireModel

class CompanyApprovedHireFragment : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
    }

    private lateinit var binding: FragmentCompanyApprovedHireBinding

    private val dbHire = GoHipeDatabases
    private val listHiring = ArrayList<HireModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_approved_hire, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHire.statusHire = "approved"
        listHiring.clear()
        listHiring.addAll(dbHire.listHire)
        showRecycleList()
    }

    private fun showRecycleList() {
        binding.rvApprovedfrg.apply {
            layoutManager = LinearLayoutManager(view?.context)
            adapter = ListHireAdapter(listHiring, object : ListHireAdapter.OnItemClickCallback {
                override fun onItemClicked(hireModel: HireModel) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HIRE_AUTH_KEY, 1)
                    startActivity(sendIntent)
                }
            }, 1)
        }
    }
}