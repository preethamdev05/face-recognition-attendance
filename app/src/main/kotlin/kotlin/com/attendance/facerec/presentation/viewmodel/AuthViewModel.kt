package com.attendance.facerec.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendance.facerec.data.repository.AuthRepositoryImpl
import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<User>>(Result.Loading)
    val loginState: StateFlow<Result<User>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Result<User>>(Result.Loading)
    val registerState: StateFlow<Result<User>> = _registerState.asStateFlow()

    private val _logoutState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val logoutState: StateFlow<Result<Unit>> = _logoutState.asStateFlow()

    private val _currentUser = MutableStateFlow<Result<User>>(Result.Loading)
    val currentUser: StateFlow<Result<User>> = _currentUser.asStateFlow()

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Result.Loading
            _loginState.value = authRepository.loginWithEmail(email, password)
        }
    }

    fun registerWithEmail(email: String, password: String, name: String) {
        viewModelScope.launch {
            _registerState.value = Result.Loading
            _registerState.value = authRepository.registerWithEmail(email, password, name)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Result.Loading
            _logoutState.value = authRepository.logout()
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            _currentUser.value = authRepository.getCurrentUser()
        }
    }
}
