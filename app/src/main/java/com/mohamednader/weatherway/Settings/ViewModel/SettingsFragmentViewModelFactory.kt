package com.mohamednader.weatherway.Settings.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class SettingsFragmentViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "SettingsFragmentViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsFragmentViewModel::class.java)) {
            SettingsFragmentViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}