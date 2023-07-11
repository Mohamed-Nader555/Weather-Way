package com.mohamednader.weatherway.Splash.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModel
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class SplashViewModelFactory (private val repo: RepositoryInterface) :
    ViewModelProvider.Factory{

    private val TAG = "SplashViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            SplashViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}