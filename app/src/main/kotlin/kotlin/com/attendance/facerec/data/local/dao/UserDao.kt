package com.attendance.facerec.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.attendance.facerec.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE session_token = :token")
    suspend fun getUserBySessionToken(token: String): UserEntity?

    @Query("SELECT * FROM users WHERE role = :role AND is_active = 1")
    fun getUsersByRole(role: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE is_active = 1 ORDER BY created_at DESC")
    fun getAllActiveUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET last_login_at = :loginTime WHERE id = :userId")
    suspend fun updateLastLogin(userId: String, loginTime: Long)

    @Query("UPDATE users SET session_token = :token, token_expiry = :expiry WHERE id = :userId")
    suspend fun updateSessionToken(userId: String, token: String, expiry: Long)
}
