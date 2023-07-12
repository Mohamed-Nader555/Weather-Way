package com.mohamednader.weatherway.Database

import androidx.room.*
import com.mohamednader.weatherway.Model.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAllPlaces() : Flow<List<Place>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlace(place: Place) : Long

    @Delete
    suspend fun deletePlace(place: Place) : Int
}