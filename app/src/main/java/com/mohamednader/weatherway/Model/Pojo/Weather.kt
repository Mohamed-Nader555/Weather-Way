package com.mohamednader.weatherway.Model.Pojo

import java.io.Serializable

data class Weather(
    val id: String,
    val main: String,
    val description: String,
    val icon: String
) : Serializable
