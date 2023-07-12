package com.mohamednader.weatherway.Favorite.FavoriteResult.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface

class FavoriteResultViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    private val TAG = "FavoriteResultViewModelFactory_INFO_TAG"

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteResultViewModel::class.java)) {
            FavoriteResultViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}