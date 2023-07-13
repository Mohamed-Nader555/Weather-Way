package com.mohamednader.weatherway.SharedPreferences

class FakeSharedPrefsSource(val sharedPrefs: MutableMap<String, String>): SharedPrefsSource {
    override fun saveFirstTimeSP(value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getFirstTimeSP(): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveDataSP(key: String, value: String) {
        sharedPrefs.put(key, value)
    }

    override fun getDataSP(key: String, defaultValue: String): String {
        val result: String =  sharedPrefs.get(key)?:"Null"
        return result
    }
}