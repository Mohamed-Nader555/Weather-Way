package com.mohamednader.weatherway.ConfigSetup.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class ConfigSetupViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "ConfigSetupViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ConfigSetupViewModel::class.java)) {
            ConfigSetupViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}