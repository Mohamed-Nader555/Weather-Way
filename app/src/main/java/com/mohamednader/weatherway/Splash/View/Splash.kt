package com.mohamednader.weatherway.Splash.View

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mohamednader.weatherway.ConfigSetup.View.ConfigSetupDialog
import com.mohamednader.weatherway.Home.View.HomeFragment
import com.mohamednader.weatherway.MainHome.MainHome
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Splash.ViewModel.SplashViewModel
import com.mohamednader.weatherway.Splash.ViewModel.SplashViewModelFactory
import kotlinx.coroutines.delay

class Splash : AppCompatActivity() {


    //View Model Members
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var splashFactory: SplashViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        //ViewModel
        splashFactory = SplashViewModelFactory(
            Repository.getInstance(ApiClient.getInstance(), ConcreteSharedPrefsSource(this))
        )
        splashViewModel = ViewModelProvider(this, splashFactory).get(SplashViewModel::class.java)

        splashTimer()

    }

    private fun splashTimer() {

        lifecycleScope.launchWhenStarted {
            splashViewModel.firstTime.collect{ result ->
                when(result){
                    true ->{
                        delay(1000)
                        showConfigDialog()
                        Toast.makeText(this@Splash, "This is The First Time" , Toast.LENGTH_LONG).show()
                    }
                    false ->{
                        //sad
                        delay(1000)
                        goToHomeScreen()
                        Toast.makeText(this@Splash, "This is The NOT First Time " , Toast.LENGTH_LONG).show()

                    }
                }
            }
        }

    }

    private fun showConfigDialog(){
        val configDialog = ConfigSetupDialog()
        configDialog.isCancelable = false
        configDialog.show(this@Splash.supportFragmentManager, "ConfigDialog")

    }

    private fun goToHomeScreen(){
        val intent = Intent(this@Splash, MainHome::class.java)
        startActivity(intent)
        finish()
    }



}