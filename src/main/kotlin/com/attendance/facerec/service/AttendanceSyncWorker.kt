package com.attendance.facerec.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.attendance.facerec.data.local.AppDatabase
import com.attendance.facerec.util.Constants
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AttendanceSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val database = AppDatabase.getInstance(applicationContext)
            val attendanceDao = database.attendanceDao()
            val firebaseDatabase = FirebaseDatabase.getInstance()

            // Get pending attendances
            val pendingRecords = attendanceDao.getPendingAttendances(limit = 50)

            if (pendingRecords.isEmpty()) {
                return@withContext Result.success()
            }

            // Sync each record
            for (record in pendingRecords) {
                try {
                    val today = java.text.SimpleDateFormat(
                        "yyyy-MM-dd",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date())

                    val dbPath = "${Constants.ATTENDANCE_PATH}/${record.classId}/$today/${record.studentId}"
                    val data = mapOf(
                        "id" to record.id,
                        "studentId" to record.studentId,
                        "classId" to record.classId,
                        "timestamp" to record.timestamp,
                        "status" to record.status,
                        "confidence" to record.confidence,
                        "deviceId" to record.deviceId
                    )

                    firebaseDatabase.reference.child(dbPath).setValue(data).get()
                    attendanceDao.markAsSynced(record.id)
                } catch (e: Exception) {
                    // Continue with next record
                }
            }

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
