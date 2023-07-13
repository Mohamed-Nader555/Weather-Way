package com.mohamednader.weatherway.Model.Repo

import android.util.Log
import com.mohamednader.weatherway.Database.LocalSource
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import com.mohamednader.weatherway.Network.RemoteSource
import com.mohamednader.weatherway.SharedPreferences.SharedPrefsSource
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    remoteSource: RemoteSource, localSource: LocalSource, sharedPrefsSource: SharedPrefsSource
) : RepositoryInterface {

    private val TAG = "Repository_INFO_TAG"


    private val remoteSource: RemoteSource
    private val localSource: LocalSource
    private val sharedPrefsSource: SharedPrefsSource

    init {
        this.remoteSource = remoteSource
        this.localSource = localSource
        this.sharedPrefsSource = sharedPrefsSource
    }

    companion object {
        private var repo: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource,
            localSource: LocalSource,
            sharedPrefsSource: SharedPrefsSource
        ): Repository {
            return repo ?: synchronized(this) {
                val instance = Repository(remoteSource, localSource, sharedPrefsSource)
                repo = instance
                instance
            }
        }
    }


    //API

    override suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse> {
        return remoteSource.getWeatherDataNetwork(params)
    }


    //SHARED PREFERENCES

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
        Log.i(TAG, "getDataSP: HI")
        return sharedPrefsSource.getDataSP(key, defaultValue)
    }

    //DATABASE

    override fun getPlacesFromDatabase(): Flow<List<Place>> {
        return localSource.getPlacesFromDatabase()
    }

    override suspend fun insertPlace(place: Place) {
        localSource.insertPlace(place)
    }

    override suspend fun deletePlace(place: Place) {
        localSource.deletePlace(place)
    }

    override fun getAlarmsFromDatabase(): Flow<List<AlarmItem>> {
        return localSource.getAlarmsFromDatabase()
    }

    override suspend fun insertAlarm(alarm: AlarmItem) {
        localSource.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: AlarmItem) {
        localSource.deleteAlarm(alarm)
    }

}