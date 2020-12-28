package com.erdin.arkaandroidtwo.remote

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Im5pY29hcmMxMkBnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYiQxMCRyV0xxbElyblZxeVdKdnBNVlE5RjdPd1RXbm03eGxza0RENDFGaVA0VG0vd0p6Sy9zVE4udSIsImxldmVsIjoiRW5naW5lZXIiLCJpYXQiOjE2MDkxODM3OTcsImV4cCI6MTYwOTIwMTc5N30._SH5QGrvxW1qBaSuEC9OmK_jRpClUedZJ-WBjEshnbQ"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}