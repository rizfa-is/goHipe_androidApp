package com.istekno.gohipeandroidapp.retrofit

import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

data class GeneralResponse(@SerializedName("success") val success: Boolean, @SerializedName("message") val message: String) {
}

data class LoginResponse(val success: Boolean, val message: String, val database: LoginModel?) {
    data class LoginModel(val acID: Long, val email: String, val level: String, val token: String)
}

data class EngineerGetByIDResponse(val success: Boolean, val message: String, val database: ArrayList<Engineer>?) {
    data class Engineer(@SerializedName("en_id") val enID: Long,
                        @SerializedName("ac_name") val enName: String,
                        @SerializedName("ac_phone") val enPhone: String,
                        @SerializedName("en_job_title") val enJobTitle: String?,
                        @SerializedName("en_job_type") val enJobType: String?,
                        @SerializedName("en_location") val enLocation: String?,
                        @SerializedName("en_desc") val enDesc: String?,
                        @SerializedName("ac_email") val enEmail: String?,
                        @SerializedName("en_ig") val enIG: String?,
                        @SerializedName("en_github") val enGithub: String?,
                        @SerializedName("en_gitlab") val enGitlab: String?,
                        @SerializedName("en_avatar") val enAvatar: String?,
                        @SerializedName("ability") val enAbilityList: ArrayList<Ability>?,
                        @SerializedName("portfolio") val enPortfolioList: ArrayList<Portfolio>?,
                        @SerializedName("experience") val enExperienceList: ArrayList<Experience>?)


}

data class Ability(@SerializedName("ab_id") val abID: Long,
                   @SerializedName("en_id") val enID: Long,
                   @SerializedName("ab_name") val abName: String?)

data class Portfolio(@SerializedName("pr_id") val prID: Long,
                     @SerializedName("en_id") val enID: Long,
                     @SerializedName("pr_application") val prApplication: String?,
                     @SerializedName("pr_desc") val prDesc: String?,
                     @SerializedName("pr_link") val prLink: String?,
                     @SerializedName("pr_repo") val prRepo: String?,
                     @SerializedName("pr_company") val prCompany: String?,
                     @SerializedName("pr_role") val prRole: String?,
                     @SerializedName("pr_img") val prImg: String?)

data class Experience(@SerializedName("ex_id") val exID: Long,
                      @SerializedName("en_id") val enID: Long,
                      @SerializedName("ex_role") val exRole: String?,
                      @SerializedName("ex_company") val exCompany: String?,
                      @SerializedName("ex_desc") val exDesc: String?,
                      @SerializedName("ex_start") val exStartDate: String?,
                      @SerializedName("ex_end") val exEndDate: String?)


data class EngineerRegisterResponse(val success: Boolean, val message: String, val database: EngineerRegisterGHR?) {
    data class EngineerRegisterGHR(@SerializedName("id") val id: Long,
                                   @SerializedName("ac_name") val enName: String,
                                   @SerializedName("ac_email") val enEmail: String,
                                   @SerializedName("ac_phone") val enPhone: String,
                                   @SerializedName("ac_level") val enLevel: String,
                                   @SerializedName("ac_created_at") val acCreatedAt: String,
                                   @SerializedName("ac_updated_at") val acUpdatedAt: String,
                                   @SerializedName("en_created_at") val enCreatedAt: String,
                                   @SerializedName("en_updated_at") val enUpdatedAt: String)
}

data class CompanyGetByIDResponse(val success: Boolean, val message: String, val database: ArrayList<Company>?) {
    data class Company(@SerializedName("ac_id") val acID: Long,
                        @SerializedName("cp_id") val cpID: Long,
                        @SerializedName("ac_name") val acName: String?,
                        @SerializedName("ac_email") val acEmail: String?,
                        @SerializedName("cp_company") val cpCompany: String?,
                        @SerializedName("cp_position") val cpPosition: String?,
                        @SerializedName("cp_field") val cpField: String?,
                        @SerializedName("cp_location") val cpLocation: String?,
                        @SerializedName("cp_desc") val cpDesc: String?,
                        @SerializedName("cp_insta") val cpInsta: String?,
                        @SerializedName("cp_linkedin") val cpLinkedIn: String?,
                        @SerializedName("cp_img") val cpAvatar: String?)
}

data class GetAllProject(val success: Boolean, val message: String, val database: ArrayList<Project>?)

data class Project(@SerializedName("pj_id") val pjID: Long,
                   @SerializedName("cp_id") val cpID: Long,
                   @SerializedName("pj_name") val pjName: String?,
                   @SerializedName("pj_desc") val pjDesc: String?,
                   @SerializedName("pj_deadline") val pjDeadline: String?,
                   @SerializedName("pj_img") val pjImage: String?,
                   @SerializedName("pj_created_at") val pjCreatedAt: String?,
                   @SerializedName("pj_updated_at") val pjUpdatedAt: String?)

data class GetAllHire(val success: Boolean, val message: String, val database: ArrayList<Hire>?)

data class Hire(@SerializedName("hr_id") val hrID: Long,
                @SerializedName("cp_id") val cpID: Long,
                @SerializedName("en_id") val enID: Long,
                @SerializedName("pj_id") val pjID: Long,
                @SerializedName("pj_name") val pjName: String?,
                @SerializedName("pj_desc") val pjDesc: String?,
                @SerializedName("pj_deadline") val pjDeadline: String?,
                @SerializedName("pj_img") val pjImage: String?,
                @SerializedName("hr_price") val hrPrice: String?,
                @SerializedName("hr_message") val hrMessage: String?,
                @SerializedName("hr_status") val hrStatus: String?,
                @SerializedName("hr_date_confirm") val hrDateConfirm: String?,
                @SerializedName("hr_created_at") val hrCreatedAt: String?)


data class CompanyRegisterResponse(val success: Boolean, val message: String, val database: CompanyRegisterGHR?) {
    data class CompanyRegisterGHR(@SerializedName("id") val id: Long,
                                   @SerializedName("ac_name") val enName: String,
                                   @SerializedName("ac_email") val enEmail: String,
                                   @SerializedName("ac_phone") val enPhone: String,
                                   @SerializedName("ac_level") val enLevel: String,
                                   @SerializedName("ac_created_at") val acCreatedAt: String,
                                   @SerializedName("ac_updated_at") val acUpdatedAt: String,
                                   @SerializedName("cp_created_at") val cpCreatedAt: String,
                                   @SerializedName("cp_updated_at") val cpUpdatedAt: String)
}

data class EngineerDeleteResponse(val success: Boolean, val message: String)