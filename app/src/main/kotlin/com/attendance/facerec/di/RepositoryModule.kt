package com.attendance.facerec.di

import com.attendance.facerec.data.local.dao.AttendanceDao
import com.attendance.facerec.data.local.dao.StudentDao
import com.attendance.facerec.data.local.dao.UserDao
import com.attendance.facerec.data.repository.AttendanceRepositoryImpl
import com.attendance.facerec.data.repository.AuthRepositoryImpl
import com.attendance.facerec.data.repository.StudentRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ): AuthRepositoryImpl {
        return AuthRepositoryImpl(firebaseAuth, firebaseDatabase)
    }

    @Singleton
    @Provides
    fun provideStudentRepository(
        studentDao: StudentDao,
        firebaseDatabase: FirebaseDatabase,
        firebaseStorage: FirebaseStorage
    ): StudentRepositoryImpl {
        return StudentRepositoryImpl(studentDao, firebaseDatabase, firebaseStorage)
    }

    @Singleton
    @Provides
    fun provideAttendanceRepository(
        attendanceDao: AttendanceDao,
        firebaseDatabase: FirebaseDatabase,
        firebaseStorage: FirebaseStorage
    ): AttendanceRepositoryImpl {
        return AttendanceRepositoryImpl(attendanceDao, firebaseDatabase, firebaseStorage)
    }
}
