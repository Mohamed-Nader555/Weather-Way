package com.mohamednader.weatherway.Network

import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("/data/2.5/onecall")
    suspend fun getWeatherData(@QueryMap params: MutableMap<String, String>): Response<WeatherResponse>

}