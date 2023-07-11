package com.mohamednader.weatherway.Splash.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "SplashViewModel_INFO_TAG"

    private var _firstTime: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(true)
    val firstTime : StateFlow<Boolean>
        get() = _firstTime

    init {
        checkFirstTime()
    }

    private fun checkFirstTime(){
        viewModelScope.launch {
            val firstTime = repo.getFirstTimeSP()
            _firstTime.value = firstTime
//            if (firstTime) {
//                repo.saveFirstTimeSP(false)
//            }
        }
    }

}