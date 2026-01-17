package com.attendance.facerec.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.attendance.facerec.data.local.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentEntity>)

    @Update
    suspend fun update(student: StudentEntity)

    @Delete
    suspend fun delete(student: StudentEntity)

    @Query("SELECT * FROM students WHERE id = :studentId")
    suspend fun getStudentById(studentId: String): StudentEntity?

    @Query("SELECT * FROM students WHERE email = :email")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    @Query("SELECT * FROM students WHERE roll_number = :rollNumber")
    suspend fun getStudentByRollNumber(rollNumber: String): StudentEntity?

    @Query("SELECT * FROM students WHERE department = :department ORDER BY name ASC")
    suspend fun getStudentsByDepartment(department: String): List<StudentEntity>

    @Query("SELECT * FROM students WHERE registration_status = :status")
    fun getStudentsByStatus(status: String): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getStudentsPaginated(limit: Int, offset: Int): List<StudentEntity>

    @Query("SELECT * FROM students WHERE is_active = 1 ORDER BY name ASC")
    fun getAllActiveStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students ORDER BY created_at DESC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getTotalStudentCount(): Int

    @Query("SELECT COUNT(*) FROM students WHERE registration_status = :status")
    suspend fun getCountByStatus(status: String): Int

    @Query("DELETE FROM students WHERE id = :studentId")
    suspend fun deleteStudentById(studentId: String)
}
