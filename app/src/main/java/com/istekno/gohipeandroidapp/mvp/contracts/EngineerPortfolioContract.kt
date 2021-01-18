package com.istekno.gohipeandroidapp.mvp.contracts

import com.istekno.gohipeandroidapp.retrofit.PortfolioModel

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