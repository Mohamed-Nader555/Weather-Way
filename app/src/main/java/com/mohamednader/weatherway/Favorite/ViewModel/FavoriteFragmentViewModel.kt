package com.mohamednader.weatherway.Favorite.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteFragmentViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "FavoriteFragmentViewModel_INFO_TAG"

    //Backing Property
    private var _placesList: MutableStateFlow<List<Place>> =
        MutableStateFlow<List<Place>>(emptyList())
    val placesList: StateFlow<List<Place>>
        get() = _placesList

    init {
        getAllPlacesFromDatabase()
    }

    fun addPlaceToFav(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertPlace(place)
        }
    }


    fun deletePlaceFromFav(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePlace(place)
            getAllPlacesFromDatabase()
        }
    }

    private fun getAllPlacesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getPlacesFromDatabase().collect() { data ->
                _placesList.value = data
            }
        }
    }

}