package com.mohamednader.weatherway.Splash.View

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mohamednader.weatherway.ConfigSetup.View.ConfigSetupDialogFragment
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.MainHome.View.MainHome
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.R
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Splash.ViewModel.SplashViewModel
import com.mohamednader.weatherway.Splash.ViewModel.SplashViewModelFactory
import com.mohamednader.weatherway.Utilities.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class Splash : AppCompatActivity() {


    //View Model Members
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var splashFactory: SplashViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        //ViewModel
        splashFactory = SplashViewModelFactory(
            Repository.getInstance(ApiClient.getInstance(), ConcreteLocalSource(this), ConcreteSharedPrefsSource(this))
        )
        splashViewModel = ViewModelProvider(this, splashFactory).get(SplashViewModel::class.java)


        splashTimer()

    }

    private fun splashTimer() {

        lifecycleScope.launchWhenStarted {
            launch {
                splashViewModel.firstTime.collect{ result ->
                    when(result){
                        true ->{
                            delay(1000)
                            showConfigDialog()
                        }
                        false ->{
                            //sad
                            delay(1000)
                            goToHomeScreen()
                        }
                    }
                }
            }


            launch {
                splashViewModel.language.collect{ result ->
                    when (result) {
                        Constants.language_arabic -> {
                            setAppLanguage(Locale("ar"))                        }
                        Constants.language_english -> {
                            setAppLanguage(Locale("en"))                        }
                    }
                }
            }

        }

    }

    private fun showConfigDialog(){
        val configDialog = ConfigSetupDialogFragment()
        configDialog.isCancelable = false
        configDialog.show(this@Splash.supportFragmentManager, "ConfigDialog")

    }

    private fun goToHomeScreen(){
        val intent = Intent(this@Splash, MainHome::class.java)
        startActivity(intent)
        finish()
    }

    private fun setAppLanguage(locale: Locale) {
        val config = resources.configuration
        config.locale = locale
        Locale.setDefault(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        createConfigurationContext(config)
    }

}