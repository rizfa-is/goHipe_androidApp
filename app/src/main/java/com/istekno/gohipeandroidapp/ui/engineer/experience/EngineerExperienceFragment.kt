package com.istekno.gohipeandroidapp.ui.engineer.experience

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.adapter.ListExperienceRecycleViewAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerExperienceBinding
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.*
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerExperienceFragment : Fragment(), EngineerExperienceContract.View {

    companion object {
        const val HOME_DATA = "home_data"
    }

    override val getEngineerID: Long
        get() {
            val goHipePreferences = GoHipePreferences(context!!)
            val data = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HOME_DATA)
            val dataShared = goHipePreferences.getEngineerPreference()

            return if (data != null) {
                data.enID!!
            } else {
                dataShared.engID!!
            }
        }

    private lateinit var binding: FragmentEngineerExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private var presenter: EngineerExperiencePresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_experience, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        val service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        presenter = EngineerExperiencePresenter(coroutineScope, service)

        showRecycleList(view)
    }

    override fun addListExperience(list: List<ExperienceModel>) {
        (binding.rvExperifrg.adapter as ListExperienceRecycleViewAdapter).setData(list)
    }

    override fun showProgressBar() {
        binding.imageView.visibility = View.GONE
        binding.tvExperience.visibility = View.GONE
        binding.pgExperifrg.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.pgExperifrg.visibility = View.GONE
    }

    override fun showEmptyDataView() {
        binding.imageView.visibility = View.VISIBLE
        binding.tvExperience.visibility = View.VISIBLE
    }

    private fun showRecycleList(view: View) {
        binding.rvExperifrg.apply {
            val rvAdapter = ListExperienceRecycleViewAdapter(1)
            rvAdapter.notifyDataSetChanged()

            layoutManager = LinearLayoutManager(view.context)
            adapter = rvAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
        presenter?.callExperienceApi()
    }

    override fun onStop() {
        presenter?.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }
}