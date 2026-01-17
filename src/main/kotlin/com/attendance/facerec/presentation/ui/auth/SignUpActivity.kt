package com.attendance.facerec.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.attendance.facerec.R
import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.presentation.viewmodel.AuthViewModel
import com.attendance.facerec.util.ErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var signUpButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeViews()
        setupClickListeners()
        observeState()
    }

    private fun initializeViews() {
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        signUpButton = findViewById(R.id.signUpButton)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupClickListeners() {
        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            when {
                name.isEmpty() -> showError("Name is required")
                email.isEmpty() -> showError("Email is required")
                password.isEmpty() -> showError("Password is required")
                password != confirmPassword -> showError("Passwords do not match")
                password.length < 6 -> showError("Password must be at least 6 characters")
                else -> authViewModel.registerWithEmail(email, password, name)
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            authViewModel.registerState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        progressBar.visibility = android.view.View.VISIBLE
                        signUpButton.isEnabled = false
                    }
                    is Result.Success -> {
                        progressBar.visibility = android.view.View.GONE
                        signUpButton.isEnabled = true
                        Toast.makeText(this@SignUpActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    }
                    is Result.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        signUpButton.isEnabled = true
                        ErrorHandler.handleError(this@SignUpActivity, result.exception) { }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
