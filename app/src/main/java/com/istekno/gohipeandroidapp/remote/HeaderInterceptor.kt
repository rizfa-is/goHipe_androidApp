package com.erdin.arkaandroidtwo.remote

import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor() : Interceptor {

    private lateinit var goHipePreferences: GoHipePreferences
    private var tokenAuth = ""
    var role = "Random"

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {

        if (role == "Company") {
            tokenAuth = goHipePreferences.getCompanyPreference().token!!
        } else if (role == "Engineer") {
            tokenAuth = goHipePreferences.getEngineerPreference().token!!
        }

        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}