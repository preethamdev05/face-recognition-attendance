package com.attendance.facerec.domain.model

import java.io.Serializable

data class User(
    val id: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val role: UserRole = UserRole.STUDENT,
    val departmentId: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val profileImage: String? = null,
    val lastLoginAt: Long? = null
) : Serializable

enum class UserRole(val displayName: String) {
    ADMIN("Administrator"),
    FACULTY("Faculty Member"),
    STUDENT("Student")
}

data class UserSession(
    val userId: String,
    val email: String,
    val role: UserRole,
    val token: String,
    val expiresAt: Long,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable
