package com.mohamednader.weatherway.Model.Pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherResponse(
    val lat: String,
    val lon: String,
    val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: String,
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
    val alerts: List<Alert>?
) : Serializable
