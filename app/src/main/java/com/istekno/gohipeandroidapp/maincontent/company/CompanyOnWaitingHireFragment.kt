package com.istekno.gohipeandroidapp.maincontent.company

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.ProfileScreenActivity
import com.istekno.gohipeandroidapp.adapter.ListHireAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyOnwaitingHireBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.GetAllHire
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.HireModelResponse
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class CompanyOnWaitingHireFragment : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
    }

    private lateinit var binding: FragmentCompanyOnwaitingHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_onwaiting_hire, container, false)
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
            binding.tvOnwait.visibility = View.GONE
            getHire()
        }
    }

    private fun getHire() {
        val cpID = goHipePreferences.getCompanyPreference().compID
        var mutable: MutableList<HireModelResponse>

        binding.rvOnwaitfrg.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
        binding.pgHireonwaitfrg.visibility = View.VISIBLE
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
                mutable.removeAll { it.hrStatus != "wait"}

                if (mutable.isEmpty()) {
                    binding.imageView.visibility = View.VISIBLE
                    binding.tvOnwait.visibility = View.VISIBLE
                }

                (binding.rvOnwaitfrg.adapter as ListHireAdapter).setData(mutable)
                binding.rvOnwaitfrg.visibility = View.VISIBLE
                binding.pgHireonwaitfrg.visibility = View.GONE
            }
        }
    }

    private fun showRecycleList(view: View) {
        binding.rvOnwaitfrg.apply {
            val rvAdapter = ListHireAdapter(0)
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(view.context)
            rvAdapter.setOnItemClickCallback(object : ListHireAdapter.OnItemClickCallback {
                override fun onItemClicked(hireModelResponse: HireModelResponse) {
                    val sendIntent = Intent(context, ProfileScreenActivity::class.java)
                    sendIntent.putExtra(HIRE_AUTH_KEY, 0)
                    sendIntent.putExtra(HIRE_DATA, hireModelResponse)
                    startActivity(sendIntent)
                }
            })
            adapter = rvAdapter
        }
    }
}