package com.istekno.gohipeandroidapp.fragments.company

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
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyDetailHireScreenBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerDetailHireScreenFragment
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.HireModelResponse
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class CompanyDetailHireScreenFragment(private val hireStatus: Int?) : Fragment() {

    companion object {
        const val HIRE_AUTH_KEY = "hire_auth_key"
        const val HIRE_DATA = "hire_data"

        private const val APPROVED = "APPROVED"
        private const val REJECTED = "REJECTED"
    }

    private lateinit var binding: FragmentCompanyDetailHireScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences

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

        binding.btnComdetailprojectfrgEditproject.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(HIRE_AUTH_KEY, 20)
            startActivity(sendIntent)
        }
    }

    private fun getEngineerInfo(data: HireModelResponse, view: View) {
        coroutineScope.launch {
            val id = data.enID
            val mutable: MutableList<EngineerModelResponse>

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

                activity?.runOnUiThread {
                    binding.modelEng = mutable[0]
                    Glide.with(view.context).load(EngineerDetailHireScreenFragment.imageLink + mutable[0].enAvatar).into(binding.imgListSearchEng)
                }
            }
        }
    }

    private fun detailStatus(hireStatus: Int?) {
        when (hireStatus) {
            1 -> {
                binding.btnComdetailprojectfrgEditproject.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = APPROVED
            }
            2 -> {
                binding.btnComdetailprojectfrgEditproject.visibility = View.GONE
                binding.tvComdetailprojectfrgHireStatus.text = REJECTED
                binding.tvComdetailprojectfrgHireStatus.setTextColor(Color.RED)
            }
        }
    }
}