package com.istekno.gohipeandroidapp.ui.company.hire

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.activity.ProfileScreenActivity
import com.istekno.gohipeandroidapp.ui.adapter.ListHireAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyFinishedHireBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GetAllHire
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.HireModelResponse
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyFinishedHireFragment : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
    }

    private lateinit var binding: FragmentCompanyFinishedHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_finished_hire, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        showRecycleList(view)
        getHire()
        viewListener()
    }

    private fun viewListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            binding.imageView.visibility = View.GONE
            binding.tvReject.visibility = View.GONE
            getHire()
        }
    }

    private fun getHire() {
        val cpID = goHipePreferences.getCompanyPreference().compID
        var mutable: MutableList<HireModelResponse>

        binding.rvRejectedfrg.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
        binding.pgHirerejectfrg.visibility = View.VISIBLE
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
                mutable.removeAll { it.cpID != cpID }
                mutable.removeAll { it.hrStatus == "wait" || it.hrStatus == "progress" }

                if (mutable.isEmpty()) {
                    binding.imageView.visibility = View.VISIBLE
                    binding.tvReject.visibility = View.VISIBLE
                }

                (binding.rvRejectedfrg.adapter as ListHireAdapter).setData(mutable)
                binding.rvRejectedfrg.visibility = View.VISIBLE
                binding.pgHirerejectfrg.visibility = View.GONE
            }
        }
    }

    private fun showRecycleList(view: View) {
        binding.rvRejectedfrg.apply {
            val rvAdapter = ListHireAdapter(2)
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(view.context)
            rvAdapter.setOnItemClickCallback(object : ListHireAdapter.OnItemClickCallback {
                override fun onItemClicked(hireModelResponse: HireModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    var key = 0
                    if (hireModelResponse.hrStatus == "approve") {
                        key = 2
                    } else if (hireModelResponse.hrStatus == "reject") {
                        key = 3
                    }

                    sendIntent.putExtra(HIRE_AUTH_KEY, key)
                    sendIntent.putExtra(HIRE_DATA, hireModelResponse)
                    startActivity(sendIntent)
                }
            })
            adapter = rvAdapter
        }
    }
}