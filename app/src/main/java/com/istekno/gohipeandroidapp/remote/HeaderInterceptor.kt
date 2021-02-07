package com.istekno.gohipeandroidapp.remote

import android.content.Context
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response = chain.run {

        val goHipePreferences = GoHipePreferences(context)
        var tokenAuth = ""

        if (goHipePreferences.getCompanyPreference().level == "Company" ) {
            tokenAuth = goHipePreferences.getCompanyPreference().token!!
        } else if (goHipePreferences.getEngineerPreference().level == "Engineer") {
            tokenAuth = goHipePreferences.getEngineerPreference().token!!
        }

        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}