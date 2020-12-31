package com.istekno.gohipeandroidapp.utility

import android.content.Context
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.models.EngineerModel

class GoHipePreferences(context: Context) {

    companion object {
        private const val ENG_PREF_NAME = "eng_pref"
        private const val COMP_PREF_NAME = "comp_pref"

        private const val ACID = "acID"
        private const val COMPID = "compID"
        private const val ENGID = "engID"
        private const val TOKEN = "token"
        private const val LOGIN = "isLogin"
    }

    private val engineerPreferences = context.getSharedPreferences(ENG_PREF_NAME, Context.MODE_PRIVATE)
    private val companyPreferences = context.getSharedPreferences(COMP_PREF_NAME, Context.MODE_PRIVATE)

    fun setEngineerPreference(value: EngineerModel) {
        val editor = engineerPreferences.edit()
        editor.putString(ACID, value.acID)
        editor.putString(ENGID, value.engID)
        editor.putString(TOKEN, value.token)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.apply()
    }

    fun getEngineerPreference(): EngineerModel {
        val model = EngineerModel()
        model.acID = engineerPreferences.getString(ACID, "")
        model.engID = engineerPreferences.getString(ENGID, "")
        model.token = engineerPreferences.getString(TOKEN, "")
        model.isLogin = engineerPreferences.getBoolean(LOGIN, false)

        return model
    }

    fun setCompanyPreference(value: CompanyModel) {
        val editor = companyPreferences.edit()
        editor.putString(ACID, value.acID)
        editor.putString(COMPID, value.compID)
        editor.putString(TOKEN, value.token)
        editor.putBoolean(LOGIN, value.isLogin)
        editor.apply()
    }

    fun getCompanyPreference(): CompanyModel {
        val model = CompanyModel()
        model.acID = companyPreferences.getString(ACID, "")
        model.compID = companyPreferences.getString(COMPID, "")
        model.token = companyPreferences.getString(TOKEN, "")
        model.isLogin = companyPreferences.getBoolean(LOGIN, false)

        return model
    }
}