package com.attendance.facerec.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    @ColumnInfo(name = "roll_number")
    val rollNumber: String,
    val department: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "profile_image")
    val profileImage: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "registration_status")
    val registrationStatus: String,
    @ColumnInfo(name = "enrollment_date")
    val enrollmentDate: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "facial_encoding_path")
    val facialEncodingPath: String = "",
    @ColumnInfo(name = "registration_image_count")
    val registrationImageCount: Int = 0,
    @ColumnInfo(name = "sync_status")
    val syncStatus: String = "synced",
    @ColumnInfo(name = "face_encoding_json")
    val faceEncodingJson: String = ""
)
