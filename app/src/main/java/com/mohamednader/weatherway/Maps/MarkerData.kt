package com.mohamednader.weatherway.Maps

data class MarkerData(
    var latitude: Double,
    val longitude: Double,
    val title: String,
    val snippet: String,
    val iconResId: Int
)
