package com.attendance.facerec.domain.model

import java.io.Serializable

data class AttendanceRecord(
    val id: String = "",
    val studentId: String = "",
    val classId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: AttendanceStatus = AttendanceStatus.PRESENT,
    val confidence: Float = 0f,
    val location: GeoLocation? = null,
    val deviceId: String = "",
    val imageUrl: String = "",
    val isVerified: Boolean = false,
    val verifiedBy: String? = null,
    val notes: String = ""
) : Serializable

enum class AttendanceStatus(val displayName: String) {
    PRESENT("Present"),
    LATE("Late"),
    ABSENT("Absent"),
    LEAVE("Leave")
}

data class GeoLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0f
) : Serializable

data class AttendanceSummary(
    val totalPresent: Int = 0,
    val totalLate: Int = 0,
    val totalAbsent: Int = 0,
    val totalLeave: Int = 0,
    val attendancePercentage: Float = 0f,
    val period: String = ""
) : Serializable
