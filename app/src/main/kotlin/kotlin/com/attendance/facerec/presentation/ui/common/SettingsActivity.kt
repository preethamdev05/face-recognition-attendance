package com.attendance.facerec.presentation.ui.common

import android.os.Bundle
import android.widget.Switch
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.attendance.facerec.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var languageSpinner: Spinner
    private lateinit var notificationSwitch: Switch
    private lateinit var darkModeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeViews()
        loadSettings()
    }

    private fun initializeViews() {
        languageSpinner = findViewById(R.id.languageSpinner)
        notificationSwitch = findViewById(R.id.notificationSwitch)
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
    }

    private fun loadSettings() {
        // Load settings from SharedPreferences
        notificationSwitch.isChecked = true
        darkModeSwitch.isChecked = false
    }
}
