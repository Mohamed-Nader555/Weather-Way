package com.mohamednader.weatherway.Model.Pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Current(
    val dt: String,
    val sunrise: String,
    val sunset: String,
    val temp: String,
    @SerializedName("feels_like") val feelsLike: String,
    val pressure: String,
    val humidity: String,
    @SerializedName("dew_point") val dewPoint: String,
    val uvi: String,
    val clouds: String,
    val visibility: String,
    @SerializedName("wind_speed") val windSpeed: String,
    @SerializedName("wind_deg") val windDeg: String,
    val weather: List<Weather>
) : Serializable