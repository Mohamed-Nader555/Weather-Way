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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.Home.View.Adapters.DailyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.HourlyAdapter
import com.mohamednader.weatherway.Home.View.Adapters.OnDayClickListener
import com.mohamednader.weatherway.Home.View.Dialogs.DailyResultDialogFragment
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModel
import com.mohamednader.weatherway.Home.ViewModel.HomeViewModelFactory
import com.mohamednader.weatherway.MainHome.View.FabClickListener
import com.mohamednader.weatherway.Model.AlarmItem
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.Daily
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.Network.ApiState
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.*
import com.mohamednader.weatherway.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeFragment : Fragment(), OnDayClickListener, FabClickListener {

    private val TAG = "HomeFragment_INFO_TAG"
    private lateinit var binding: FragmentHomeBinding

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

    //API Variables
    private lateinit var languageValue: String
    private lateinit var tempUnitValue: String
    private lateinit var windUnitValue: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        getSharedPrefsData()
    }

    private fun initViews() {

        //ViewModel
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

        //Progress Bar
        customProgress = CustomProgress.getInstance()

        //Location
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())

        //RecyclerViews Components
        hourlyLinearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        dailyLinearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        hourlyAdapter = HourlyAdapter(requireContext())
        dailyAdapter = DailyAdapter(requireContext(), this)

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
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_PERMISSION_ID
            )
        }
    }

    private fun getSharedPrefsData() {

        lifecycleScope.launchWhenStarted {

            launch {
                homeViewModel.language.collect { result ->
                    Log.i(TAG, "initViews: $result")
                    when (result) {
                        Constants.language_arabic -> languageValue = result
                        Constants.language_english -> languageValue = result
                    }
                    Constants.languageValueForAll = languageValue
                }
            }

            launch {
                homeViewModel.tempUnit.collect { result ->
                    when (result) {
                        Constants.tempUnit_celsius -> tempUnitValue = Constants.metric
                        Constants.tempUnit_kelvin -> tempUnitValue = Constants.standard
                        Constants.tempUnit_fahrenheit -> tempUnitValue = Constants.imperial
                    }
                }
            }


            launch {
                homeViewModel.windUnit.collect { result ->
                    when (result) {
                        Constants.windUnit_meter_per_second -> windUnitValue = result
                        Constants.windUnit_mile_per_hour -> windUnitValue = result
                    }
                }
            }

            getLastLocation()
            requestWeatherData()

        }

    }

    private fun requestWeatherData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.weatherData.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        val weather = result.data
                        val img = weather.current.weather[0].icon
                        val des = weather.current.weather[0].description
                        val wind = weather.current.windSpeed.toDouble()
                        lateinit var tempUnit: String
                        lateinit var windUnitText: String
                        when (languageValue) {
                            Constants.language_arabic -> {
                                tempUnit = when (tempUnitValue) {
                                    Constants.standard -> "ك"
                                    Constants.metric -> "س"
                                    Constants.imperial -> "ف"
                                    else -> "س"
                                }
                            }
                            Constants.language_english -> {
                                tempUnit = when (tempUnitValue) {
                                    Constants.standard -> "k"
                                    Constants.metric -> "c"
                                    Constants.imperial -> "f"
                                    else -> "c"
                                }
                            }
                        }
                        //Log.i(TAG, "requestWeatherData-Alert: ${(weather.alerts?.get(0)?.tags?.get(0))?:"There is no alerts"}")

                        when (tempUnitValue) {
                            Constants.standard -> {
                                windUnitText = when (windUnitValue) {
                                    Constants.windUnit_meter_per_second -> "${wind.roundToInt()}m/s"
                                    Constants.windUnit_mile_per_hour -> "${
                                        convertMeterPerSecToMilePerHour(
                                            wind
                                        ).roundToInt()
                                    }m/h"
                                    else -> ""
                                }
                            }
                            Constants.metric -> {
                                windUnitText = when (windUnitValue) {
                                    Constants.windUnit_meter_per_second -> "${wind.roundToInt()}m/s"
                                    Constants.windUnit_mile_per_hour -> "${
                                        convertMeterPerSecToMilePerHour(
                                            wind
                                        ).roundToInt()
                                    }m/h"
                                    else -> ""
                                }
                            }
                            Constants.imperial -> {
                                windUnitText = when (windUnitValue) {
                                    Constants.windUnit_meter_per_second -> "${
                                        convertMilePerHourToMeterPerSec(
                                            wind
                                        ).roundToInt()
                                    }m/s"
                                    Constants.windUnit_mile_per_hour -> "${wind.roundToInt()}m/h"
                                    else -> ""
                                }
                            }
                        }
                        Constants.tempUnitForAll = tempUnit
                        Constants.tempUnitValueForAll = tempUnitValue
                        Constants.windUnitValueForAll = windUnitValue
                        val temp = "${weather.current.temp.toFloat().roundToInt()}$tempUnit"
                        val humidity = "${weather.current.humidity.toFloat().roundToInt()}%"
                        val windSpeed = windUnitText
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
                        if (checkPermissions()) {
                            customProgress.showDialog(requireContext(), "Loading..", false)
                        }
                    }
                    is ApiState.Failure -> {
                        //hideViews()
                        Toast.makeText(requireContext(), "There Was An Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDayClickListener(dailyItem: Daily) {
        Log.i(TAG, "onDayClickListener - The Address To Dialog: $locationAddress ")
        val dailyResultDialog = DailyResultDialogFragment(dailyItem, locationAddress)
        dailyResultDialog.show(requireActivity().supportFragmentManager, "DailyResultDialog")
    }


    //Location Section
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(requireContext(), "Turn On Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_PERMISSION_ID
            )

        }
    }

    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000 // Update interval in milliseconds
            fastestInterval = 5000 // Fastest update interval in milliseconds
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let { location ->
                    val params: MutableMap<String, String> = mutableMapOf(
                        "lat" to location.latitude.toString(),
                        "lon" to location.longitude.toString()
                    )
                    params += mapOf(
                        "appid" to Constants.app_id,
                        "units" to tempUnitValue,
                        "lang" to languageValue,
                        "exclude" to "minutely"
                    )
                    homeViewModel.getWeatherDataFromNetwork(params)
                    getAddressFromLocation(location)
                    fusedClient.removeLocationUpdates(this)
                }
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission granted")
                customProgress.showDialog(requireContext(), "Loading..", false)
                showViewsLocationPermission()
                getLastLocation()
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Permission denied")
                Toast.makeText(
                    requireContext(),
                    "Location permission denied. Unable to retrieve location.",
                    Toast.LENGTH_SHORT
                ).show()
                hideViewsLocationPermission()

            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getAddressFromLocation(location: Location) {
        try {
            val addresses: List<Address> = geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            ) as List<Address>

            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val country = address.countryName
                val adminArea = address.adminArea
                locationAddress = "$country, $adminArea"
                binding.weatherAddress.text = "$country, $adminArea"
                Constants.placeToAlarm = Place(1000, location.latitude.toString(), location.longitude.toString(), locationAddress)
            } else {
                Toast.makeText(requireContext(), "Unable to retrieve address.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error retrieving address.", Toast.LENGTH_SHORT).show()
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

    override fun onFabClick() {
        Toast.makeText(requireContext(), "This is From HomeFragment", Toast.LENGTH_LONG).show()
    }

}