package com.mohamednader.weatherway.Utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class CheckInternetConnection(private val context: Context) {

    fun isConnected(): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.activeNetworkInfo
        return info?.state == NetworkInfo.State.CONNECTED
    }
}