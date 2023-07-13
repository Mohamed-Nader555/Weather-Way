package com.mohamednader.weatherway.MainHome.View

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationBarView
import com.mohamednader.weatherway.Alarm.View.AlarmFragment
import com.mohamednader.weatherway.Favorite.View.FavoriteFragment
import com.mohamednader.weatherway.Home.View.HomeFragment
import com.mohamednader.weatherway.MainHome.ViewModel.MainHomeViewModel
import com.mohamednader.weatherway.MainHome.ViewModel.MainHomeViewModelFactory
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.Settings.View.SettingsFragment
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.databinding.ActivityMainHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class MainHome : AppCompatActivity() {

    private val TAG = "MainHome_INFO_TAG"
    private lateinit var binding: ActivityMainHomeBinding

//    //View Model Members
//    private lateinit var mainHomeViewModel: MainHomeViewModel
//    private lateinit var mainHomeFactory: MainHomeViewModelFactory

    private var isLanguageChangePending = false
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        //ViewModel
//        mainHomeFactory = MainHomeViewModelFactory(
//            Repository.getInstance(
//                ApiClient.getInstance(), ConcreteLocalSource(this), ConcreteSharedPrefsSource(this)
//            )
//        )
//        mainHomeViewModel =
//            ViewModelProvider(this, mainHomeFactory).get(MainHomeViewModel::class.java)
//
//
//        lifecycleScope.launchWhenStarted {
//            mainHomeViewModel.language.collect { result ->
//                when (result) {
//                    Constants.language_device -> {
//                        setAppLanguage(Locale.getDefault())
//                        Log.i(TAG, "onCreate: device")
//                    }
//                    Constants.language_arabic -> {
//                        setAppLanguage(Locale("ar"))
//                        Log.i(TAG, "onCreate: arabic")
//                    }
//                    Constants.language_english -> {
//                        setAppLanguage(Locale("en"))
//                        Log.i(TAG, "onCreate: english ")
//                    }
//                }
//
//                if (isLanguageChangePending) {
//                    recreate()
//                    isLanguageChangePending = false
//                }
//            }
//        }
//




        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
                .commit()
        }
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.selectedItemId = R.id.home_menu_item

        initViews()

    }

    private fun initViews() {

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home_menu_item -> {
                    replaceFragment(HomeFragment())
                    binding.fab.setImageResource(R.drawable.ic_baseline_search)
                }

                R.id.favorite_menu_item -> {
                    replaceFragment(FavoriteFragment())
                    binding.fab.setImageResource(R.drawable.ic_baseline_add)
                }

                R.id.alarms_menu_item -> {
                    replaceFragment(AlarmFragment())
                    binding.fab.setImageResource(R.drawable.ic_baseline_add)
                }

                R.id.settings_menu_item -> {
                    replaceFragment(SettingsFragment())
                    binding.fab.setImageResource(R.drawable.ic_baseline_restore)
                }
            }
            true
        })


        binding.fab.setOnClickListener(View.OnClickListener {
            val fragmentManager = supportFragmentManager
            val currentFragment = fragmentManager.findFragmentById(R.id.frame_layout)

            if (currentFragment is FabClickListener) {
                currentFragment.onFabClick()
            }
        })

    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


    override fun onBackPressed() {

        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.frame_layout)
        if (currentFragment is HomeFragment) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        } else {
            replaceFragment(HomeFragment())
            binding.bottomNavigationView.selectedItemId = R.id.home_menu_item
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isLanguageChangePending = true
    }

    private fun setAppLanguage(locale: Locale) {
        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }


}