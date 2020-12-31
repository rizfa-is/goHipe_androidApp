package com.istekno.gohipeandroidapp.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EngineerModelRequest(
        val enID: String?,
        val enName: String?,
        val enJobTitle: String?,
        val enJobType: String?,
        val enLocation: String?,
        val enDesc: String?,
        val enEmail: String?,
        val enIG: String?,
        val enGithub: String?,
        val enGitlab: String?,
        val enAvatar: String?,
        val ability: ArrayList<EngineerGetByIDResponse.Ability>?,
        val portfolio: ArrayList<EngineerGetByIDResponse.Portfolio>?,
        val experience: ArrayList<EngineerGetByIDResponse.Experience>?
)

data class AbilityModel(
    val abID: String?,
    val enID: String?,
    val abName: String?)

data class PortfolioModel(val prID: String,
                     val enID: String,
                     val prApplication: String?,
                     val prDesc: String,
                     val prLink: String,
                     val prRepo: String,
                     val prCompany: String,
                     val prRole: String,
                     val prImg: String)

data class ExperienceModel(val exID: String,
                      val enID: String,
                      val exRole: String,
                      val exCompany: String,
                      val exDesc: String,
                      val exStartDate: String?,
                      val exEndDate: String?)

data class LoginModelRequest(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String)

data class EngineerRegisterModelRequest(
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("email")
        @Expose
        val email: String,
        @SerializedName("phone")
        @Expose
        val phone: String,
        @SerializedName("password")
        @Expose
        val password: String)