package com.mohamednader.weatherway.SharedPreferences

interface SharedPrefsSource {

    fun saveFirstTimeSP(value: Boolean)
    fun getFirstTimeSP(): Boolean
    fun saveDataSP(key: String, value: String)
    fun getDataSP(key: String, defaultValue: String): String

}