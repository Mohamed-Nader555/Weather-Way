package com.mohamednader.weatherway.Home.View.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mohamednader.weatherway.Model.Pojo.Daily
import com.mohamednader.weatherway.Utilities.getWeatherImageDrawable
import com.mohamednader.weatherway.databinding.DailyResultDialogFragmentBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyResultDialog(val dailyItem: Daily, val address: String) : DialogFragment() {

    private lateinit var binding: DailyResultDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DailyResultDialogFragmentBinding.inflate(inflater, container, false)
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
        val temp = dailyItem.temp.day.toFloat().roundToInt().toString()
        val humidity = "${dailyItem.humidity.toFloat().roundToInt()}%"
        val windSpeed = "${dailyItem.windSpeed.toFloat().roundToInt()}%"
        val clouds = "${dailyItem.clouds.toFloat().roundToInt()}%"
        val minTemp = "${dailyItem.temp.min.toFloat().roundToInt()}°"
        val maxTemp = "${dailyItem.temp.max.toFloat().roundToInt()}°"
        val mornTemp = "${dailyItem.temp.eve.toFloat().roundToInt()}°"
        val eveTemp = "${dailyItem.temp.morn.toFloat().roundToInt()}°"

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