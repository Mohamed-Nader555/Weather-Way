package com.mohamednader.weatherway.Database

import android.content.Context
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(context: Context) : LocalSource {

    private val TAG = "ProductDatabase_INFO_TAG"

    private val favoriteDAO: FavoriteDao by lazy {
        val db: WeatherDatabase = WeatherDatabase.getInstance(context)
        db.getFavoriteDao()
    }


    override fun getPlacesFromDatabase(): Flow<List<Place>> {
        return favoriteDAO.getAllPlaces()
    }

    override suspend fun insertPlace(place: Place) {
        favoriteDAO.insertPlace(place)
    }

    override suspend fun deletePlace(place: Place) {
        favoriteDAO.deletePlace(place)
    }

    override fun getAlarmsFromDatabase(): Flow<List<AlarmItem>> {
        return favoriteDAO.getAllAlarms()
    }



    override suspend fun insertAlarm(alarm: AlarmItem) {
        favoriteDAO.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: AlarmItem) {
        favoriteDAO.deleteAlarm(alarm)
    }

    

}
