package com.mohamednader.weatherway.Network

import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse>
}