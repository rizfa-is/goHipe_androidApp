package com.istekno.gohipeandroidapp.ui.engineer.experience

import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ExperienceModel

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