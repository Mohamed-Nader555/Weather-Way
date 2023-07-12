package com.mohamednader.weatherway.Favorite.FavoriteResult.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.Utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteResultViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "FavoriteResultViewModel_INFO_TAG"

    private var _weatherData: MutableStateFlow<ApiState> =
        MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherData : StateFlow<ApiState>
        get() = _weatherData

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