package com.mohamednader.weatherway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mohamednader.weatherway.Home.View.Home
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashTimer()

    }

    private fun splashTimer(){
        lifecycleScope.launch(Dispatchers.Main){
            delay(1000)
            val intent = Intent(this@Splash, Home::class.java)
            startActivity(intent)
        }
    }
}