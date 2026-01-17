package com.attendance.facerec.data.repository

import android.net.Uri
import com.attendance.facerec.data.local.dao.StudentDao
import com.attendance.facerec.data.local.entity.StudentEntity
import com.attendance.facerec.domain.model.Result
import com.attendance.facerec.domain.model.Student
import com.attendance.facerec.domain.model.StudentRegistrationData
import com.attendance.facerec.util.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import kotlin.coroutines.resume

class StudentRepositoryImpl(
    private val studentDao: StudentDao,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) {

    suspend fun registerStudent(studentData: StudentRegistrationData): Result<Student> =
        withContext(Dispatchers.IO) {
            try {
                val studentId = UUID.randomUUID().toString()
                val timestamp = System.currentTimeMillis()

                // Upload images to Firebase Storage
                val uploadedImageUrls = uploadStudentImages(studentId, studentData.imagePaths)

                // Create student record
                val student = Student(
                    id = studentId,
                    name = studentData.name,
                    email = studentData.email,
                    rollNumber = studentData.rollNumber,
                    department = studentData.department,
                    phoneNumber = studentData.phoneNumber,
                    registrationImageCount = uploadedImageUrls.size,
                    createdAt = timestamp,
                    updatedAt = timestamp
                )

                // Save to local database
                val entity = student.toEntity()
                studentDao.insert(entity)

                // Save to Firebase
                val dbRef = firebaseDatabase.reference.child(Constants.STUDENTS_PATH).child(studentId)
                dbRef.setValue(student.toFirebaseMap()).await()

                Result.Success(student)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun getStudent(studentId: String): Result<Student> =
        withContext(Dispatchers.IO) {
            try {
                val entity = studentDao.getStudentById(studentId)
                val student = entity?.toDomainModel()
                    ?: throw Exception("Student not found")
                Result.Success(student)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    suspend fun updateStudent(student: Student): Result<Student> =
        withContext(Dispatchers.IO) {
            try {
                val updated = student.copy(updatedAt = System.currentTimeMillis())
                val entity = updated.toEntity()
                studentDao.update(entity)

                val dbRef = firebaseDatabase.reference.child(Constants.STUDENTS_PATH).child(student.id)
                dbRef.setValue(updated.toFirebaseMap()).await()

                Result.Success(updated)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    private suspend fun uploadStudentImages(studentId: String, imagePaths: List<String>): List<String> =
        withContext(Dispatchers.IO) {
            imagePaths.mapIndexed { index, path ->
                try {
                    val file = File(path)
                    val ref = firebaseStorage.reference
                        .child("${Constants.STUDENTS_PATH}/$studentId/faces/image_$index.jpg")

                    ref.putFile(Uri.fromFile(file)).await()
                    val downloadUrl = ref.downloadUrl.await()
                    downloadUrl.toString()
                } catch (e: Exception) {
                    ""
                }
            }.filter { it.isNotEmpty() }
        }

    private fun Student.toEntity(): StudentEntity = StudentEntity(
        id = id,
        name = name,
        email = email,
        rollNumber = rollNumber,
        department = department,
        phoneNumber = phoneNumber,
        profileImage = profileImage,
        isActive = isActive,
        registrationStatus = registrationStatus.name,
        enrollmentDate = enrollmentDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        registrationImageCount = registrationImageCount
    )

    private fun StudentEntity.toDomainModel(): Student = Student(
        id = id,
        name = name,
        email = email,
        rollNumber = rollNumber,
        department = department,
        phoneNumber = phoneNumber,
        profileImage = profileImage,
        isActive = isActive,
        registrationStatus = com.attendance.facerec.domain.model.RegistrationStatus.valueOf(registrationStatus),
        enrollmentDate = enrollmentDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        registrationImageCount = registrationImageCount
    )

    private fun Student.toFirebaseMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "email" to email,
        "rollNumber" to rollNumber,
        "department" to department,
        "phoneNumber" to phoneNumber,
        "profileImage" to profileImage,
        "isActive" to isActive,
        "registrationStatus" to registrationStatus.name,
        "enrollmentDate" to enrollmentDate,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt
    )
}
