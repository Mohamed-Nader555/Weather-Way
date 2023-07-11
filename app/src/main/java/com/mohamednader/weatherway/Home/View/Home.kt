package com.mohamednader.weatherway.Home.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.mohamednader.weatherway.Home.View.Adapters.DailyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.HourlyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.OnDayClickListener
import com.mohamednader.weatherway.Home.View.Dialogs.DailyResultDialog
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModel
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModelFactory
import com.mohamednader.weatherway.Model.Pojo.Daily
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.CustomProgress
import com.mohamednader.weatherway.Utilities.getWeatherImageDrawable
import com.mohamednader.weatherway.databinding.ActivityHomeBinding
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Home : AppCompatActivity(), OnDayClickListener {

    private val TAG = "Home_INFO_TAG"
    private lateinit var binding: ActivityHomeBinding

    //View Model Members
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeFactory: HomeViewModelFactory

    //RecyclerViews Members
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyLinearLayoutManager: LinearLayoutManager
    private lateinit var hourlyLinearLayoutManager: LinearLayoutManager

    //Location Members
    private val REQUEST_LOCATION_PERMISSION_ID = 1001
    private lateinit var currentLocation: Location
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    //Loading Progress
    private lateinit var customProgress: CustomProgress

    //Needed Variables
    lateinit var locationAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        requestWeatherData()
        getLastLocation()
    }

    private fun initViews() {

        //ViewModel
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(ApiClient.getInstance(), ConcreteSharedPrefsSource(this))
        )
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

        //Progress Bar
        customProgress = CustomProgress.getInstance()

        //Location
        geocoder = Geocoder(this, Locale.getDefault())
        fusedClient = LocationServices.getFusedLocationProviderClient(this)

        //RecyclerViews Components
        hourlyLinearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        dailyLinearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        hourlyAdapter = HourlyAdapter(this)
        dailyAdapter = DailyAdapter(this, this)

        binding.hourlyRecyclerView.apply {
            adapter = hourlyAdapter
            layoutManager = hourlyLinearLayoutManager
        }

        binding.dailyRecyclerView.apply {
            adapter = dailyAdapter
            layoutManager = dailyLinearLayoutManager
        }

        //Click Listeners
        binding.locationPermissionRequestBtn.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestWeatherData() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.weatherData.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        val weather = result.data
                        val img = weather.current.weather[0].icon
                        val des = weather.current.weather[0].description
                        val temp = weather.current.temp.toFloat().roundToInt().toString()
                        val humidity = "${weather.current.humidity.toFloat().roundToInt()}%"
                        val windSpeed = "${weather.current.windSpeed.toFloat().roundToInt()}%"
                        val clouds = "${weather.current.clouds.toFloat().roundToInt()}%"
                        val hourlyList = weather.hourly
                        val dailyList = weather.daily

                        val dateFormat = SimpleDateFormat("EEE,dd MMM", Locale.getDefault())
                        val date = Date(weather.current.dt.toLong() * 1000)

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
                        customProgress.showDialog(this@Home, "Loading..", false)
                    }
                    is ApiState.Failure -> {
                        //hideViews()
                        Toast.makeText(this@Home, "There Was An Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDayClickListener(dailyItem: Daily) {
        val dailyResultDialog = DailyResultDialog(dailyItem, locationAddress)
        dailyResultDialog.show(this@Home.supportFragmentManager, "DailyResultDialog")
    }


    //Location Section
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(this, "Turn On Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    //button to open google maps and pin the long and lat
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), REQUEST_LOCATION_PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        fusedClient.lastLocation.addOnSuccessListener {
            val params: MutableMap<String, String> =
                mutableMapOf("lat" to it.latitude.toString(), "lon" to it.longitude.toString())
            params += mapOf(
                "appid" to "3e6f518ff7d5d207d9c201f3c1ec34f6",
                "units" to "metric",
                "exclude" to "minutely"
            )
            homeViewModel.getWeatherDataFromNetwork(params)
            getAddressFromLocation(it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showViewsLocationPermission()
                getLastLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Unable to retrieve location.",
                    Toast.LENGTH_SHORT
                ).show()
                hideViewsLocationPermission()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAddressFromLocation(location: Location) {
        val addresses: List<Address> = geocoder.getFromLocation(
            location.latitude, location.longitude, 1
        ) as List<Address>

        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            val country = address.countryName
            val adminArea = address.adminArea
            locationAddress = "$country, $adminArea"
            binding.weatherAddress.text = "$country, $adminArea"
        } else {
            Toast.makeText(this, "Unable to retrieve address.", Toast.LENGTH_SHORT).show()
        }

    }

    //Show and Hide Views Functions
    private fun hideViewsLocationPermission() {
        binding.locationPermissionHintCardView.visibility = View.VISIBLE
        binding.weatherImage.visibility = View.GONE
        binding.weatherDes.visibility = View.GONE
        binding.tempDegree.visibility = View.GONE
        binding.weatherThreeDetails.visibility = View.GONE
        binding.weatherHourlyTitle.visibility = View.GONE
        binding.hourlyRecyclerView.visibility = View.GONE
        binding.dailyHintCardView.visibility = View.GONE
        binding.dailyRecyclerView.visibility = View.GONE
    }

    private fun showViewsLocationPermission() {
        binding.locationPermissionHintCardView.visibility = View.GONE
        binding.weatherImage.visibility = View.VISIBLE
        binding.weatherDes.visibility = View.VISIBLE
        binding.tempDegree.visibility = View.VISIBLE
        binding.weatherThreeDetails.visibility = View.VISIBLE
        binding.weatherHourlyTitle.visibility = View.VISIBLE
        binding.hourlyRecyclerView.visibility = View.VISIBLE
        binding.dailyHintCardView.visibility = View.VISIBLE
        binding.dailyRecyclerView.visibility = View.VISIBLE
    }

}