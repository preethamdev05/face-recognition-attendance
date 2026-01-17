package com.attendance.facerec.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.attendance.facerec.data.local.converter.DateTypeConverter
import com.attendance.facerec.data.local.converter.GeoLocationConverter
import com.attendance.facerec.data.local.dao.AttendanceDao
import com.attendance.facerec.data.local.dao.StudentDao
import com.attendance.facerec.data.local.entity.AttendanceEntity
import com.attendance.facerec.data.local.entity.StudentEntity
import com.attendance.facerec.data.local.entity.UserEntity
import com.attendance.facerec.data.local.dao.UserDao

@Database(
    entities = [
        StudentEntity::class,
        AttendanceEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateTypeConverter::class, GeoLocationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "attendance_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
