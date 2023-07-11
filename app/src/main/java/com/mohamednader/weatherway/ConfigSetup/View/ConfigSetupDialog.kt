package com.mohamednader.weatherway.ConfigSetup.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.ConfigSetup.ViewModel.ConfigSetupViewModel
import com.mohamednader.weatherway.ConfigSetup.ViewModel.ConfigSetupViewModelFactory
import com.mohamednader.weatherway.Home.View.HomeFragment
import com.mohamednader.weatherway.MainHome.MainHome
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.databinding.ConfigSetupFragmentBinding

class ConfigSetupDialog : DialogFragment() {

    private lateinit var binding: ConfigSetupFragmentBinding

    //View Model Members
    private lateinit var configViewModel: ConfigSetupViewModel
    private lateinit var configFactory: ConfigSetupViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ConfigSetupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //ViewModel
        configFactory = ConfigSetupViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(), ConcreteSharedPrefsSource(requireContext())
            )
        )
        configViewModel =
            ViewModelProvider(this, configFactory).get(ConfigSetupViewModel::class.java)


        initViews()

    }

    private fun initViews() {

        var selectedLocationAccess: String? = null
        var selectedNotification: String? = null
        binding.configSaveBtn.setOnClickListener {

            if (binding.radioGroupLocation.checkedRadioButtonId != -1) {


                binding.radioGroupLocation.setOnCheckedChangeListener { _, checkedID ->
                    when (checkedID) {
                        binding.radioGpsBtn.id -> {
                            selectedLocationAccess = Constants.location_gps
                        }
                        binding.radioMapBtn.id -> {
                            selectedLocationAccess = Constants.location_map
                        }
                    }
                }

                selectedNotification =
                    if (binding.configNotificationSwitch.isChecked) Constants.notification_enable else Constants.notification_disable

                configViewModel.saveFirstTime()
                configViewModel.saveLocationAccessOption(
                    selectedLocationAccess ?: Constants.location_gps
                )
                configViewModel.saveNotificationOption(
                    selectedNotification ?: Constants.notification_disable
                )
                configViewModel.saveLanguageOption(Constants.language_device)
                configViewModel.saveTempUnitOption(Constants.tempUnit_celsius)
                configViewModel.saveWindUnitOption(Constants.windUnit_meter_per_second)
                goToHomeScreen()

            } else {

                Toast.makeText(
                    requireContext(), "Please select location access method!", Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    private fun goToHomeScreen() {
        val intent = Intent(requireContext(), MainHome::class.java)
        startActivity(intent)
        activity?.finish()
    }

}