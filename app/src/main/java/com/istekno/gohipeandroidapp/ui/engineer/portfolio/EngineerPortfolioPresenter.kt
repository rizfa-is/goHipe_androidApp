package com.istekno.gohipeandroidapp.ui.engineer.portfolio

import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.PortfolioModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EngineerPortfolioPresenter(private val coroutineScope: CoroutineScope,
                                 private val service: GoHipeApiService
): EngineerPortfolioContract.Presenter {

    private var view: EngineerPortfolioContract.View? = null

    override fun bindToView(view: EngineerPortfolioContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callPortfolioApi() {
        coroutineScope.launch {
            val listPortfolio = mutableListOf<PortfolioModel>()

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
                    result.database[i].enPortfolioList?.map {
                        listPortfolio.add(PortfolioModel(it.prID, it.enID, it.prApplication, it.prDesc, it.prLink, it.prRepo, it.prCompany, it.prRole, it.prImg))
                    }
                }
                listPortfolio.removeAll { it.enID != view?.getEngineerID }

                if (listPortfolio.isEmpty()) {
                    view?.showEmptyDataView()
                }

                view?.addListPortfolio(listPortfolio)
                view?.hideProgressBar()
            }
        }
    }
}