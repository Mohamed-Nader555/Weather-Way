package com.mohamednader.weatherway.MainHome

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.mohamednader.weatherway.Alarm.View.AlarmFragment
import com.mohamednader.weatherway.Favorite.View.FavoriteFragment
import com.mohamednader.weatherway.Home.View.HomeFragment
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.Settings.View.SettingsFragment
import com.mohamednader.weatherway.databinding.ActivityMainHomeBinding

class MainHome : AppCompatActivity() {

    private val TAG = "MainHome_INFO_TAG"
    private lateinit var binding: ActivityMainHomeBinding

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
                .commit()
        }
        replaceFragment(HomeFragment())

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
}