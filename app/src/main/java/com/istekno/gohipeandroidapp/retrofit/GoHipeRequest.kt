package com.istekno.gohipeandroidapp.retrofit

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class EngineerModelResponse(
         val enID: Long?,
         val enName: String?,
         val enJobTitle: String?,
         val enJobType: String?,
         val enLocation: String?,
         val enDesc: String?,
         val enEmail: String?,
         val enIG: String?,
         val enGithub: String?,
         val enGitlab: String?,
         val enAvatar: String?
) : Parcelable

@Parcelize
data class ProjectModelResponse(
    val pjID : Long?,
    val cpID: Long?,
    val pjName : String?,
    val pjDesc: String?,
    val pjDeadline: String?,
    val pjImage: String?
) : Parcelable

data class AbilityM(val list: ArrayList<Ability>)

data class AbilityModel(val abID: Long,
                        val enID: Long,
                        val abName: String?)

data class PortfolioModel(val prID: Long,
                          val enID: Long,
                          val prApplication: String?,
                          val prDesc: String?,
                          val prLink: String?,
                          val prRepo: String?,
                          val prCompany: String?,
                          val prRole: String?,
                          val prImg: String?)

data class ExperienceModel(val exID: Long,
                           val enID: Long,
                           val exRole: String?,
                           val exCompany: String?,
                           val exDesc: String?,
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