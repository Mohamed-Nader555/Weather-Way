package com.mohamednader.weatherway.Home.View.Dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mohamednader.weatherway.Model.Pojo.Daily
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.Utilities.convertMeterPerSecToMilePerHour
import com.mohamednader.weatherway.Utilities.convertMilePerHourToMeterPerSec
import com.mohamednader.weatherway.Utilities.getWeatherImageDrawable
import com.mohamednader.weatherway.databinding.FragmentDailyResultDialogBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyResultDialogFragment(val dailyItem: Daily, val address: String) : DialogFragment() {

    private lateinit var binding: FragmentDailyResultDialogBinding
    val TAG = "DailyResultDialogFragment_INFO_TAG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyResultDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogClose.setOnClickListener {
            dismiss()
        }
        fillData(dailyItem, address)

    }


    private fun fillData(dailyItem: Daily, address: String) {

        val dateFormat: DateFormat = SimpleDateFormat("EEE dd-MM-yyyy", Locale.getDefault())
        val date: Date = Date(dailyItem.dt.toLong() * 1000)

        val img = dailyItem.weather[0].icon
        val des = dailyItem.weather[0].description
        val temp = "${dailyItem.temp.day.toFloat().roundToInt()}${Constants.tempUnitForAll}"
        val humidity = "${dailyItem.humidity.toFloat().roundToInt()}%"
        val wind = dailyItem.windSpeed.toDouble()

        lateinit var windUnitText: String

        when (Constants.tempUnitValueForAll) {
            Constants.standard -> {
                windUnitText = when(Constants.windUnitValueForAll){
                    Constants.windUnit_meter_per_second -> "${wind.roundToInt()}m/s"
                    Constants.windUnit_mile_per_hour -> "${convertMeterPerSecToMilePerHour(wind).roundToInt()}m/h"
                    else -> ""
                }
                Log.i(TAG, "fillData1: $windUnitText")
            }
            Constants.metric ->{
                windUnitText = when(Constants.windUnitValueForAll){
                    Constants.windUnit_meter_per_second -> "${wind.roundToInt()}m/s"
                    Constants.windUnit_mile_per_hour -> "${convertMeterPerSecToMilePerHour(wind).roundToInt()}m/h"
                    else -> ""
                }
                Log.i(TAG, "fillData2: $windUnitText")
            }
            Constants.imperial ->{
                windUnitText = when(Constants.windUnitValueForAll){
                    Constants.windUnit_meter_per_second -> "${convertMilePerHourToMeterPerSec(wind).roundToInt()}m/s"
                    Constants.windUnit_mile_per_hour -> "${wind.roundToInt()}m/h"
                    else -> ""
                }
                Log.i(TAG, "fillData3: $windUnitText")
            }
        }


        val windSpeed = windUnitText
        val clouds = "${dailyItem.clouds.toFloat().roundToInt()}%"
        val minTemp = "${dailyItem.temp.min.toFloat().roundToInt()}${Constants.tempUnitForAll}°"
        val maxTemp = "${dailyItem.temp.max.toFloat().roundToInt()}${Constants.tempUnitForAll}°"
        val mornTemp = "${dailyItem.temp.eve.toFloat().roundToInt()}${Constants.tempUnitForAll}°"
        val eveTemp = "${dailyItem.temp.morn.toFloat().roundToInt()}${Constants.tempUnitForAll}°"

        binding.dialogAddress.text = address
        binding.dialogDate.text = dateFormat.format(date).toString()
        binding.dialogImage.setImageResource(getWeatherImageDrawable(img))
        binding.dialogDes.text = des
        binding.dialogTemp.text = temp
        binding.dialogMaxTemp.text = maxTemp
        binding.dialogMinTemp.text = minTemp
        binding.dialogMorning.text = mornTemp
        binding.dialogEvening.text = eveTemp
        binding.dialogHumidity.text = humidity
        binding.dialogWindSpeed.text = windSpeed
        binding.dialogClouds.text = clouds
    }

}