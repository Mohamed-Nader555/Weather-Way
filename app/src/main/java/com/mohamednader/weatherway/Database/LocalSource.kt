package com.mohamednader.weatherway.Database

import com.mohamednader.weatherway.Model.Place
import kotlinx.coroutines.flow.Flow

interface LocalSource {

    fun getPlacesFromDatabase(): Flow<List<Place>>
    suspend fun insertPlace(place: Place)
    suspend fun deletePlace(place: Place)

}