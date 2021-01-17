package com.istekno.gohipeandroidapp.fragments.engineer

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
import com.istekno.gohipeandroidapp.activities.EngineerMainContentActivity
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerDetailHireScreenBinding
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


class EngineerDetailHireScreenFragment(private val hireStatus: Int?) : Fragment() {

    companion object {
        const val HIRE_DATA = "hire_data"
        const val HIRE_ADD_AUTH_KEY = "hire_add_auth_key"

        const val imageLink = "http://107.22.89.131:7000/image/"

        private const val COMPLETED = "COMPLETED"
        private const val APPROVED = "APPROVED"
        private const val REJECTED = "REJECTED"
    }

    private lateinit var binding: FragmentEngineerDetailHireScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_detail_hire_screen, container, false)

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
        binding.tvEngdetailhirefrgprice.text = "Budget: $price"
        Glide.with(view.context).load(imageLink + model?.pjImage).into(binding.imgEngdetailhirefrgproject)

        getEngineerInfo(model!!, view)

        binding.btnEngdetailhirefrgEditproject.setOnClickListener {
            updateHireStatus("approve", model)

            dialog.dialog(view.context, "Hiring accepted!") {
                val sendIntent = Intent(view.context, EngineerMainContentActivity::class.java)
                sendIntent.putExtra(HIRE_ADD_AUTH_KEY, 1)
                startActivity(sendIntent)
            }
        }

        binding.btnEngdetailhirefrgEditproject2.setOnClickListener {
            updateHireStatus("reject", model)

            dialog.dialog(view.context, "Hiring denied!") {
                val sendIntent = Intent(view.context, EngineerMainContentActivity::class.java)
                sendIntent.putExtra(HIRE_ADD_AUTH_KEY, 1)
                startActivity(sendIntent)
            }
        }
    }

    private fun updateHireStatus(status: String, data: HireModelResponse) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.updateHireStatus(data.hrID, status)
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

                activity?.runOnUiThread {
                    binding.modelEng = mutable[0]
                    Glide.with(view.context).load(imageLink + mutable[0].enAvatar).into(binding.imgListSearchEng)
                }
            }

            binding.pgDetailhirefrg.visibility = View.GONE
            binding.svDetailhirefrg.visibility = View.VISIBLE
        }
    }

    private fun detailStatus(hireStatus: Int?) {
        when (hireStatus) {
            0 -> {
                binding.tvEngdetailhirefrghireStatus.visibility = View.GONE
            }
            1 -> {
                binding.btnEngdetailhirefrgEditproject.visibility = View.GONE
                binding.btnEngdetailhirefrgEditproject2.visibility = View.GONE
                binding.tvEngdetailhirefrghireStatus.text = APPROVED
            }
            2 -> {
                binding.btnEngdetailhirefrgEditproject.visibility = View.GONE
                binding.btnEngdetailhirefrgEditproject2.visibility = View.GONE
                binding.tvEngdetailhirefrghireStatus.text = REJECTED
                binding.tvEngdetailhirefrghireStatus.setTextColor(Color.RED)
            }
            5 -> {
                binding.btnEngdetailhirefrgEditproject.visibility = View.GONE
                binding.btnEngdetailhirefrgEditproject2.visibility = View.GONE
                binding.tvEngdetailhirefrghireStatus.text = COMPLETED
            }
        }
    }
}