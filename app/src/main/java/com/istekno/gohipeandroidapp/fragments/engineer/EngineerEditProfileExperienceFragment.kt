package com.istekno.gohipeandroidapp.fragments.engineer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.adapter.ListExperienceRecycleViewAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileExperienceBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.ExperienceModel
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class EngineerEditProfileExperienceFragment : Fragment() {

    private lateinit var binding: FragmentEngineerEditProfileExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    private val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile_experience, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        val dataShared = goHipePreferences.getEngineerPreference()

        showRecycleList(view, dataShared.engID!!)
        getExperience(dataShared.engID!!)
        viewListener(view, dataShared.engID!!)
    }

    private fun viewListener(view: View, id: Long) {
        binding.fabEditexperifrg.setOnClickListener {
            setFormDialog(view, 0, id)
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            getExperience(id)
        }
    }

    private fun getExperience(enID: Long) {
        coroutineScope.launch {
            val listExperiences = mutableListOf<ExperienceModel>()

            binding.fabEditexperifrg.visibility = View.GONE
            binding.rvEditexperifrg.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false
            binding.pgEditexperifrg.visibility = View.VISIBLE
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                for (i in 0 until result.database!!.size) {
                    result.database[i].enExperienceList?.map {
                        listExperiences.add(ExperienceModel(it.exID, it.enID, it.exRole, it.exCompany, it.exDesc, it.exStartDate, it.exEndDate))
                    }
                }

                listExperiences.removeAll { it.enID != enID }
                (binding.rvEditexperifrg.adapter as ListExperienceRecycleViewAdapter).setData(listExperiences)
                binding.fabEditexperifrg.visibility = View.VISIBLE
                binding.rvEditexperifrg.visibility = View.VISIBLE
                binding.pgEditexperifrg.visibility = View.GONE
            }
        }
    }

    private fun addExperience(exRole: String, exCompany: String, exDesc: String, exStartDate: String, exEndDate: String) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().engID

            withContext(Dispatchers.IO) {
                try {
                    service.addExperience(id!!.toLong(), exRole, exCompany, exDesc, exStartDate, exEndDate)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateExperience(exID: Long, exRole: String, exCompany: String, exDesc: String, exStartDate: String, exEndDate: String) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().engID

            withContext(Dispatchers.IO) {
                try {
                    service.updateExperience(exID, id!!.toLong(), exRole, exCompany, exDesc, exStartDate, exEndDate)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteExperience(exID: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteExperience(exID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun retrieveDate(view: View, editText: TextInputEditText, type: Int, yearData: Int = 0, monthData: Int = 0, dayData: Int = 0) {
        val date: DatePickerDialog.OnDateSetListener

        date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(editText)
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(view.context, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun updateLabel(editText: TextInputEditText) {
        val myFormat = "YYYY-MM-DD" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        editText.setText(sdf.format(myCalendar.time))
    }

    private fun showRecycleList(view: View, id: Long) {
        binding.rvEditexperifrg.apply {
            val rvAdapter = ListExperienceRecycleViewAdapter(0)
            layoutManager = LinearLayoutManager(view.context)
            rvAdapter.onItemClickCallbak(object : ListExperienceRecycleViewAdapter.OnItemClickCallback {
                override fun onUpdatePressed(experienceModel: ExperienceModel) {
                    val start = experienceModel.exStartDate!!.split('T')[0]
                    val end = experienceModel.exEndDate!!.split('T')[0]

                    setFormDialog(view, 1, id, experienceModel.exID, experienceModel.exRole!!, experienceModel.exCompany!!, experienceModel.exDesc!!, start, end)
                }

                override fun onDeletePressed(experienceModel: ExperienceModel) {
                    dialog.dialog(view.context, "Are you sure delete ${experienceModel.exRole}") {
                        dialog.dialogCancel(view.context, "${experienceModel.exRole} deleted!") {
                            deleteExperience(experienceModel.exID)
                            binding.swipeRefresh.isRefreshing = true
                            getExperience(id)
                        }
                    }
                }
            })
            adapter = rvAdapter
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun setFormDialog(view: View, type: Int, enId: Long, id: Long = 0, exRole: String? = "Empty", exCompany: String? = "Empty", exDesc: String? = "Empty", exStart: String? = "Empty", exEnd: String? = "Empty") {
        val dialog = AlertDialog.Builder(view.context)
        val customView = LayoutInflater.from(view.context).inflate(R.layout.fragment_engineer_addupdate_experience_dialog, null)
        val header = customView.findViewById<TextView>(R.id.tv_header)
        val role = customView.findViewById<TextInputEditText>(R.id.et_addex_role)
        val company = customView.findViewById<TextInputEditText>(R.id.et_addex_company)
        val desc = customView.findViewById<TextInputEditText>(R.id.et_addex_desc)
        val startDate = customView.findViewById<TextInputEditText>(R.id.et_addex_startdate)
        val endDate = customView.findViewById<TextInputEditText>(R.id.et_addex_enddate)

        val btnAdd = customView.findViewById<MaterialButton>(R.id.btn_addex)
        val cDialog = dialog.setView(customView).create()

        if (type == 1) {
            val startD = exStart?.split('-')
            val endD = exEnd?.split('-')
            val add = startD!![0].toInt() + startD[1].toInt()

            Log.e("add", add.toString())

            retrieveDate(view, startDate, 1, startD!![0].toInt(), startD[1].toInt(), startD[2].toInt())
            retrieveDate(view, endDate, 1, endD!![0].toInt(), endD[1].toInt(), endD[2].toInt())

            header.text = "Update experience"
            btnAdd.text = "Update"
            role.setText(exRole)
            company.setText(exCompany)
            desc.setText(exDesc)
            startDate.setText(exStart)
            endDate.setText(exEnd)
        } else {
            retrieveDate(view, startDate, 0)
            retrieveDate(view, endDate, 0)
        }

        cDialog.show()
        btnAdd.setOnClickListener {
            if (type == 1) {
                val inputRole = role.text.toString()
                val inputComp = company.text.toString()
                val inputDesc = desc.text.toString()
                val inputStart = startDate.text.toString()
                val inputEnd = endDate.text.toString()

                if (inputRole.isEmpty()) {
                    role.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputComp.isEmpty()) {
                    company.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputDesc.isEmpty()) {
                    desc.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputStart.isEmpty()) {
                    startDate.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputEnd.isEmpty()) {
                    endDate.error = "Form must be filled"
                    return@setOnClickListener
                }

                updateExperience(id, inputRole, inputComp, inputDesc, inputStart, inputEnd)
                Toast.makeText(view.context, "Success update experience", Toast.LENGTH_SHORT).show()
                cDialog.cancel()
                binding.swipeRefresh.isRefreshing = true
                getExperience(enId)
            } else {
                val inputRole = role.text.toString()
                val inputComp = company.text.toString()
                val inputDesc = desc.text.toString()
                val inputStart = startDate.text.toString()
                val inputEnd = endDate.text.toString()

                if (inputRole.isEmpty()) {
                    role.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputComp.isEmpty()) {
                    company.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputDesc.isEmpty()) {
                    desc.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputStart.isEmpty()) {
                    startDate.error = "Form must be filled"
                    return@setOnClickListener
                }

                if (inputEnd.isEmpty()) {
                    endDate.error = "Form must be filled"
                    return@setOnClickListener
                }

                addExperience(inputRole, inputComp, inputDesc, inputStart, inputEnd)
                Toast.makeText(view.context, "Success add experience", Toast.LENGTH_SHORT).show()
                cDialog.cancel()
                binding.swipeRefresh.isRefreshing = true
                getExperience(enId)
            }
        }
    }
}