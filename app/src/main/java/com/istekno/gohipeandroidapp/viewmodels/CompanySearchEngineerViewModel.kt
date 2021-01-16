package com.istekno.gohipeandroidapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.istekno.gohipeandroidapp.retrofit.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CompanySearchEngineerViewModel() : ViewModel(), CoroutineScope {

    private lateinit var service: GoHipeApiService
    private var listENG = MutableLiveData<List<EngineerModelResponse>>()
    private var listAB = MutableLiveData<List<AbilityM>>()
    val engAction = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSearchService(service: GoHipeApiService) {
        this.service = service
    }

    fun getAllEngineer(filter: String) {
        launch {
            engAction.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByFilter(filter)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                val listAbility = result.database?.map { AbilityM(it.enAbilityList!!) }
                val listEngineer = result.database?.map {
                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                }

                listENG.value = listEngineer
                listAB.value = listAbility
            }
            engAction.value = false
        }
    }

    fun getEngineerByQuery(query: String, filter: String) {
        launch {
            engAction.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByQuery(query, filter)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                val listAbility = result.database?.map { AbilityM(it.enAbilityList!!) }
                val listEngineer = result.database?.map {
                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                }

                listENG.value = listEngineer
                listAB.value = listAbility
            }
            engAction.value = false
        }
    }

    fun getAllData(): LiveData<List<EngineerModelResponse>> {
        return listENG
    }

    fun getListAbility(): LiveData<List<AbilityM>> {
        return listAB
    }
}