package com.attendance.facerec.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["student_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("student_id"),
        Index("class_id"),
        Index("timestamp")
    ]
)
data class AttendanceEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "student_id")
    val studentId: String,
    @ColumnInfo(name = "class_id")
    val classId: String,
    val timestamp: Long,
    val status: String,
    val confidence: Float,
    @ColumnInfo(name = "location_json")
    val locationJson: String = "",
    @ColumnInfo(name = "device_id")
    val deviceId: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "is_verified")
    val isVerified: Boolean = false,
    @ColumnInfo(name = "verified_by")
    val verifiedBy: String? = null,
    val notes: String = "",
    @ColumnInfo(name = "sync_status")
    val syncStatus: String = "pending"
)
