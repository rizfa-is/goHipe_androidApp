package com.erdin.arkaandroidtwo.remote

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Im5pY29hcmMxMkBnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYiQxMCRyV0xxbElyblZxeVdKdnBNVlE5RjdPd1RXbm03eGxza0RENDFGaVA0VG0vd0p6Sy9zVE4udSIsImxldmVsIjoiRW5naW5lZXIiLCJpYXQiOjE2MDkxNjAzMjQsImV4cCI6MTYwOTE3ODMyNH0.Ip16JdUG6ZSbCyp9aJoS2Q28RPuXNMValbYidWh3zwo"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }

}