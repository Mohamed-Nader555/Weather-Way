package com.mohamednader.weatherway.Network

import com.mohamednader.weatherway.Model.Pojo.WeatherResponse

sealed class ApiState {
    class Success(val data: WeatherResponse) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}
