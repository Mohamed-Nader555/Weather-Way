package com.mohamednader.weatherway.Model.Pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Daily(
    val dt: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerializedName("moon_phase") val moonPhase: String,
    val temp: Temp,
    @SerializedName("feels_like") val feelsLike: FeelsLike,
    val pressure: String,
    val humidity: String,
    @SerializedName("dew_point") val dewPoint: String,
    @SerializedName("wind_speed") val windSpeed: String,
    @SerializedName("wind_deg") val windDeg: String,
    @SerializedName("wind_gust") val windGust: String,
    val weather: List<Weather>,
    val clouds: String,
    val pop: String,
    val uvi: String
) : Serializable