package com.mohamednader.weatherway.MainHome.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModel
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class MainHomeViewModelFactory (private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "MainHomeViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainHomeViewModel::class.java)) {
            MainHomeViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}