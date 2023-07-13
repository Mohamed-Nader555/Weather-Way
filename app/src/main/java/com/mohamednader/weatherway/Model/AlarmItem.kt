package com.mohamednader.weatherway.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "alarm")
data class AlarmItem(
    @PrimaryKey var id: Int,
    val address: String,
    val time: String
) : Serializable