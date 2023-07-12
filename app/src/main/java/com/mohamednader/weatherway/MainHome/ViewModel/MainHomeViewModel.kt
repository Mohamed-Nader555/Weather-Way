package com.mohamednader.weatherway.MainHome.ViewModel

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

class MainHomeViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "MainHomeViewModel_INFO_TAG"

    private var _language: MutableStateFlow<String> = MutableStateFlow<String>("null")
    val language: StateFlow<String>
        get() = _language



    fun getLanguage() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDataSP(Constants.language, Constants.language_english)
            Log.i(TAG, "language: $result")
            _language.value = result
        }
    }

}