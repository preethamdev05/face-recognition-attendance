package com.attendance.facerec.domain.model

import java.io.Serializable

data class Student(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val rollNumber: String = "",
    val department: String = "",
    val phoneNumber: String = "",
    val profileImage: String = "",
    val isActive: Boolean = true,
    val registrationStatus: RegistrationStatus = RegistrationStatus.PENDING,
    val enrollmentDate: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val facialEncodingPath: String = "",
    val registrationImageCount: Int = 0
) : Serializable

enum class RegistrationStatus(val displayName: String) {
    PENDING("Registration Pending"),
    COMPLETED("Registration Completed"),
    REJECTED("Registration Rejected")
}

data class StudentRegistrationData(
    val name: String,
    val email: String,
    val rollNumber: String,
    val department: String,
    val phoneNumber: String,
    val imagePaths: List<String> = emptyList(),
    val faceEncodings: List<FloatArray> = emptyList()
) : Serializable
