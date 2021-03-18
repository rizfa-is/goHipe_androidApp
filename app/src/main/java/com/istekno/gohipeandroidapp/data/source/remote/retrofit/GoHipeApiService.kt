package com.istekno.gohipeandroidapp.data.source.remote.retrofit

import com.istekno.gohipeandroidapp.data.source.remote.retrofit.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface GoHipeApiService {

    @POST("login")
    @FormUrlEncoded
    suspend fun loginAccount(
            @Field("email") email: String,
            @Field("password") password: String
    ): LoginResponse




    @GET("engineer")
    suspend fun getAllEngineer(): EngineerGetByIDResponse

    @GET("engineer/{id}")
    suspend fun getEngineerByID(
            @Path("id") id: Long
    ): EngineerGetByIDResponse

    @GET("engineer")
    suspend fun getEngineerByQuery(
            @Query("search") search: String,
            @Query("filter") filter: String
    ): EngineerGetByIDResponse

    @GET("engineer")
    suspend fun getEngineerByFilter(
            @Query("filter") filter: String
    ): EngineerGetByIDResponse

    @POST("signup/engineer")
    @FormUrlEncoded
    suspend fun registerEngineer(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String
    ): EngineerRegisterResponse

    @POST("ability/create")
    @FormUrlEncoded
    suspend fun addABility(
            @Field("en_id") enID: Long,
            @Field("ab_name") abName: String
    ): GeneralResponse

    @Multipart
    @POST("portfolio/create")
    suspend fun addPortfolio(
            @Part("en_id") enID: RequestBody,
            @Part("pr_application") appName: RequestBody,
            @Part("pr_desc") desc: RequestBody,
            @Part("pr_link") link: RequestBody,
            @Part("pr_repo") repo: RequestBody,
            @Part("pr_company") company: RequestBody,
            @Part("pr_role") role: RequestBody,
            @Part image: MultipartBody.Part
    ): GeneralResponse

    @POST("experience/create")
    @FormUrlEncoded
    suspend fun addExperience(
            @Field("en_id") enID: Long,
            @Field("ex_role") exRole: String,
            @Field("ex_company") exCompany: String,
            @Field("ex_desc") exDesc: String,
            @Field("ex_start") exStartDate: String,
            @Field("ex_end") exEndDate: String,
    ): GeneralResponse

    @DELETE("account/engineer/{id}")
    suspend fun deleteEngineer(
            @Path("id") id: Long
    ): GeneralResponse

    @DELETE("ability/{id}")
    suspend fun deleteAbility(
            @Path("id") id: Long
    ): GeneralResponse

    @DELETE("portfolio/{id}")
    suspend fun deletePortfolio(
            @Path("id") id: Long
    ): GeneralResponse

    @DELETE("experience/{id}")
    suspend fun deleteExperience(
            @Path("id") id: Long
    ): GeneralResponse

    @Multipart
    @PUT("account/engineer/update/{id}")
    suspend fun updateEngineerAvatar(
            @Path("id") id: Long,
            @Part image: MultipartBody.Part
    ): GeneralResponse

    @Multipart
    @PUT("account/engineer/update/{id}")
    suspend fun updateEngineer(
            @Path("id") id: Long,
            @Part("ac_name") name: RequestBody,
            @Part("ac_email") email: RequestBody,
            @Part("ac_phone") phone: RequestBody,
            @Part("en_job_title") jType: RequestBody,
            @Part("en_job_type") jTitle: RequestBody,
            @Part("en_location") location: RequestBody,
            @Part("en_desc") desc: RequestBody,
            @Part("en_ig") ig: RequestBody,
            @Part("en_github") github: RequestBody,
            @Part("en_gitlab") gitlab: RequestBody,
            @Part image: MultipartBody.Part
    ): GeneralResponse

    @Multipart
    @PUT("account/engineer/update/{id}")
    suspend fun updateEngineer(
            @Path("id") id: Long,
            @Part("ac_name") name: RequestBody,
            @Part("ac_email") email: RequestBody,
            @Part("ac_phone") phone: RequestBody,
            @Part("en_job_title") jType: RequestBody,
            @Part("en_job_type") jTitle: RequestBody,
            @Part("en_location") location: RequestBody,
            @Part("en_desc") desc: RequestBody,
            @Part("en_ig") ig: RequestBody,
            @Part("en_github") github: RequestBody,
            @Part("en_gitlab") gitlab: RequestBody
    ): GeneralResponse

    @PUT("ability/update/{id}")
    @FormUrlEncoded
    suspend fun updateABility(
            @Path("id") id: Long,
            @Field("en_id") enID: Long,
            @Field("ab_name") abName: String
    ): GeneralResponse

    @Multipart
    @PUT("portfolio/update/{id}")
    suspend fun updatePortfolio(
            @Path("id") id: Long,
            @Part("en_id") enID: RequestBody,
            @Part("pr_application") appName: RequestBody,
            @Part("pr_desc") desc: RequestBody,
            @Part("pr_link") link: RequestBody,
            @Part("pr_repo") repo: RequestBody,
            @Part("pr_company") company: RequestBody,
            @Part("pr_role") role: RequestBody,
            @Part image: MultipartBody.Part
    ): GeneralResponse

    @Multipart
    @PUT("portfolio/update/{id}")
    suspend fun updatePortfolio(
            @Path("id") id: Long,
            @Part("en_id") enID: RequestBody,
            @Part("pr_application") appName: RequestBody,
            @Part("pr_desc") desc: RequestBody,
            @Part("pr_link") link: RequestBody,
            @Part("pr_repo") repo: RequestBody,
            @Part("pr_company") company: RequestBody,
            @Part("pr_role") role: RequestBody
    ): GeneralResponse

    @PUT("experience/update/{id}")
    @FormUrlEncoded
    suspend fun updateExperience(
            @Path("id") id: Long,
            @Field("en_id") enID: Long,
            @Field("ex_role") exRole: String,
            @Field("ex_company") exCompany: String,
            @Field("ex_desc") exDesc: String,
            @Field("ex_start") exStartDate: String,
            @Field("ex_end") exEndDate: String,
    ): GeneralResponse





    @GET("company/{id}")
    suspend fun getCompanyByID(
            @Path("id") id: Long
    ): CompanyGetByIDResponse

    @GET("project")
    suspend fun getAllProjectCompany(): GetAllProject

    @GET("project")
    suspend fun getProjectByQuery(
            @Query("search") query: String
    ): GetAllProject

    @GET("hire")
    suspend fun getAllHire(): GetAllHire

    @POST("signup/company")
    @FormUrlEncoded
    suspend fun registerCompany(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String,
            @Field("company") company: String,
            @Field("position") position: String
    ): CompanyRegisterResponse

    @Multipart
    @POST("project/create")
    suspend fun addProject(
        @Part("cp_id") cpID: RequestBody,
        @Part("pj_name") name: RequestBody,
        @Part("pj_desc") desc: RequestBody,
        @Part("pj_deadline") deadline: RequestBody,
        @Part image: MultipartBody.Part
    ): GeneralResponse

    @POST("hire/create")
    @FormUrlEncoded
    suspend fun addHire(
            @Field("en_id") enID: Long,
            @Field("pj_id") pjID: Long,
            @Field("hr_price") price: String,
            @Field("hr_message") message: String
    ): GeneralResponse

    @DELETE("project/{id}")
    suspend fun deleteProject(
        @Path("id") id: Long
    ): GeneralResponse

    @DELETE("hire/{id}")
    suspend fun deleteHire(
        @Path("id") id: Long
    ): GeneralResponse

    @DELETE("account/company/{id}")
    suspend fun deleteCompany(
            @Path("id") id: Long
    ): GeneralResponse

    @Multipart
    @PUT("account/company/update/{id}")
    suspend fun updateCompany(
            @Path("id") id: Long,
            @Part("ac_name") name: RequestBody,
            @Part("ac_email") email: RequestBody,
            @Part("ac_phone") phone: RequestBody,
            @Part("cp_company") company: RequestBody,
            @Part("cp_position") position: RequestBody,
            @Part("cp_field") field: RequestBody,
            @Part("cp_location") location: RequestBody,
            @Part("cp_desc") desc: RequestBody,
            @Part("cp_insta") ig: RequestBody,
            @Part("cp_linkedin") linkedin: RequestBody,
            @Part image: MultipartBody.Part
    ): GeneralResponse

    @Multipart
    @PUT("account/company/update/{id}")
    suspend fun updateCompany(
            @Path("id") id: Long,
            @Part("ac_name") name: RequestBody,
            @Part("ac_email") email: RequestBody,
            @Part("ac_phone") phone: RequestBody,
            @Part("cp_company") company: RequestBody,
            @Part("cp_position") position: RequestBody,
            @Part("cp_field") field: RequestBody,
            @Part("cp_location") location: RequestBody,
            @Part("cp_desc") desc: RequestBody,
            @Part("cp_insta") ig: RequestBody,
            @Part("cp_linkedin") linkedin: RequestBody
    ): GeneralResponse

    @PUT("hire/update/{id}")
    @FormUrlEncoded
    suspend fun updateHireStatus(
            @Path("id") id: Long,
            @Field("hr_status") hrStatus: String
    ): GeneralResponse

    @PUT("hire/update/{id}")
    @FormUrlEncoded
    suspend fun updateHiring(
            @Path("id") id: Long,
            @Field("en_id") enID: Long,
            @Field("pj_id") pjID: Long,
            @Field("hr_price") hrPrice: String,
            @Field("hr_message") hrMessage: String,
            @Field("hr_status") hrStatus: String
    ): GeneralResponse

    @Multipart
    @PUT("project/update/{id}")
    suspend fun updateProject(
            @Path("id") id: Long,
            @Part("pj_name") pjName: RequestBody,
            @Part("pj_desc") pjDesc: RequestBody,
            @Part("pj_deadline") pjDeadline: RequestBody
    ): GeneralResponse

    @Multipart
    @PUT("project/update/{id}")
    suspend fun updateProject(
            @Path("id") id: Long,
            @Part("pj_name") pjName: RequestBody,
            @Part("pj_desc") pjDesc: RequestBody,
            @Part("pj_deadline") pjDeadline: RequestBody,
            @Part image: MultipartBody.Part
    ): GeneralResponse

}