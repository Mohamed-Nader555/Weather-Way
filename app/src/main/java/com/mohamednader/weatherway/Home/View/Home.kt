package com.mohamednader.weatherway.Home.View

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Home.View.Adapters.DailyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.HourlyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.OnDayClickListener
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModel
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModelFactory
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.Utilities.getWeatherImageDrawable
import com.mohamednader.weatherway.databinding.ActivityHomeBinding
import kotlin.math.roundToInt

class Home : AppCompatActivity(), OnDayClickListener {

    private val TAG = "Home_INFO_TAG"
    private lateinit var binding: ActivityHomeBinding

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeFactory: HomeViewModelFactory

    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyLinearLayoutManager: LinearLayoutManager
    private lateinit var hourlyLinearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFactory = HomeViewModelFactory(
            Repository.getInstance(ApiClient.getInstance())
        )

        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)
        hourlyLinearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        dailyLinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        hourlyAdapter = HourlyAdapter(this)
        dailyAdapter = DailyAdapter(this , this)

        binding.hourlyRecyclerView.apply{
            adapter = hourlyAdapter
            layoutManager = hourlyLinearLayoutManager
        }

        binding.dailyRecyclerView.apply {
            adapter = dailyAdapter
            layoutManager = dailyLinearLayoutManager
        }

        lifecycleScope.launchWhenStarted {
            val params: MutableMap<String, String> =
                mutableMapOf("lat" to "30.044420", "lon" to "31.235712")
            params += mapOf("appid" to "3e6f518ff7d5d207d9c201f3c1ec34f6", "units" to "metric")
            homeViewModel.getWeatherDataFromNetwork(params)

            homeViewModel.weatherData.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        //showViews()
                        //allProductsAdapter.submitList(result.data)
                        val weather = result.data
                        val img = weather.current.weather[0].icon
                        val des = weather.current.weather[0].description
                        val temp = weather.current.temp.toFloat().roundToInt().toString()
                        val humidity = "${weather.current.humidity.toFloat().roundToInt()}%"
                        val windSpeed = "${weather.current.windSpeed.toFloat().roundToInt()}%"
                        val clouds = "${weather.current.clouds.toFloat().roundToInt()}%"
                        val hourlyList = weather.hourly
                        val dailyList = weather.daily

                        binding.weatherImage.setImageResource(getWeatherImageDrawable(img))
                        binding.weatherDes.text = des
                        binding.weatherTemp.text = temp
                        binding.weatherHumidity.text = humidity
                        binding.weatherWindSpeed.text = windSpeed
                        binding.weatherClouds.text = clouds
                        hourlyAdapter.submitList(hourlyList.take(24))
                        dailyAdapter.submitList(dailyList)

                        Log.i(TAG, "onCreate: ${result.data}")
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "onCreate: Loading...")
//                        hideViews()
                    }
                    is ApiState.Failure -> {
                        //hideViews()
                        Toast.makeText(this@Home, "There Was An Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    override fun onDayClickListener() {
        TODO("Not yet implemented")
    }
}