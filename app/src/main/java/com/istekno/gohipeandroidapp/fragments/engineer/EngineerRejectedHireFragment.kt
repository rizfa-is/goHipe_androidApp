package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerRejectedHireBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyRejectedHireFragment
import com.istekno.gohipeandroidapp.models.HireModel
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GetAllHire
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.HireModelResponse
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerRejectedHireFragment : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
    }

    private lateinit var binding: FragmentEngineerRejectedHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_rejected_hire, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        showRecycleList()
        getHire()
    }

    private fun getHire() {
        val enID = goHipePreferences.getEngineerPreference().engID
        var mutable: MutableList<HireModelResponse>

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllHire()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GetAllHire) {
                val list = result.database?.map {
                    HireModelResponse(it.hrID, it.cpID, it.enID, it.pjID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage, it.hrPrice, it.hrMessage, it.hrStatus, it.hrDateConfirm, it.hrCreatedAt)
                }
                mutable = list!!.toMutableList()
                mutable.removeAll { it.enID != enID }
                mutable.removeAll { it.hrStatus != "reject"}

                Log.e("listHire Reject", mutable.toString())
                (binding.rvRejectedfrg.adapter as ListHireAdapter).setData(mutable)
                binding.pgHireengfrgC.visibility = View.GONE
            }
        }
    }

    private fun showRecycleList() {
        binding.rvRejectedfrg.apply {
            layoutManager = LinearLayoutManager(view?.context)
            val rvAdapter = ListHireAdapter(2)
            rvAdapter.setOnItemClickCallback(object : ListHireAdapter.OnItemClickCallback {
                override fun onItemClicked(hireModelResponse: HireModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HIRE_AUTH_KEY, 12)
                    sendIntent.putExtra(HIRE_DATA, hireModelResponse)
                    startActivity(sendIntent)
                }
            })
            adapter = rvAdapter
        }
    }
}