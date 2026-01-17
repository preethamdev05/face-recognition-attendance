package com.attendance.facerec.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.attendance.facerec.R
import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.domain.model.UserRole
import com.attendance.facerec.presentation.ui.admin.DashboardActivity
import com.attendance.facerec.presentation.ui.student.AttendanceMarkingActivity
import com.attendance.facerec.presentation.viewmodel.AuthViewModel
import com.attendance.facerec.util.ErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()
        observeState()
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            when {
                email.isEmpty() -> showError("Email is required")
                password.isEmpty() -> showError("Password is required")
                else -> authViewModel.loginWithEmail(email, password)
            }
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            authViewModel.loginState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        progressBar.visibility = android.view.View.VISIBLE
                        loginButton.isEnabled = false
                    }
                    is Result.Success -> {
                        progressBar.visibility = android.view.View.GONE
                        loginButton.isEnabled = true
                        val user = result.data

                        val intent = when (user.role) {
                            UserRole.ADMIN, UserRole.FACULTY -> Intent(this@LoginActivity, DashboardActivity::class.java)
                            else -> Intent(this@LoginActivity, AttendanceMarkingActivity::class.java)
                        }

                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                    }
                    is Result.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        loginButton.isEnabled = true
                        ErrorHandler.handleError(this@LoginActivity, result.exception) { message ->
                            Log.e("LoginActivity", message)
                        }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
