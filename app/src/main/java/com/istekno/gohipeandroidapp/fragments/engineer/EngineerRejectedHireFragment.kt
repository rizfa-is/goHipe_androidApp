package com.istekno.gohipeandroidapp.fragments.engineer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListHireAdapter
import com.istekno.gohipeandroidapp.databases.GoHipeDatabases
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerRejectedHireBinding
import com.istekno.gohipeandroidapp.models.HireModel

class EngineerRejectedHireFragment : Fragment() {

    private lateinit var binding: FragmentEngineerRejectedHireBinding

    private val dbHire = GoHipeDatabases
    private val listHiring = ArrayList<HireModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_rejected_hire, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHire.statusHire = "rejected"
        listHiring.clear()
        listHiring.addAll(dbHire.listHire)
        showRecycleList()
    }

    private fun showRecycleList() {
        binding.rvRejectedfrg.apply {
            layoutManager = LinearLayoutManager(view?.context)
            adapter = ListHireAdapter(listHiring, object : ListHireAdapter.OnItemClickCallback {
                override fun onItemClicked(hireModel: HireModel) {
                    Toast.makeText(context, "Selected ${hireModel.project}", Toast.LENGTH_SHORT).show()
                }
            }, 2)
        }
    }
}