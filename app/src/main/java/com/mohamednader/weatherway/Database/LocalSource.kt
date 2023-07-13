package com.mohamednader.weatherway.Database

import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalSource {

    fun getPlacesFromDatabase(): Flow<List<Place>>
    suspend fun insertPlace(place: Place)
    suspend fun deletePlace(place: Place)

    fun getAlarmsFromDatabase(): Flow<List<AlarmItem>>
    suspend fun insertAlarm(alarm: AlarmItem)
    suspend fun deleteAlarm(alarmId: Int)


}