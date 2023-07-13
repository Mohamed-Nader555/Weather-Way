package com.mohamednader.weatherway.Alarm.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlarmFragmentViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "AlarmFragmentViewModel_INFO_TAG"

    //Backing Property
    private var _alarmsList: MutableStateFlow<List<AlarmItem>> =
        MutableStateFlow<List<AlarmItem>>(emptyList())
    val alarmsList: StateFlow<List<AlarmItem>>
        get() = _alarmsList

    private var _notification: MutableStateFlow<String> = MutableStateFlow<String>("")
    val notification: StateFlow<String>
        get() = _notification.asStateFlow()

    init {
        getAllAlarmsFromDatabase()
        getNotificationOption()
    }

    fun addAlarmToFav(alarm: AlarmItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlarm(alarm)
        }
    }


    fun deleteAlarmFromFav(alarm: AlarmItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlarm(alarm)
            getAllAlarmsFromDatabase()
        }
    }

    private fun getAllAlarmsFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAlarmsFromDatabase().collect() { data ->
                _alarmsList.value = data
            }
        }
    }

    private fun getNotificationOption() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.notification, Constants.notification_disable)
            Log.i(TAG, "_notification: $result")
            _notification.value = result
        }
    }

}