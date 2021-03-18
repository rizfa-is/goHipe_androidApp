package com.istekno.gohipeandroidapp.ui.engineer.portfolio

import com.istekno.gohipeandroidapp.data.source.remote.retrofit.PortfolioModel

interface EngineerPortfolioContract {

    interface View {
        fun addListPortfolio(list: List<PortfolioModel>)
        fun showProgressBar()
        fun hideProgressBar()
        fun showEmptyDataView()

        val getEngineerID: Long
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun callPortfolioApi()
    }
}