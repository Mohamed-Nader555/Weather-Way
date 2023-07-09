package com.mohamednader.weatherway.Model.Pojo

import java.io.Serializable

data class Temp(
    val day: String,
    val min: String,
    val max: String,
    val night: String,
    val eve: String,
    val morn: String
) : Serializable
