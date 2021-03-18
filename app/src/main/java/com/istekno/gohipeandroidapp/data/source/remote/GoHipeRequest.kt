package com.istekno.gohipeandroidapp.data.source.remote.retrofit

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EngineerModelResponse(
         val enID: Long?,
         val enName: String?,
         val enPhone: String?,
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
data class CompanyModelResponse(
        val cpID: Long,
        val cpName: String?,
        val cpEmail: String?,
        val cpPhone: String?,
        val cpCompany: String?,
        val cpPosition: String?,
        val cpField: String?,
        val cpLocation: String?,
        val cpDesc: String?,
        val cpInsta: String?,
        val cpLinkedIn: String?,
        val cpAvatar: String?
) : Parcelable

data class SearchEngineerModelResponse(
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
        val enAvatar: String?,
        val listAbility: ArrayList<Ability>?
)

@Parcelize
data class ProjectModelResponse(
    val pjID : Long?,
    val cpID: Long?,
    val pjName : String?,
    val pjDesc: String?,
    val pjDeadline: String?,
    val pjImage: String?
) : Parcelable

@Parcelize
data class HireModelResponse(
        val hrID: Long,
        val cpID: Long,
        val enID: Long,
        val pjID: Long,
        val pjName : String?,
        val pjDesc: String?,
        val pjDeadline: String?,
        val pjImage: String?,
        val hrPrice: String?,
        val hrMessage: String?,
        val hrStatus: String?,
        val hrDateConfirm: String?,
        val hrCreatedAt: String?
) : Parcelable

data class AbilityM(val list: ArrayList<Ability>)

@Parcelize
data class AbilityModel(val abID: Long,
                        val enID: Long,
                        val abName: String?
) : Parcelable

@Parcelize
data class PortfolioModel(val prID: Long,
                          val enID: Long,
                          val prApplication: String?,
                          val prDesc: String?,
                          val prLink: String?,
                          val prRepo: String?,
                          val prCompany: String?,
                          val prRole: String?,
                          val prImg: String?
) : Parcelable

@Parcelize
data class ExperienceModel(val exID: Long,
                           val enID: Long,
                           val exRole: String?,
                           val exCompany: String?,
                           val exDesc: String?,
                           val exStartDate: String?,
                           val exEndDate: String?
): Parcelable