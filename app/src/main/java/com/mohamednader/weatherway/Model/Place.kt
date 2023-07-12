package com.mohamednader.weatherway.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Place(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val lat: String,
    val lon: String,
    val address: String
) : java.io.Serializable
