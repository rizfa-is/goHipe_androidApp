package com.erdin.arkaandroidtwo.project

import retrofit2.http.GET
import retrofit2.http.POST

interface GoHipeApiService {

    @GET("engineer")
    suspend fun getAllEngineer(): EngineerResponse

    @GET("ability/1")
    suspend fun getAllAbility(): AbilityResponse

}