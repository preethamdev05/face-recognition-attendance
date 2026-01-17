package com.attendance.facerec.data.repository

import com.attendance.facerec.data.local.dao.AttendanceDao
import com.attendance.facerec.data.local.entity.AttendanceEntity
import com.attendance.facerec.domain.model.AttendanceRecord
import com.attendance.facerec.domain.model.AttendanceStatus
import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.util.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class AttendanceRepositoryImpl(
    private val attendanceDao: AttendanceDao,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) {

    suspend fun markAttendance(studentId: String, classId: String): Result<AttendanceRecord> =
        withContext(Dispatchers.IO) {
            try {
                val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Date())

                // Check if already marked today
                val existing = attendanceDao.getAttendanceByStudentAndDate(studentId, classId, today)
                if (existing != null) {
                    return@withContext Result.Error(Exception("Attendance already marked for today"))
                }

                val recordId = UUID.randomUUID().toString()
                val timestamp = System.currentTimeMillis()

                val record = AttendanceRecord(
                    id = recordId,
                    studentId = studentId,
                    classId = classId,
                    timestamp = timestamp,
                    status = AttendanceStatus.PRESENT,
                    confidence = 0.95f,
                    deviceId = android.os.Build.DEVICE
                )

                // Save locally
                val entity = record.toEntity()
                attendanceDao.insert(entity)

                // Save to Firebase
                val dbPath = "${Constants.ATTENDANCE_PATH}/$classId/$today/$studentId"
                firebaseDatabase.reference.child(dbPath).setValue(record.toFirebaseMap()).await()

                Result.Success(record)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun getStudentAttendance(studentId: String, limit: Int = 50, offset: Int = 0): Result<List<AttendanceRecord>> =
        withContext(Dispatchers.IO) {
            try {
                val records = attendanceDao.getStudentAttendanceHistory(studentId, limit, offset)
                Result.Success(records.map { it.toDomainModel() })
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun getClassAttendance(classId: String, date: String): Result<List<AttendanceRecord>> =
        withContext(Dispatchers.IO) {
            try {
                val records = attendanceDao.getClassAttendanceForDate(classId, date)
                Result.Success(records.map { it.toDomainModel() })
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun getPendingAttendances(): Result<List<AttendanceRecord>> =
        withContext(Dispatchers.IO) {
            try {
                val records = attendanceDao.getPendingAttendances()
                Result.Success(records.map { it.toDomainModel() })
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    private fun AttendanceRecord.toEntity(): AttendanceEntity = AttendanceEntity(
        id = id,
        studentId = studentId,
        classId = classId,
        timestamp = timestamp,
        status = status.name,
        confidence = confidence,
        deviceId = deviceId,
        imageUrl = imageUrl,
        syncStatus = "pending"
    )

    private fun AttendanceEntity.toDomainModel(): AttendanceRecord = AttendanceRecord(
        id = id,
        studentId = studentId,
        classId = classId,
        timestamp = timestamp,
        status = AttendanceStatus.valueOf(status),
        confidence = confidence,
        deviceId = deviceId,
        imageUrl = imageUrl
    )

    private fun AttendanceRecord.toFirebaseMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "studentId" to studentId,
        "classId" to classId,
        "timestamp" to timestamp,
        "status" to status.name,
        "confidence" to confidence,
        "deviceId" to deviceId,
        "imageUrl" to imageUrl
    )
}
