# API Documentation

## Overview

This document describes the internal API structure of the Face Recognition Attendance System.

## Architecture

The app follows Clean Architecture with clear separation between layers:

```
Presentation → Domain → Data
```

## Domain Models

### Student

```kotlin
data class Student(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val enrollmentNumber: String,
    val classId: String,
    val faceEncodingPath: String?,
    val enrollmentDate: Long,
    val isActive: Boolean = true
)
```

### Attendance

```kotlin
data class Attendance(
    val id: String,
    val studentId: String,
    val classId: String,
    val timestamp: Long,
    val status: AttendanceStatus,
    val latitude: Double?,
    val longitude: Double?,
    val confidence: Float,
    val deviceId: String,
    val isSynced: Boolean = false
)

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE
}
```

## Repository Interfaces

### StudentRepository

```kotlin
interface StudentRepository {
    suspend fun registerStudent(student: Student, faceImage: Bitmap): Result<Student>
    suspend fun getStudent(studentId: String): Result<Student>
    suspend fun getAllStudents(classId: String): Flow<Result<List<Student>>>
    suspend fun updateStudent(student: Student): Result<Unit>
    suspend fun deleteStudent(studentId: String): Result<Unit>
    suspend fun searchStudents(query: String): Flow<Result<List<Student>>>
}
```

### AttendanceRepository

```kotlin
interface AttendanceRepository {
    suspend fun markAttendance(studentId: String, faceImage: Bitmap): Result<Attendance>
    suspend fun getAttendance(studentId: String, date: Long): Result<Attendance?>
    suspend fun getAttendanceByClass(classId: String, date: Long): Flow<Result<List<Attendance>>>
    suspend fun getAttendanceReport(studentId: String, startDate: Long, endDate: Long): Result<List<Attendance>>
    suspend fun syncPendingAttendances(): Result<Int>
}
```

### FaceRecognitionRepository

```kotlin
interface FaceRecognitionRepository {
    suspend fun detectFaces(image: Bitmap): Result<List<Face>>
    suspend fun generateFaceEncoding(face: Face, image: Bitmap): Result<FloatArray>
    suspend fun compareFaces(encoding1: FloatArray, encoding2: FloatArray): Float
    suspend fun findMatchingStudent(faceEncoding: FloatArray, classId: String): Result<Student?>
}
```

## ViewModels

### AttendanceMarkingViewModel

```kotlin
@HiltViewModel
class AttendanceMarkingViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val faceRecognitionRepository: FaceRecognitionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AttendanceUiState>(AttendanceUiState.Idle)
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun markAttendance(image: Bitmap, classId: String)
}

sealed class AttendanceUiState {
    object Idle : AttendanceUiState()
    object Loading : AttendanceUiState()
    data class Success(val attendance: Attendance) : AttendanceUiState()
    data class Error(val message: String) : AttendanceUiState()
}
```

## Firebase Structure

### Realtime Database

```
/
├── students/
│   └── {studentId}/
│       ├── name: String
│       ├── email: String
│       ├── phoneNumber: String
│       ├── enrollmentNumber: String
│       ├── classId: String
│       ├── faceEncodingPath: String
│       ├── enrollmentDate: Long
│       └── isActive: Boolean
│
├── attendance/
│   └── {classId}/
│       └── {date}/  // YYYY-MM-DD
│           └── {studentId}/
│               ├── timestamp: Long
│               ├── status: String
│               ├── latitude: Double
│               ├── longitude: Double
│               ├── confidence: Float
│               └── deviceId: String
│
├── classes/
│   └── {classId}/
│       ├── name: String
│       ├── description: String
│       └── createdAt: Long
│
├── admins/
│   └── {userId}/
│       ├── email: String
│       └── role: String
│
└── users/
    └── {userId}/
        ├── name: String
        ├── email: String
        └── role: String
```

### Firebase Storage

```
/
├── students/
│   └── {studentId}/
│       ├── profile.jpg
│       └── face_encoding.bin
│
└── reports/
    └── {classId}/
        └── {date}/
            └── attendance_report.pdf
```

## Error Handling

All repository methods return `Result<T>` sealed class:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## Common Exceptions

```kotlin
class StudentNotFoundException(studentId: String) : 
    Exception("Student not found: $studentId")

class FaceNotDetectedException : 
    Exception("No face detected in image")

class MultipleFacesDetectedException : 
    Exception("Multiple faces detected, only one allowed")

class FaceRecognitionFailedException : 
    Exception("Face recognition failed")

class NetworkException(cause: Throwable) : 
    Exception("Network error", cause)

class DatabaseException(cause: Throwable) : 
    Exception("Database error", cause)
```

## Constants

See `Constants.kt` for all application constants:

```kotlin
object Constants {
    const val FACE_DETECTION_CONFIDENCE_THRESHOLD = 0.6f
    const val FACE_LIVENESS_THRESHOLD = 0.7f
    const val MIN_FACE_SIZE = 100
    const val ATTENDANCE_TIMEOUT_SECONDS = 30
    // ... more constants
}
```

## Security

All sensitive data is encrypted using `SecurityUtils`:

```kotlin
val encrypted = SecurityUtils.encryptData(context, plainText)
val decrypted = SecurityUtils.decryptData(context, encrypted)
```

## Testing

Mock repositories for testing:

```kotlin
class MockStudentRepository : StudentRepository {
    override suspend fun registerStudent(student: Student, faceImage: Bitmap): Result<Student> {
        return Result.Success(student)
    }
    // ... implement other methods
}
```
