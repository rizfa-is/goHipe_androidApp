package com.istekno.gohipeandroidapp.viewmodels

import android.app.Activity
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.istekno.gohipeandroidapp.retrofit.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EngineerSearchProjectViewModel : ViewModel(), CoroutineScope {

    private lateinit var service: GoHipeApiService
    val listProject = MutableLiveData<List<ProjectModelResponse>>()
    val projectAction = MutableLiveData<Boolean>()
    val isFailedStatus = MutableLiveData<String>()
    var state = ""

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSearchService(service: GoHipeApiService) {
        this.service = service
    }

    fun getAllProjectByCompanyID(filter: Int) {
        launch {
            val mutableProject: MutableList<ProjectModelResponse>
            val listPjIdHire = mutableListOf<Long>()

            projectAction.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getAllProjectCompany()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            val result2 = withContext(Dispatchers.IO) {
                try {
                    service.getAllHire()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result2 is GetAllHire) {
                result2.database?.map {
                    if (it.hrStatus != "wait") listPjIdHire.add(it.pjID)
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }
                mutableProject = list!!.toMutableList()

                for (i in 0 until listPjIdHire.size) {
                    mutableProject.removeIf { it.pjID == listPjIdHire[i] }
                }

                if (filter == 1) {
                    mutableProject.sortBy { it.pjName }
                } else if (filter == 2) {
                    mutableProject.sortBy { it.pjDeadline }
                }

                listProject.value = mutableProject
                isFailedStatus.value = "ADD LIST PROJECT"
            }
            projectAction.value = false
        }
    }

    fun getProjectByQuery(query: String, filter: Int) {
        launch {
            val mutableProject: MutableList<ProjectModelResponse>
            val listPjIdHire = mutableListOf<Long>()

            projectAction.value = true
            state = ""

            val result2 = withContext(Dispatchers.IO) {
                try {
                    service.getAllHire()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            val result = withContext(Dispatchers.IO) {
                try {
                    service.getProjectByQuery(query)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    val status = e.toString().split(" ")[2]
                    state = status
                }
            }

            if (result2 is GetAllHire) {
                result2.database?.map {
                    if (it.hrStatus != "wait") listPjIdHire.add(it.pjID)
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }
                mutableProject = list!!.toMutableList()

                for (i in 0 until listPjIdHire.size) {
                    mutableProject.removeIf { it.pjID == listPjIdHire[i] }
                }

                if (filter == 1) {
                    mutableProject.sortBy { it.pjName }
                } else if (filter == 2) {
                    mutableProject.sortBy { it.pjDeadline }
                }

                listProject.value = mutableProject
                isFailedStatus.value = "ADD LIST PROJECT"
            }

            if (state == "400") {
                isFailedStatus.value = state
            }
            projectAction.value = false
        }
    }
}