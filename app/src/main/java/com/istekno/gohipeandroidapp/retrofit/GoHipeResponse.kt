package com.istekno.gohipeandroidapp.retrofit

import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

data class EngineerResponse(val success: String, val message: String, val database: ArrayList<Engineer>) {
    data class Engineer(@SerializedName("en_id") val enID: String,
                        @SerializedName("ac_name") val enName: String,
                        @SerializedName("en_job_title") val enJobTitle: String,
                        @SerializedName("en_job_type") val enJobType: String,
                        @SerializedName("en_location") val enLocation: String,
                        @SerializedName("en_desc") val enDesc: String,
                        @SerializedName("ac_email") val enEmail: String,
                        @SerializedName("en_ig") val enIG: String,
                        @SerializedName("en_github") val enGithub: String,
                        @SerializedName("en_gitlab") val enGitlab: String,
                        @SerializedName("en_avatar") val enAvatar: String,
                        @SerializedName("ability") val enAbilityList: ArrayList<Ability>,
                        @SerializedName("portfolio") val enPortfolioList: ArrayList<Portfolio>,
                        @SerializedName("experience") val enExperienceList: ArrayList<Experience>)

    data class Ability(@SerializedName("ab_id") val abID: String,
                       @SerializedName("en_id") val enID: String,
                       @SerializedName("ab_name") val abName: String)

    data class Portfolio(@SerializedName("pr_id") val prID: String,
                         @SerializedName("en_id") val enID: String,
                         @SerializedName("pr_application") val prApplication: String,
                         @SerializedName("pr_desc") val prDesc: String,
                         @SerializedName("pr_link") val prLink: String,
                         @SerializedName("pr_repo") val prRepo: String,
                         @SerializedName("pr_company") val prCompany: String,
                         @SerializedName("pr_role") val prRole: String,
                         @SerializedName("pr_img") val prImg: String)

    data class Experience(@SerializedName("ex_id") val exID: String,
                         @SerializedName("en_id") val enID: String,
                         @SerializedName("ex_role") val exRole: String,
                         @SerializedName("ex_company") val exCompany: String,
                         @SerializedName("ex_desc") val exDesc: String,
                         @SerializedName("ex_start") val exStartDate: String?,
                         @SerializedName("ex_end") val exEndDate: String?)
}

data class LoginResponse(val success: String, val message: String, val database: LoginModel) {
    data class LoginModel(val email: String, val level: String, val token: String)
}

data class EngineerRegisterResponse(val success: String, val message: String, val database: ArrayList<EngineerRegisterGHR>) {
    data class EngineerRegisterGHR(val id: String,
                                   @SerializedName("ac_name") val enName: String,
                                   @SerializedName("ac_email") val enEmail: String,
                                   @SerializedName("ac_phone") val enPhone: String,
                                   @SerializedName("ac_level") val enLevel: String,
                                   @SerializedName("ac_created_at") val acCreatedAt: String,
                                   @SerializedName("ac_updated_at") val acUpdatedAt: String,
                                   @SerializedName("en_created_at") val enCreatedAt: String,
                                   @SerializedName("en_updated_at") val enUpdatedAt: String)
}