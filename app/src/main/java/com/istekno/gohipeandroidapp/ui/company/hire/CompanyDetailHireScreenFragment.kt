package com.istekno.gohipeandroidapp.ui.company.hire

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.activity.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyDetailHireScreenBinding
import com.istekno.gohipeandroidapp.ui.engineer.hire.EngineerDetailHireScreenFragment
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.HireModelResponse
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class CompanyDetailHireScreenFragment(private val hireStatus: Int?) : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"
        const val HIRE_DATA_EDIT = "hire_data_edit"
        const val HIRE_DATA_EDIT2 = "hire_data_edit2"


        private const val COMPLETED = "COMPLETED"
        private const val PROGRESS = "ON PROGRESS"
        private const val APPROVE = "APPROVED"
        private const val REJECTED = "REJECTED"
    }

    private lateinit var binding: FragmentCompanyDetailHireScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog
    private var engineer = listOf<EngineerModelResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_detail_hire_screen, container, false)

        detailStatus(hireStatus)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        viewListener(view)
    }

    @SuppressLint("SetTextI18n")
    private fun viewListener(view: View) {
        val model = activity?.intent?.getParcelableExtra<HireModelResponse>(HIRE_DATA)
        val price = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(model?.hrPrice?.toDouble())

        binding.model = model
        binding.tvComdetailhirefrgPrice.text = "Budget: $price"
        Glide.with(view.context).load(EngineerDetailHireScreenFragment.imageLink + model?.pjImage).into(binding.imgComdetailhirefrgProject)

        getEngineerInfo(model!!, view)

        binding.btnComdetailprojectfrgEdithire.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(HIRE_AUTH_KEY, 20)
            sendIntent.putExtra(HIRE_DATA_EDIT, model)
            sendIntent.putExtra(HIRE_DATA_EDIT2, engineer[0])
            startActivity(sendIntent)
        }

        binding.btnComdetailprojectfrgDeletehire.setOnClickListener {
            dialog.dialog(view.context, "Are you sure to delete ${model.pjName}") {
                deleteHire(model.hrID)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            getEngineerInfo(model, view)
        }
    }

    private fun deleteHire(id: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteHire(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getEngineerInfo(data: HireModelResponse, view: View) {
        coroutineScope.launch {
            val id = data.enID
            val mutable: MutableList<EngineerModelResponse>

            binding.pgDetailhirefrg.visibility = View.VISIBLE
            binding.swipeRefresh.isRefreshing = false
            binding.svDetailhirefrg.visibility = View.GONE
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                val listEngineer = result.database?.map {
                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                }
                mutable = listEngineer!!.toMutableList()
                mutable.removeAll { it.enID != id }

                binding.modelEng = mutable[0]
                engineer = mutable
                Glide.with(view.context).load(EngineerDetailHireScreenFragment.imageLink + mutable[0].enAvatar).into(binding.imgListSearchEng)
            }

            binding.pgDetailhirefrg.visibility = View.GONE
            binding.svDetailhirefrg.visibility = View.VISIBLE
        }
    }

    private fun detailStatus(hireStatus: Int?) {
        when (hireStatus) {
            1 -> {
                binding.btnComdetailprojectfrgDeletehire.visibility = View.GONE
                binding.btnComdetailprojectfrgEdithire.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = PROGRESS
                binding.tvComdetailprojectfrgHireStatus.setTextColor(Color.rgb(239, 167, 35))
            }
            2 -> {
                binding.btnComdetailprojectfrgDeletehire.visibility = View.GONE
                binding.btnComdetailprojectfrgEdithire.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = APPROVE
            }
            3 -> {
                binding.btnComdetailprojectfrgDeletehire.visibility = View.GONE
                binding.btnComdetailprojectfrgEdithire.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = REJECTED
                binding.tvComdetailprojectfrgHireStatus.setTextColor(Color.RED)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}