package com.istekno.gohipeandroidapp.models

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
data class User(
        val name : String,
        val job : String,
        val image: Int,
        val ability: List<String>
) : Parcelable

@Parcelize
data class EngineerModel(
    var acID: Long? = null,
    var engID: Long? = null,
    var level: String? = null,
    var token: String? = null,
    var isLogin: Boolean = false
) : Parcelable

@Parcelize
data class CompanyModel(
        var acID: Long? = null,
        var compID: Long? = null,
        var level: String? = null,
        var token: String? = null,
        var isLogin: Boolean = false
) : Parcelable

@Parcelize
data class MostPopular(
        val name : String,
        val job : String,
        val image: String,
        val project: Int? = null,
        val delivery_time: Int? = null,
        val conv_rate: Int? = null
) : Parcelable

@Parcelize
data class ScouterTop(
    val name : String,
    val field : String,
    val image: Int,
    val project: Int,
    val engineer_hired: Int,
    val requirement_rate: Double
) : Parcelable

@Parcelize
data class HireModel(
    val project : String,
    val desc : String,
    val image: Int,
    val deadline: String
) : Parcelable

@Parcelize
data class EngineerModels(
    val enID: String?,
    val enName: String?,
    val enJobTitle: String?,
    val enJobType: String?,
    val enLocation: String?,
    val enDesc: String?,
    val enEmail: String?,
    val enIG: String?,
    val enGithub: String?,
    val enGitlab: String?
) : Parcelable

@Parcelize
data class Ability(
    val abID: String?,
    val enID: String?,
    val abName: String?
) : Parcelable