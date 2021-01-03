package com.istekno.gohipeandroidapp.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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

    @POST("signup/engineer")
    @FormUrlEncoded
    suspend fun registerEngineer(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String
    ): EngineerRegisterResponse

    @DELETE("account/engineer/{id}")
    suspend fun deleteEngineer(
            @Path("id") id: Long
    ): EngineerDeleteResponse

    @PUT("account/engineer/update/{id}")
    @FormUrlEncoded
    suspend fun updateEngineer(
            @Path("id") id: Long,
            @Field("ac_name") name: String,
            @Field("ac_email") email: String,
            @Field("ac_phone") phone: String,
            @Field("ac_password") password: String
    ): EngineerDeleteResponse




    @GET("company/{id}")
    suspend fun getCompanyByID(
            @Path("id") id: Long
    ): CompanyGetByIDResponse

    @GET("project")
    suspend fun getAllProjectCompany(): GetAllProject

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
    ): Call<GeneralResponse>

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

}