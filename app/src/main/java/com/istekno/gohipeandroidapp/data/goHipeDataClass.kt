package com.istekno.gohipeandroidapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Portfolio(
    val image : Int
) : Parcelable