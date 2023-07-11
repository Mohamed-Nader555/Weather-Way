package com.mohamednader.weatherway.SharedPreferences

import android.content.Context
import com.mohamednader.weatherway.Utilities.Constants

class ConcreteSharedPrefsSource(context: Context) : SharedPrefsSource {

    private val sharedPreferences = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()
    override fun saveFirstTimeSP(value: Boolean) {
        editor.putBoolean(Constants.firstTime, value).apply()
    }

    override fun getFirstTimeSP(): Boolean {
        return sharedPreferences.getBoolean(Constants.firstTime, true)
    }

    override fun saveDataSP(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    override fun getDataSP(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: "null"
    }

}