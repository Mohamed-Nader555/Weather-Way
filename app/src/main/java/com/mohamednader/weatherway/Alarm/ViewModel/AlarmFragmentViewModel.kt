package com.mohamednader.weatherway.Alarm.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlarmFragmentViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "AlarmFragmentViewModel_INFO_TAG"

    //Backing Property
    private var _alarmsList: MutableStateFlow<List<AlarmItem>> =
        MutableStateFlow<List<AlarmItem>>(emptyList())
    val alarmsList: StateFlow<List<AlarmItem>>
        get() = _alarmsList


    init {
        getAllAlarmsFromDatabase()
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

}