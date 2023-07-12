package com.mohamednader.weatherway.Favorite.View

import com.mohamednader.weatherway.Model.Place

interface OnFavClickListener {

    fun onClickPlace(place: Place)
    fun onClickDeletePlace(place: Place)

}