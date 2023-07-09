package com.mohamednader.weatherway.Network

import android.util.Log
import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class ApiClient : RemoteSource {

    private val TAG = "ApiClient_INFO_TAG"

    val apiService: ApiService by lazy {
        RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                val temp = ApiClient()
                instance = temp
                temp
            }
        }
    }


    override suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse> {
        val response = apiService.getWeatherData(params)
        val weatherData: Flow<WeatherResponse>
        Log.i(TAG, "getWeatherFromNetwork: Open")
        if (response.isSuccessful) {
            weatherData = flowOf(response.body()!!)
            Log.i(TAG, "getProductsFromNetwork: Done")
        } else {
            weatherData = emptyFlow()
            Log.i(TAG, "getProductsFromNetwork: ${response.errorBody().toString()}")
        }
        return weatherData
    }


}