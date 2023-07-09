package com.mohamednader.weatherway.Model.Pojo

import java.io.Serializable

data class FeelsLike(
    val day: String,
    val night: String,
    val eve: String,
    val morn: String,
) : Serializable
