package com.example.drawingapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import com.example.drawingapp.databinding.SplashScreenActivityBinding

/**
 * Create a splash screen activity and transition to the MainActivity after a delay.
 * Our splash screen is a turquoise screen
 */
class Splash_Screen_Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) //3 seconds
    }
}