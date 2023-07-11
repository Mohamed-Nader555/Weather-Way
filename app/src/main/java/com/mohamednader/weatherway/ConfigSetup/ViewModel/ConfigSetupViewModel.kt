package com.mohamednader.weatherway.ConfigSetup.ViewModel

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

class ConfigSetupViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "ConfigSetupViewModel_INFO_TAG"

    fun saveFirstTime(){
        repo.saveFirstTimeSP(false)
    }

    fun saveLocationAccessOption(access: String){
        repo.saveDataSP(Constants.location, access)
    }

    fun saveNotificationOption(option: String){
        repo.saveDataSP(Constants.notification, option)
    }

    fun saveLanguageOption(language: String){
        repo.saveDataSP(Constants.language, language)
    }

    fun saveTempUnitOption(tempUnit: String){
        repo.saveDataSP(Constants.tempUnit, tempUnit)
    }

    fun saveWindUnitOption(windUnit: String){
        repo.saveDataSP(Constants.windUnit, windUnit)
    }

}