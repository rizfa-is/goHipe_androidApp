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
data class SearchProject(
    val name : String,
    val desc : String,
    val image: Int
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
    var phone: Long = 0,
    var password: String? = null,
    var isLogin: Boolean = false
) : Parcelable

@Parcelize
data class MostPopular(
        val name : String,
        val job : String,
        val image: Int,
        val project: Int,
        val delivery_time: Int,
        val conv_rate: Int
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