package com.mohamednader.weatherway.Utilities

import com.mohamednader.weatherway.Model.Place

object Constants {
    //Shared Preferences Keys
    const val firstTime = "first_time"
    const val location= "location"
    const val notification= "notification"
    const val language= "language"
    const val tempUnit= "temp_unit"
    const val windUnit= "wind_unit"


    //Shared Preferences Values
    const val location_gps = "gps"
    const val location_map = "map"
    const val notification_enable = "enable"
    const val notification_disable = "disable"
    const val language_arabic = "ar"
    const val language_english = "en"
    const val tempUnit_celsius = "celsius"
    const val tempUnit_kelvin = "kelvin"
    const val tempUnit_fahrenheit = "fahrenheit"
    const val windUnit_meter_per_second = "meter_per_second"
    const val windUnit_mile_per_hour = "mile_per_hour"

    //API Values
    const val app_id = "3e6f518ff7d5d207d9c201f3c1ec34f6"
    const val standard = "standard"
    const val metric  = "metric"
    const val imperial = "imperial"

    var tempUnitForAll = ""
    var tempUnitValueForAll = ""
    var windUnitValueForAll = ""
    var languageValueForAll = ""

    //Alarm

    const val alarmIdKey = "ALARM_ID"
    const val notificationIdKey = "NOTIFICATION_ID"
    const val notificationIdPrefix = "ALARM_NOTIFICATION_"

    lateinit var placeToAlarm : Place


    const val sourceFragment = "Source_Fragment"
    const val homeFragment = "Home_Fragment"
    const val favoriteFragment = "Favorite_Fragment"





}