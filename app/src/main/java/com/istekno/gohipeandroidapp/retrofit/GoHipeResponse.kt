package com.erdin.arkaandroidtwo.project

import com.google.gson.annotations.SerializedName

data class EngineerResponse(val success: String, val message: String, val data: List<Engineer>?) {
    data class Engineer(@SerializedName("en_id") val enID: String,
                       @SerializedName("ac_name") val enName: String,
                       @SerializedName("en_job_title") val enJobTitle: String,
                       @SerializedName("en_job_type") val enJobType: String,
                       @SerializedName("en_location") val enLocation: String,
                       @SerializedName("en_desc") val enDesc: String,
                       @SerializedName("ac_email") val enEmail: String,
                       @SerializedName("en_ig") val enIG: String,
                       @SerializedName("en_github") val enGithub: String,
                       @SerializedName("en_gitlab") val enGitlab: String)
//                       @SerializedName("en_avatar") val enAvatar: String,
//                       @SerializedName("ability") val enAbilityList: String,
//                       @SerializedName("portfolio") val enPortfolioList: String,
//                       @SerializedName("experience") val enExperienceList: String)
}

data class AbilityResponse(val success: String, val message: String, val data: List<Ability>) {
    data class Ability(@SerializedName("ab_id") val abID: String,
                       @SerializedName("en_id") val enID: String,
                       @SerializedName("ab_name") val abName: String)
}
