package com.mohamednader.weatherway.Model.Pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Hourly(
    val dt: String,
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
    @SerializedName("wind_gust") val windGust: String,
    val weather: List<Weather>,
    val pop: String
) : Serializable
