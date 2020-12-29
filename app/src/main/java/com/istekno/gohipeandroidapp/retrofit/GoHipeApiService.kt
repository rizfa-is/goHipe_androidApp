package com.istekno.gohipeandroidapp.retrofit

import retrofit2.http.*

interface GoHipeApiService {

    @POST("login")
    @FormUrlEncoded
    suspend fun loginAccount(
            @Field("email") email: String,
            @Field("password") password: String
    ): LoginResponse

    @GET("engineer/1")
    suspend fun getAllEngineer(): EngineerResponse

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

    @POST("signup/company")
    suspend fun registerCompany(): LoginResponse

}