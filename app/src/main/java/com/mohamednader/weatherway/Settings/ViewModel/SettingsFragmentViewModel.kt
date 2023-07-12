package com.mohamednader.weatherway.Settings.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class SettingsFragmentViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "SettingsFragmentViewModel_INFO_TAG"

    private var _location: MutableStateFlow<String> = MutableStateFlow<String>("")
    val location: StateFlow<String>
        get() = _location.asStateFlow()

    private var _language: MutableStateFlow<String> = MutableStateFlow<String>("")
    val language: StateFlow<String>
        get() = _language.asStateFlow()

    private var _notification: MutableStateFlow<String> = MutableStateFlow<String>("")
    val notification: StateFlow<String>
        get() = _notification.asStateFlow()

    private var _tempUnit: MutableStateFlow<String> = MutableStateFlow<String>("")
    val tempUnit: StateFlow<String>
        get() = _tempUnit.asStateFlow()

    private var _windUnit: MutableStateFlow<String> = MutableStateFlow<String>("")
    val windUnit: StateFlow<String>
        get() = _windUnit.asStateFlow()


    init {
        getLocationAccessOption()
        getLanguageOption()
        getNotificationOption()
        getTempUnitOption()
        getWindUnitOption()
    }

    fun setLocationAccessOption(access: String) {
        repo.saveDataSP(Constants.location, access)
    }

    private fun getLocationAccessOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.location, Constants.location_gps)
            Log.i(TAG, "_location: $result")
            _location.value = result
        }
    }


    fun setLanguageOption(language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveDataSP(Constants.language, language)
        }
    }

    fun getLanguageOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.language,"")
            Log.i(TAG, "language: $result")
            _language.emit(result)
        }
    }


    fun setNotificationOption(notification: String) {
        repo.saveDataSP(Constants.notification, notification)
    }

    private fun getNotificationOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.notification, Constants.notification_disable)
            Log.i(TAG, "_notification: $result")
            _notification.value = result
        }
    }


    fun setTempUnitOption(tempUnit: String) {
        repo.saveDataSP(Constants.tempUnit, tempUnit)
    }

    private fun getTempUnitOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.tempUnit, Constants.tempUnit_celsius)
            Log.i(TAG, "_tempUnit: $result")
            _tempUnit.value = result
        }
    }


    fun setWindUnitOption(windUnit: String) {
        repo.saveDataSP(Constants.windUnit, windUnit)
    }

    private fun getWindUnitOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.windUnit, Constants.windUnit_meter_per_second)
            Log.i(TAG, "_windUnit: $result")
            _windUnit.value = result
        }
    }

}