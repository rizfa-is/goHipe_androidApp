package com.istekno.gohipeandroidapp.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.istekno.gohipeandroidapp.adapter.ListSearchProjectAdapter
import com.istekno.gohipeandroidapp.retrofit.GetAllProject
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.retrofit.ProjectModelResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EngineerSearchProjectViewModel() : ViewModel(), CoroutineScope {

    private lateinit var service: GoHipeApiService
    private var listProject = MutableLiveData<List<ProjectModelResponse>>()
    val projectAction = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSearchService(service: GoHipeApiService) {
        this.service = service
    }

    fun getAllProjectByCompanyID() {
        launch {
            projectAction.value = true
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

                listProject.value = list
                projectAction.value = false
            }
        }
    }

    fun getProjectByQuery(query: String) {
        launch {
            projectAction.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getProjectByQuery(query)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GetAllProject) {
                val list = result.database?.map {
                    ProjectModelResponse(it.pjID, it.cpID, it.pjName, it.pjDesc, it.pjDeadline, it.pjImage)
                }

                listProject.value = list
                projectAction.value = false
            }
        }
    }

    fun getListProject(): LiveData<List<ProjectModelResponse>> {
        return listProject
    }


}