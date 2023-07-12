package com.mohamednader.weatherway.Favorite.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import com.mohamednader.weatherway.Settings.ViewModel.SettingsFragmentViewModel

class FavoriteFragmentViewModelFactory (private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "FavoriteFragmentViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteFragmentViewModel::class.java)) {
            FavoriteFragmentViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}