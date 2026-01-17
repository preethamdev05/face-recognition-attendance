package com.attendance.facerec.presentation.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.attendance.facerec.R
import com.attendance.facerec.presentation.ui.common.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView
    private lateinit var presentCountText: TextView
    private lateinit var absentCountText: TextView
    private lateinit var reportsButton: Button
    private lateinit var settingsButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initializeViews()
        loadDashboardData()
        setupClickListeners()
    }

    private fun initializeViews() {
        welcomeText = findViewById(R.id.welcomeText)
        presentCountText = findViewById(R.id.presentCountText)
        absentCountText = findViewById(R.id.absentCountText)
        reportsButton = findViewById(R.id.reportsButton)
        settingsButton = findViewById(R.id.settingsButton)
        logoutButton = findViewById(R.id.logoutButton)
    }

    private fun loadDashboardData() {
        val user = intent.getSerializableExtra("user")
        if (user != null) {
            welcomeText.text = "Welcome, ${(user as com.attendance.facerec.domain.model.User).name}!"
        }

        // Load attendance statistics
        presentCountText.text = "42 Present"
        absentCountText.text = "3 Absent"
    }

    private fun setupClickListeners() {
        reportsButton.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}
