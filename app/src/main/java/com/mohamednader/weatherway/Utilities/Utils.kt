package com.mohamednader.weatherway.Utilities

import com.mohamednader.weatherway.R

fun getWeatherImageDrawable(imgName: String): Int {
    when (imgName) {
        "01d" -> return R.drawable.sun
        "01n" -> return R.drawable.moon
        "02d" -> return R.drawable.cloud_day
        "02n" -> return R.drawable.cloud_night
        "03d", "03n" -> return R.drawable.cloud
        "04d", "04n" -> return R.drawable.clouds
        "09d", "09n", "10d", "10n" -> return R.drawable.rain
        "11d", "11n" -> return R.drawable.storm
        "13d", "13n" -> return R.drawable.snowy
        "50d", "50n" -> return R.drawable.tornado
    }
    return R.drawable.cloud
}