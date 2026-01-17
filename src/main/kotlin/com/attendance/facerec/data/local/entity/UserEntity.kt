package com.attendance.facerec.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String = "",
    val name: String,
    val role: String,
    @ColumnInfo(name = "department_id")
    val departmentId: String = "",
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "profile_image")
    val profileImage: String? = null,
    @ColumnInfo(name = "last_login_at")
    val lastLoginAt: Long? = null,
    @ColumnInfo(name = "session_token")
    val sessionToken: String = "",
    @ColumnInfo(name = "token_expiry")
    val tokenExpiry: Long = 0
)
