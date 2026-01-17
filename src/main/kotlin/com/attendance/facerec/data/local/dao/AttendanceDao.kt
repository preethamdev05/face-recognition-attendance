package com.attendance.facerec.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.attendance.facerec.data.local.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: AttendanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attendances: List<AttendanceEntity>)

    @Update
    suspend fun update(attendance: AttendanceEntity)

    @Delete
    suspend fun delete(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance WHERE id = :attendanceId")
    suspend fun getAttendanceById(attendanceId: String): AttendanceEntity?

    @Query("SELECT * FROM attendance WHERE student_id = :studentId AND class_id = :classId AND DATE(timestamp/1000, 'unixepoch') = :date")
    suspend fun getAttendanceByStudentAndDate(studentId: String, classId: String, date: String): AttendanceEntity?

    @Query("SELECT * FROM attendance WHERE student_id = :studentId ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getStudentAttendanceHistory(studentId: String, limit: Int, offset: Int): List<AttendanceEntity>

    @Query("SELECT * FROM attendance WHERE class_id = :classId AND DATE(timestamp/1000, 'unixepoch') = :date ORDER BY timestamp ASC")
    suspend fun getClassAttendanceForDate(classId: String, date: String): List<AttendanceEntity>

    @Query("SELECT * FROM attendance WHERE class_id = :classId AND timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    suspend fun getClassAttendanceByDateRange(classId: String, startTime: Long, endTime: Long): List<AttendanceEntity>

    @Query("SELECT * FROM attendance WHERE sync_status = 'pending' LIMIT :limit")
    suspend fun getPendingAttendances(limit: Int = 50): List<AttendanceEntity>

    @Query("SELECT * FROM attendance ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getAllAttendancesPaginated(limit: Int, offset: Int): List<AttendanceEntity>

    @Query("SELECT * FROM attendance WHERE student_id = :studentId AND status = :status AND timestamp BETWEEN :startTime AND :endTime")
    suspend fun getStudentAttendanceByStatus(studentId: String, status: String, startTime: Long, endTime: Long): List<AttendanceEntity>

    @Query("SELECT COUNT(*) FROM attendance WHERE student_id = :studentId AND status = :status")
    suspend fun getCountByStudentAndStatus(studentId: String, status: String): Int

    @Query("SELECT COUNT(*) FROM attendance WHERE class_id = :classId AND DATE(timestamp/1000, 'unixepoch') = :date")
    suspend fun getAttendanceCountForDate(classId: String, date: String): Int

    @Query("UPDATE attendance SET sync_status = 'synced' WHERE id = :attendanceId")
    suspend fun markAsSynced(attendanceId: String)

    @Query("DELETE FROM attendance WHERE timestamp < :expiryTime")
    suspend fun deleteOldRecords(expiryTime: Long)
}
