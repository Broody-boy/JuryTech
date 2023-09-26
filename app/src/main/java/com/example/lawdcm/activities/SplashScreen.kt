package com.example.lawdcm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.lawdcm.MainActivity
import com.example.lawdcm.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val intent = Intent(this, MainActivity::class.java)

        // Create a Handler to post a delayed action
        val handler = Handler()

        // Delayed execution after 3000ms (3 seconds)
        val delayMillis: Long = 3000
        handler.postDelayed({
            // Start the intent after the delay
            startActivity(intent)
            finish() // Optionally, finish the current activity
        }, delayMillis)

    }
}