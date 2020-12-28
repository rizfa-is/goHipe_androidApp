package com.istekno.gohipeandroidapp.retrofit

import retrofit2.http.*

interface GoHipeApiService {

    @GET("engineer/1")
    suspend fun getAllEngineer(): EngineerResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("signup/engineer")
    suspend fun registerEngineer(
        @Body infoRegister: EngineerRegisterModelRequest
    ): EngineerRegisterResponse

    @POST("signup/company")
    suspend fun registerCompany(): LoginResponse
}