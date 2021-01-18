package com.istekno.gohipeandroidapp.maincontent.company

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.FragmentCompanyEditHireScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.*
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*
import java.util.*

class CompanyEditHireScreenFragment(private val toolbar: MaterialToolbar): Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val HIRE_DATA = "hire_data"
        const val HIRE_DATA_EDIT = "hire_data_edit"
        const val HIRE_DATA_EDIT2 = "hire_data_edit2"
    }

    private lateinit var binding: FragmentCompanyEditHireScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_edit_hire_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        val model = activity?.intent?.getParcelableExtra<HireModelResponse>(HIRE_DATA_EDIT)
        val model2 = activity?.intent?.getParcelableExtra<EngineerModelResponse>(HIRE_DATA_EDIT2)

        if (model != null) {
            if (model2 != null) {
                setText(model, model2)
            }
            getAllProjectByCompanyID(view)
            viewListener(view, model)
        }
    }

    private fun viewListener(view: View, data: HireModelResponse) {
        binding.btnComedithirefrgUpdate.setOnClickListener {
            hire(view, data)
        }
    }

    private fun setText(data: HireModelResponse, data2: EngineerModelResponse) {
        val project = binding.etComedithirefrgProject
        val msg = binding.etComedithirefrgMessage
        val price = binding.etComedithirefrgPrice
        val engIDTextInput = binding.etComedithirefrgEngid
        val engIDFormat = "(ID: ${data.enID}) ${data2.enName}"


        project.setText(data.pjName)
        msg.setText(data.hrMessage)
        price.setText(data.hrPrice)
        engIDTextInput.setText(engIDFormat)
        engIDTextInput.setTextColor(Color.rgb(96, 96, 96))
        engIDTextInput.isEnabled = false
    }

    private fun hire(view: View, data: HireModelResponse) {
        val project = binding.etComedithirefrgProject.text.toString()
        val msg = binding.etComedithirefrgMessage.text.toString()
        val price = binding.etComedithirefrgPrice.text.toString()

        if (project.isEmpty()) {
            binding.etComedithirefrgProject.error = CompanyAddHireScreenFragment.FIELD_REQUIRED
            return
        }

        if (msg.isEmpty()) {
            binding.etComedithirefrgMessage.error = CompanyAddHireScreenFragment.FIELD_REQUIRED
            return
        }

        if (price.isEmpty()) {
            binding.etComedithirefrgPrice.error = CompanyAddHireScreenFragment.FIELD_REQUIRED
            return
        }

        updateHireStatus(price, msg, "wait", data)
        dialog.dialogCancel(view.context, "Update hire successful!") {
            activity?.onBackPressed()
        }
    }

    private fun updateHireStatus(price: String, message: String, status: String, data: HireModelResponse) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.updateHiring(data.hrID, data.enID, data.pjID, price, message, status)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getAllProjectByCompanyID(view: View) {
        val cpID = goHipePreferences.getCompanyPreference().compID
        var mutable: MutableList<ProjectModelResponse>
        val listProject = mutableListOf<String>()

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectCompany()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }
                mutable = list!!.toMutableList()
                mutable.removeAll { it.cpID != cpID }
                mutable.map {
                    listProject.add(it.pjName!!)
                }
                setDropdownMenuAdapter(view, listProject)
            }
        }
    }

    private fun setDropdownMenuAdapter(view: View, list: MutableList<String>) {
        val adapter = ArrayAdapter(view.context, R.layout.item_list_dropdown_template, list)
        (binding.itComedithirefrgProject.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setToolbar(toolbar: MaterialToolbar) {
        toolbar.title = ""
    }
}