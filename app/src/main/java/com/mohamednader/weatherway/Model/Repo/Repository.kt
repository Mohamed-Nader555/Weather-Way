package com.mohamednader.weatherway.Model.Repo

import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import com.mohamednader.weatherway.Network.RemoteSource
import com.mohamednader.weatherway.SharedPreferences.SharedPrefsSource
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    remoteSource: RemoteSource /*, localSource: LocalSource*/, sharedPrefsSource: SharedPrefsSource
) : RepositoryInterface {

    private val TAG = "Repository_INFO_TAG"


    private val remoteSource: RemoteSource

    //private val localSource: LocalSource
    private val sharedPrefsSource: SharedPrefsSource

    init {
        this.remoteSource = remoteSource
        //  this.localSource = localSource
        this.sharedPrefsSource = sharedPrefsSource
    }

    companion object {
        private var repo: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource,
            //    localSource: LocalSource
            sharedPrefsSource: SharedPrefsSource
        ): Repository {
            return repo ?: synchronized(this) {
                val instance = Repository(remoteSource/*, localSource*/, sharedPrefsSource)
                repo = instance
                instance
            }
        }
    }


    override suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse> {
        return remoteSource.getWeatherDataNetwork(params)
    }

    override fun saveFirstTimeSP(value: Boolean) {
        sharedPrefsSource.saveFirstTimeSP(value)
    }

    override fun getFirstTimeSP(): Boolean {
        return sharedPrefsSource.getFirstTimeSP()
    }

    override fun saveDataSP(key: String, value: String) {
        sharedPrefsSource.saveDataSP(key, value)
    }

    override fun getDataSP(key: String, defaultValue: String): String {
        return sharedPrefsSource.getDataSP(key, defaultValue)
    }

}