package com.mohamednader.weatherway.Splash.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.Utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "SplashViewModel_INFO_TAG"

    private var _firstTime: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(true)
    val firstTime : StateFlow<Boolean>
        get() = _firstTime

    private var _language: MutableStateFlow<String> = MutableStateFlow<String>("")
    val language: StateFlow<String>
        get() = _language.asStateFlow()


    init {
        getLanguageOption()
        checkFirstTime()
    }

    private fun checkFirstTime(){
        viewModelScope.launch(Dispatchers.IO) {
            val firstTime = repo.getFirstTimeSP()
            _firstTime.value = firstTime
        }
    }

    private fun getLanguageOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.language,"")
            Log.i(TAG, "language: $result")
            _language.emit(result)
        }
    }


}