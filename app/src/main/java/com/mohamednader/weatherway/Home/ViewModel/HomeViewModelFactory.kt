package com.mohamednader.weatherway.Home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class HomeViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "HomeViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }

}