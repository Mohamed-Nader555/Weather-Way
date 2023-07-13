package com.mohamednader.weatherway.Home.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.Utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "HomeViewModel_INFO_TAG"


    private var _weatherDataOffline: MutableStateFlow<List<WeatherResponse>> =
        MutableStateFlow<List<WeatherResponse>>(emptyList())
    val weatherDataOffline: StateFlow<List<WeatherResponse>>
        get() = _weatherDataOffline


    private var _weatherData: MutableStateFlow<ApiState> =
        MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherData : StateFlow<ApiState>
        get() = _weatherData

    private var _language: MutableStateFlow<String> = MutableStateFlow<String>("")
    val language: StateFlow<String>
        get() = _language.asStateFlow()

    private var _tempUnit: MutableStateFlow<String> = MutableStateFlow<String>("")
    val tempUnit: StateFlow<String>
        get() = _tempUnit.asStateFlow()

    private var _windUnit: MutableStateFlow<String> = MutableStateFlow<String>("")
    val windUnit: StateFlow<String>
        get() = _windUnit.asStateFlow()

    private var _location: MutableStateFlow<String> = MutableStateFlow<String>("")
    val location: StateFlow<String>
        get() = _location.asStateFlow()

    init {
        getLocationAccessOption()
        getLanguageOption()
        getTempUnitOption()
        getWindUnitOption()
    }

    private fun getLanguageOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.language,"")
            Log.i(TAG, "language: $result")
            _language.emit(result)
        }
    }

    private fun getLocationAccessOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.location, Constants.location_gps)
            Log.i(TAG, "_location: $result")
            _location.value = result
        }
    }



    private fun getTempUnitOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.tempUnit, Constants.tempUnit_celsius)
            Log.i(TAG, "_tempUnit: $result")
            _tempUnit.value = result
        }
    }


    private fun getWindUnitOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.windUnit, Constants.windUnit_meter_per_second)
            Log.i(TAG, "_windUnit: $result")
            _windUnit.value = result
        }
    }


    fun getWeatherDataFromNetwork(params : MutableMap<String, String>){
        viewModelScope.launch(Dispatchers.IO){
            Log.i(TAG, "getWeatherDataFromNetwork: ")
            repo.getWeatherDataNetwork(params)
                .catch { e -> _weatherData.value = ApiState.Failure(e) }
                .collect{ data -> _weatherData.value = ApiState.Success(data)
                }
        }
    }
}