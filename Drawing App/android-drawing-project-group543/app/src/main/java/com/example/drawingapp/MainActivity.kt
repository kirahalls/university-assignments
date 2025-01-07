package com.example.drawingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drawingapp.databinding.MainActivityBinding
import android.util.Log

/**
 * Simple MainActivity class. Sets up the tools widget fragment and the custom canvas view.
 */
class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }
}