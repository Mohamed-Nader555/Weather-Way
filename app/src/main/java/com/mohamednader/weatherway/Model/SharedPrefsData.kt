package com.mohamednader.weatherway.Model

data class SharedPrefsData(
    val firstTime: Boolean,
    val language: String,
    val location: String,
    val tempUnit: String,
    val windUnit: String
)
