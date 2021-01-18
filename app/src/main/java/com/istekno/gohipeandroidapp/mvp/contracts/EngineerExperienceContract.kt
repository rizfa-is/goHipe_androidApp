package com.istekno.gohipeandroidapp.mvp.contracts

import com.istekno.gohipeandroidapp.retrofit.ExperienceModel

interface EngineerExperienceContract {

    interface View {
        fun addListExperience(list: List<ExperienceModel>)
        fun showProgressBar()
        fun hideProgressBar()
        fun showEmptyDataView()

        val getEngineerID: Long
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun callExperienceApi()
    }
}