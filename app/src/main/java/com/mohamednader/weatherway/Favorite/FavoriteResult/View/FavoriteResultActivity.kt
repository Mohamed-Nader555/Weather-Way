package com.mohamednader.weatherway.Favorite.FavoriteResult.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.Favorite.FavoriteResult.ViewModel.FavoriteResultViewModel
import com.mohamednader.weatherway.Favorite.FavoriteResult.ViewModel.FavoriteResultViewModelFactory
import com.mohamednader.weatherway.Home.View.Adapters.DailyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.HourlyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.OnDayClickListener
import com.mohamednader.weatherway.Home.View.Dialogs.DailyResultDialogFragment
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModel
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModelFactory
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.Daily
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.*
import com.mohamednader.weatherway.databinding.ActivityFavoriteResultBinding
import com.mohamednader.weatherway.databinding.ActivityMainHomeBinding
import com.mohamednader.weatherway.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class FavoriteResultActivity : AppCompatActivity(), OnDayClickListener {

    private val TAG = "FavoriteResultActivity_INFO_TAG"
    private lateinit var binding: ActivityFavoriteResultBinding

    //View Model Members
    private lateinit var favoriteResultViewModel: FavoriteResultViewModel
    private lateinit var favoriteResultFactory: FavoriteResultViewModelFactory

    //RecyclerViews Members
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyLinearLayoutManager: LinearLayoutManager
    private lateinit var hourlyLinearLayoutManager: LinearLayoutManager

    //Loading Progress
    private lateinit var customProgress: CustomProgress

    var locationAddress:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ViewModel
        favoriteResultFactory = FavoriteResultViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(this),
                ConcreteSharedPrefsSource(this)
            )
        )
        favoriteResultViewModel = ViewModelProvider(this, favoriteResultFactory).get(FavoriteResultViewModel::class.java)

        //Progress Bar
        customProgress = CustomProgress.getInstance()

        //RecyclerViews Components
        hourlyLinearLayoutManager =
            LinearLayoutManager(this@FavoriteResultActivity, RecyclerView.HORIZONTAL, false)
        dailyLinearLayoutManager =
            LinearLayoutManager(this@FavoriteResultActivity, RecyclerView.VERTICAL, false)
        hourlyAdapter = HourlyAdapter(this@FavoriteResultActivity)
        dailyAdapter = DailyAdapter(this@FavoriteResultActivity, this)

        binding.hourlyRecyclerView.apply {
            adapter = hourlyAdapter
            layoutManager = hourlyLinearLayoutManager
        }

        binding.dailyRecyclerView.apply {
            adapter = dailyAdapter
            layoutManager = dailyLinearLayoutManager
        }

        val place = intent.getSerializableExtra("place_data") as Place
        locationAddress = place.address

        val params: MutableMap<String, String> = mutableMapOf(
            "lat" to place.lat.toString(),
            "lon" to place.lon.toString()
        )
        params += mapOf(
            "appid" to Constants.app_id,
            "units" to Constants.tempUnitValueForAll,
            "lang" to Constants.languageValueForAll,
            "exclude" to "minutely"
        )
        favoriteResultViewModel.getWeatherDataFromNetwork(params)
        requestWeatherData()

    }





    private fun requestWeatherData() {
         lifecycleScope.launchWhenStarted {
             favoriteResultViewModel.weatherData.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        val weather = result.data
                        val img = weather.current.weather[0].icon
                        val des = weather.current.weather[0].description
                        val wind = weather.current.windSpeed.toDouble()



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



                        val temp = "${weather.current.temp.toFloat().roundToInt()}${Constants.tempUnitForAll}"
                        val humidity = "${weather.current.humidity.toFloat().roundToInt()}%"
                        val windSpeed = windUnitText
                        val clouds = "${weather.current.clouds.toFloat().roundToInt()}%"
                        val hourlyList = weather.hourly
                        val dailyList = weather.daily

                        val dateFormat = SimpleDateFormat("EEE,dd MMM", Locale.getDefault())
                        val date = Date(weather.current.dt.toLong() * 1000)

                        binding.weatherAddress.text = locationAddress
                        binding.weatherImage.setImageResource(getWeatherImageDrawable(img))
                        binding.weatherDes.text = des
                        binding.weatherTemp.text = temp
                        binding.weatherHumidity.text = humidity
                        binding.weatherWindSpeed.text = windSpeed
                        binding.weatherClouds.text = clouds
                        binding.weatherDate.text = dateFormat.format(date)
                        hourlyAdapter.submitList(hourlyList.take(24))
                        dailyAdapter.submitList(dailyList)
                        Log.i(TAG, "onCreate: ${result.data}")
                        delay(500)
                        customProgress.hideProgress()
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading...")

                            customProgress.showDialog(this@FavoriteResultActivity,   false)

                    }
                    is ApiState.Failure -> {
                        Toast.makeText(this@FavoriteResultActivity, "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDayClickListener(dailyItem: Daily) {
         val dailyResultDialog = DailyResultDialogFragment(dailyItem, locationAddress)
        dailyResultDialog.show(this@FavoriteResultActivity.supportFragmentManager, "DailyResultDialog")
    }


}