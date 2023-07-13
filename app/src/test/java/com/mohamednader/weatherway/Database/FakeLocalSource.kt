package com.mohamednader.weatherway.Database

import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

class FakeLocalSource(var places : MutableList<Place>? = mutableListOf()) : LocalSource {
    override fun getPlacesFromDatabase(): Flow<List<Place>> {
        places?.let { places!!.toList().asFlow() }
        return flowOf()
    }

    override suspend fun insertPlace(place: Place) {
        places?.add(place)
    }

    override suspend fun deletePlace(place: Place) {
        places?.remove(place)
    }

    override fun getAlarmsFromDatabase(): Flow<List<AlarmItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlarm(alarm: AlarmItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarmId : Int) {
        TODO("Not yet implemented")
    }
}