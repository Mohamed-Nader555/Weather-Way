package com.mohamednader.weatherway.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place

@Database(entities = arrayOf(Place::class, AlarmItem::class), version = 5)
abstract class WeatherDatabase : RoomDatabase() {

    private val TAG = "FavoriteDatabase_INFO_TAG"

    abstract fun getFavoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(ctx: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDatabase::class.java, "favorites_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}