package com.mohamednader.weatherway.Model.Pojo

import com.google.gson.annotations.SerializedName

data class Alert(
    @SerializedName("sender_name") val senderName: String,
    val event: String,
    val start: String,
    val end: String,
    val description: String,
    val tags: List<String>
)
