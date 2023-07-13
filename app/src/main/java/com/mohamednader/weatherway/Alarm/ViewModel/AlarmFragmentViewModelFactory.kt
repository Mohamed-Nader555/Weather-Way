package com.mohamednader.weatherway.Alarm.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Favorite.ViewModel.FavoriteFragmentViewModel
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class AlarmFragmentViewModelFactory (private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "AlarmFragmentViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlarmFragmentViewModel::class.java)) {
            AlarmFragmentViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}