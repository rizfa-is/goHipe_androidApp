package com.erdin.arkaandroidtwo.remote

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imh5dWdhSGluMDJAZ21haWwuY29tIiwicGFzc3dvcmQiOiIkMmIkMTAkMFRQWjNTb0xNZkJoWlZZLzV0UHpwLlh2WnhJS2pUWk5KNU9VVEhRNEZKaE5WQjF2eVoxVVMiLCJsZXZlbCI6IkVuZ2luZWVyIiwiaWF0IjoxNjA5MjA4NDMyLCJleHAiOjE2MDkyMjY0MzJ9.vcgGWJEWjhw5KcL1KOXreXpeYlP3t8yvzGSEC_W7kl4"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}