package com.mohamednader.weatherway.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class AlarmItem(
    @PrimaryKey var id: Int,
    val address: String,
    val time: String
) : java.io.Serializable