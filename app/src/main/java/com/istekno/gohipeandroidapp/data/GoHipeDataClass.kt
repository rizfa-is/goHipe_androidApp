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