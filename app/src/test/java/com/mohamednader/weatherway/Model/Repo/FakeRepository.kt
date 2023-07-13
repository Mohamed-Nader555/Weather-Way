package com.mohamednader.weatherway.Model.Repo

import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow

class FakeRepository : RepositoryInterface {

    private var _map: MutableMap<String, String> = mutableMapOf(
        "windUnit" to "",
        "location" to "",
        "language" to "",
        "notification" to "enabled",
        "tempUnit" to "",
    )
    val map: Map<String, String>
        get() = _map


    override fun getDataSP(key: String, defaultValue: String): String {
        return _map.get(key) ?: defaultValue
    }

    override fun saveDataSP(key: String, value: String) {
        _map.put(key, value)
    }

    override suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override fun getPlacesFromDatabase(): Flow<List<Place>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertPlace(place: Place) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlace(place: Place) {
        TODO("Not yet implemented")
    }

    override fun getAlarmsFromDatabase(): Flow<List<AlarmItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlarm(alarm: AlarmItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarm: AlarmItem) {
        TODO("Not yet implemented")
    }

    override fun saveFirstTimeSP(value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getFirstTimeSP(): Boolean {
        TODO("Not yet implemented")
    }
}