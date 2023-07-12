package com.mohamednader.weatherway.Settings.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.MainHome.View.FabClickListener
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.Settings.ViewModel.SettingsFragmentViewModel
import com.mohamednader.weatherway.Settings.ViewModel.SettingsFragmentViewModelFactory
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import java.util.*


class SettingsFragment : Fragment(), FabClickListener {

    private val TAG = "SettingsFragment_INFO_TAG"

    private lateinit var binding: FragmentSettingsBinding

    //View Model Members
    private lateinit var settingsViewModel: SettingsFragmentViewModel
    private lateinit var settingsFactory: SettingsFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //ViewModel
        settingsFactory = SettingsFragmentViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )
        settingsViewModel =
            ViewModelProvider(this, settingsFactory).get(SettingsFragmentViewModel::class.java)



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {


                launch { settingsViewModel.getLanguageOption() }

                launch {
                    settingsViewModel.language.collect { result ->
                        Log.i(TAG, "initViews: $result")
                        when (result) {
                            Constants.language_arabic -> {
                                binding.radioGroupLanguageSetting.check(binding.radioArabicBtn.id)
                            }
                            Constants.language_english -> {
                                binding.radioGroupLanguageSetting.check(binding.radioEnglishBtn.id)
                            }
                        }
                    }
                }

                launch {
                    settingsViewModel.notification.collect { result ->
                        when (result) {
                            Constants.notification_enable -> {
                                binding.radioGroupNotificationSetting.check(binding.radioEnabledBtn.id)
                            }
                            Constants.notification_disable -> {
                                binding.radioGroupNotificationSetting.check(binding.radioDisabledBtn.id)
                            }
                        }
                    }
                }


                launch {
                    settingsViewModel.location.collect { result ->
                        when (result) {
                            Constants.location_gps -> {
                                binding.radioGroupLocationSetting.check(binding.radioGpsBtn.id)
                            }
                            Constants.location_map -> {
                                binding.radioGroupLocationSetting.check(binding.radioMapBtn.id)
                            }
                        }
                    }
                }



                launch {
                    settingsViewModel.tempUnit.collect { result ->
                        when (result) {
                            Constants.tempUnit_celsius -> {
                                binding.radioGroupTempUnitSetting.check(binding.radioCelsiusBtn.id)
                            }
                            Constants.tempUnit_kelvin -> {
                                binding.radioGroupTempUnitSetting.check(binding.radioKelvinBtn.id)
                            }
                            Constants.tempUnit_fahrenheit -> {
                                binding.radioGroupTempUnitSetting.check(binding.radioFahrenheitBtn.id)
                            }
                        }
                    }
                }


                launch {
                    settingsViewModel.windUnit.collect { result ->
                        when (result) {
                            Constants.windUnit_meter_per_second -> {
                                binding.radioGroupWindUnitSetting.check(binding.radioMeterPerSecondBtn.id)
                            }
                            Constants.windUnit_mile_per_hour -> {
                                binding.radioGroupWindUnitSetting.check(binding.radioMilePerHourBtn.id)
                            }
                        }
                    }

                }

            }
        }

        binding.radioEnglishBtn.setOnClickListener {
            setAppLanguage(Locale("en"))
            requireActivity().recreate()
        }
        binding.radioArabicBtn.setOnClickListener {
            setAppLanguage(Locale("ar"))
            requireActivity().recreate()
        }
        saveSettings()
    }


    private fun saveSettings() {


        binding.radioGroupLocationSetting.setOnCheckedChangeListener { _, checkedID ->
            when (checkedID) {
                binding.radioGpsBtn.id -> {
                    settingsViewModel.setLocationAccessOption(Constants.location_gps)
                }
                binding.radioMapBtn.id -> {
                    settingsViewModel.setLocationAccessOption(Constants.location_map)
                }
            }
        }



        binding.radioGroupNotificationSetting.setOnCheckedChangeListener { _, checkedID ->
            when (checkedID) {
                binding.radioEnabledBtn.id -> {
                    settingsViewModel.setNotificationOption(Constants.notification_enable)
                }
                binding.radioDisabledBtn.id -> {
                    settingsViewModel.setNotificationOption(Constants.notification_disable)

                }
            }
        }



        binding.radioGroupLanguageSetting.setOnCheckedChangeListener { _, checkedID ->
            when (checkedID) {
                binding.radioArabicBtn.id -> {
                    settingsViewModel.setLanguageOption(Constants.language_arabic)
                    Log.i(TAG, "saveSettings: Changed to ${Constants.language_arabic}")
                    settingsViewModel.getLanguageOption()
                }
                binding.radioEnglishBtn.id -> {
                    settingsViewModel.setLanguageOption(Constants.language_english)
                    Log.i(TAG, "saveSettings: Changed to ${Constants.language_english}")
                    settingsViewModel.getLanguageOption()
                }
            }


        }


        binding.radioGroupTempUnitSetting.setOnCheckedChangeListener { _, checkedID ->
            when (checkedID) {
                binding.radioCelsiusBtn.id -> {
                    settingsViewModel.setTempUnitOption(Constants.tempUnit_celsius)
                }
                binding.radioKelvinBtn.id -> {
                    settingsViewModel.setTempUnitOption(Constants.tempUnit_kelvin)
                }
                binding.radioFahrenheitBtn.id -> {
                    settingsViewModel.setTempUnitOption(Constants.tempUnit_fahrenheit)
                }
            }
        }



        binding.radioGroupWindUnitSetting.setOnCheckedChangeListener { _, checkedID ->
            when (checkedID) {
                binding.radioMeterPerSecondBtn.id -> {
                    settingsViewModel.setWindUnitOption(Constants.windUnit_meter_per_second)
                }
                binding.radioMilePerHourBtn.id -> {
                    settingsViewModel.setWindUnitOption(Constants.windUnit_mile_per_hour)
                }
            }
        }


    }

    private fun resetSettings() {
        settingsViewModel.setNotificationOption(Constants.notification_disable)
        settingsViewModel.setLocationAccessOption(Constants.location_gps)
        settingsViewModel.setTempUnitOption(Constants.tempUnit_celsius)
        settingsViewModel.setWindUnitOption(Constants.windUnit_meter_per_second)

        binding.radioGroupNotificationSetting.check(binding.radioDisabledBtn.id)
        binding.radioGroupLocationSetting.check(binding.radioGpsBtn.id)
        binding.radioGroupTempUnitSetting.check(binding.radioCelsiusBtn.id)
        binding.radioGroupWindUnitSetting.check(binding.radioMeterPerSecondBtn.id)

    }

    override fun onFabClick() {
        resetSettings()
        Toast.makeText(requireContext(), "Settings Reset Successfully", Toast.LENGTH_LONG).show()
    }

    private fun setAppLanguage(locale: Locale) {
        val config = resources.configuration
        config.locale = locale
        Locale.setDefault(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        requireContext().createConfigurationContext(config)
    }

}