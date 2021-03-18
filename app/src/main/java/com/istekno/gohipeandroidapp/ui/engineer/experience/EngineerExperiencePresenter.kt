package com.istekno.gohipeandroidapp.ui.engineer.experience

import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ExperienceModel
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EngineerExperiencePresenter(private val coroutineScope: CoroutineScope,
                                  private val service: GoHipeApiService
): EngineerExperienceContract.Presenter {

    private var view: EngineerExperienceContract.View? = null

    override fun bindToView(view: EngineerExperienceContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callExperienceApi() {
        coroutineScope.launch {
            val listExperience = mutableListOf<ExperienceModel>()

            view?.showProgressBar()
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
                        listExperience.add(ExperienceModel(it.exID, it.enID, it.exRole, it.exCompany, it.exDesc, it.exStartDate, it.exEndDate))
                    }
                }
                listExperience.removeAll { it.enID != view?.getEngineerID }

                if (listExperience.isEmpty()) {
                    view?.showEmptyDataView()
                }

                view?.addListExperience(listExperience)
                view?.hideProgressBar()
            }
        }
    }
}