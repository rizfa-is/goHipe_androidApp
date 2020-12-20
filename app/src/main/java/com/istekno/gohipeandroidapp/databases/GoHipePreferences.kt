package com.istekno.gohipeandroidapp.databases

import android.content.Context
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.models.EngineerModel

class GoHipePreferences(context: Context) {

    companion object {
        private const val ENG_PREF_NAME = "eng_pref"
        private const val COMP_PREF_NAME = "comp_pref"

        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val PHONE = "phone"
        private const val COMPANY = "company"
        private const val POSITION = "position"
        private const val LOGIN = "isLogin"
    }

    private val engineerPreferences = context.getSharedPreferences(ENG_PREF_NAME, Context.MODE_PRIVATE)
    private val companyPreferences = context.getSharedPreferences(COMP_PREF_NAME, Context.MODE_PRIVATE)

    fun setEngineerPreference(value: EngineerModel) {
        val editor = engineerPreferences.edit()
        editor.putString(EMAIL, value.email)
        editor.putString(PASSWORD, value.password)
        editor.putLong(PHONE, value.phone)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.apply()
    }

    fun getEngineerPreference(): EngineerModel {
        val model = EngineerModel()
        model.email = engineerPreferences.getString(EMAIL, "")
        model.password = engineerPreferences.getString(PASSWORD, "")
        model.phone = engineerPreferences.getLong(PHONE, 0)
        model.isLogin = engineerPreferences.getBoolean(LOGIN, false)

        return model
    }

    fun setCompanyPreference(value: CompanyModel) {
        val editor = companyPreferences.edit()
        editor.putString(EMAIL, value.email)
        editor.putString(PASSWORD, value.password)
        editor.putString(COMPANY, value.company)
        editor.putString(POSITION, value.position)
        editor.putLong(PHONE, value.phone)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.apply()
    }

    fun getCompanyPreference(): CompanyModel {
        val model = CompanyModel()
        model.email = companyPreferences.getString(EMAIL, "")
        model.password = companyPreferences.getString(PASSWORD, "")
        model.company = companyPreferences.getString(COMPANY, "")
        model.position = companyPreferences.getString(POSITION, "")
        model.phone = companyPreferences.getLong(PHONE, 0)
        model.isLogin = companyPreferences.getBoolean(LOGIN, false)

        return model
    }
}