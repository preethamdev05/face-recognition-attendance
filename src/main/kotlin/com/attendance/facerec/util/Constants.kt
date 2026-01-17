package com.attendance.facerec.util

import com.attendance.facerec.BuildConfig

object Constants {
    // Firebase - Now loaded from BuildConfig (injected via local.properties)
    val FIREBASE_DATABASE_URL: String = BuildConfig.FIREBASE_DATABASE_URL
    val FIREBASE_STORAGE_BUCKET: String = BuildConfig.FIREBASE_STORAGE_BUCKET

    // Face Detection
    const val MIN_FACE_SIZE = 100
    const val MAX_FACE_SIZE = 500
    const val FACE_DETECTION_CONFIDENCE_THRESHOLD = 0.6f
    const val FACE_LIVENESS_THRESHOLD = 0.7f
    const val MIN_IMAGE_WIDTH = 480
    const val MIN_IMAGE_HEIGHT = 360
    const val MIN_FACE_WIDTH = 200
    const val MIN_FACE_HEIGHT = 200

    // Attendance
    const val ATTENDANCE_WINDOW_LOCK_MINUTES = 5
    const val ATTENDANCE_TIMEOUT_SECONDS = 30
    const val LOW_ATTENDANCE_THRESHOLD = 75

    // Image Processing
    const val IMAGE_COMPRESSION_QUALITY = 85
    const val IMAGE_MAX_SIZE_KB = 500
    const val THUMBNAIL_WIDTH = 150
    const val THUMBNAIL_HEIGHT = 150
    const val IMAGE_RETRY_ATTEMPTS = 3

    // Session
    const val SESSION_TIMEOUT_MINUTES = 15
    const val TOKEN_EXPIRY_HOURS = 24
    const val OTP_VALIDITY_MINUTES = 10
    const val OTP_LENGTH = 6

    // Pagination
    const val ATTENDANCE_PAGE_SIZE = 50
    const val STUDENT_PAGE_SIZE = 20

    // Database Paths
    const val STUDENTS_PATH = "students"
    const val ATTENDANCE_PATH = "attendance"
    const val CLASSES_PATH = "classes"
    const val ADMINS_PATH = "admins"
    const val USERS_PATH = "users"

    // Permissions
    const val CAMERA_PERMISSION_REQUEST = 100
    const val LOCATION_PERMISSION_REQUEST = 101
    const val STORAGE_PERMISSION_REQUEST = 102

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "attendance_notifications"
    const val NOTIFICATION_CHANNEL_NAME = "Attendance Alerts"

    // Analytics
    const val ANALYTICS_EVENT_ATTENDANCE = "attendance_marked"
    const val ANALYTICS_EVENT_REGISTRATION = "student_registered"
    const val ANALYTICS_EVENT_LOGIN = "user_login"
}
