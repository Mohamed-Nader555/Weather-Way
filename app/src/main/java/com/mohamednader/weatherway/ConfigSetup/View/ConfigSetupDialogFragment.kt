package com.mohamednader.weatherway.ConfigSetup.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.weatherway.ConfigSetup.ViewModel.ConfigSetupViewModel
import com.mohamednader.weatherway.ConfigSetup.ViewModel.ConfigSetupViewModelFactory
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.MainHome.View.MainHome
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.databinding.FragmentConfigSetupBinding

class ConfigSetupDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentConfigSetupBinding

    //View Model Members
    private lateinit var configViewModel: ConfigSetupViewModel
    private lateinit var configFactory: ConfigSetupViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfigSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //ViewModel
        configFactory = ConfigSetupViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(), ConcreteLocalSource(requireContext()), ConcreteSharedPrefsSource(requireContext())
            )
        )
        configViewModel =
            ViewModelProvider(this, configFactory).get(ConfigSetupViewModel::class.java)


        initViews()

    }

    private fun initViews() {

        binding.radioGroupLocation.check(binding.radioGpsBtn.id)
        binding.configSaveBtn.setOnClickListener {

                binding.radioGroupLocation.setOnCheckedChangeListener { _, checkedID ->
                    when (checkedID) {
                        binding.radioGpsBtn.id -> {
                            configViewModel.saveLocationAccessOption(Constants.location_gps)
                        }
                        binding.radioMapBtn.id -> {
                            configViewModel.saveLocationAccessOption(Constants.location_map)
                        }
                    }
                }

                val selectedNotification =
                    if (binding.configNotificationSwitch.isChecked) Constants.notification_enable else Constants.notification_disable

                configViewModel.saveFirstTime()
                configViewModel.saveNotificationOption(selectedNotification)
                configViewModel.saveLanguageOption(Constants.language_english)
                configViewModel.saveTempUnitOption(Constants.tempUnit_celsius)
                configViewModel.saveWindUnitOption(Constants.windUnit_meter_per_second)
                goToHomeScreen()

        }
    }

    private fun goToHomeScreen() {
        val intent = Intent(requireContext(), MainHome::class.java)
        startActivity(intent)
        activity?.finish()
    }

}