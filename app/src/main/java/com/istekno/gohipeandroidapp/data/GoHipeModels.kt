package com.istekno.gohipeandroidapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Portfolio(
    val image : Int
) : Parcelable

@Parcelize
data class Experience(
    val image : Int,
    val job : String,
    val company : String,
    val period : String,
    val totalMonth: String,
    val description : String
) : Parcelable

@Parcelize
data class EngineerModel(
    var name: String? = null,
    var email: String? = null,
    var phone: Long = 0,
    var password: String? = null,
    var isLogin: Boolean = false
) : Parcelable

@Parcelize
data class CompanyModel(
    var name: String? = null,
    var email: String? = null,
    var company: String? = null,
    var position: String? = null,
    var phone: Int = 0,
    var password: String? = null,
    var isLogin: Boolean = false
) : Parcelable