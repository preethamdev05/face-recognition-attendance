package com.attendance.facerec.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendance.facerec.data.repository.StudentRepositoryImpl
import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.domain.model.Student
import com.attendance.facerec.domain.model.StudentRegistrationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentRegistrationViewModel @Inject constructor(
    private val studentRepository: StudentRepositoryImpl
) : ViewModel() {

    private val _registrationState = MutableStateFlow<Result<Student>>(Result.Loading)
    val registrationState: StateFlow<Result<Student>> = _registrationState.asStateFlow()

    private val _capturedImages = MutableStateFlow<List<String>>(emptyList())
    val capturedImages: StateFlow<List<String>> = _capturedImages.asStateFlow()

    fun registerStudent(studentData: StudentRegistrationData) {
        viewModelScope.launch {
            _registrationState.value = Result.Loading
            _registrationState.value = studentRepository.registerStudent(studentData)
        }
    }

    fun addCapturedImage(imagePath: String) {
        val currentList = _capturedImages.value.toMutableList()
        currentList.add(imagePath)
        _capturedImages.value = currentList
    }

    fun removeCapturedImage(imagePath: String) {
        val currentList = _capturedImages.value.toMutableList()
        currentList.remove(imagePath)
        _capturedImages.value = currentList
    }

    fun clearCapturedImages() {
        _capturedImages.value = emptyList()
    }
}
