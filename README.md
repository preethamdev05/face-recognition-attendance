# Face Recognition Attendance System

Production-grade Face Recognition Attendance System in Kotlin using Android Studio, Firebase ML Kit for face detection, and Firebase Realtime Database for attendance tracking.

## Features

✅ Firebase Authentication with Role-Based Access Control  
✅ Multi-face Student Registration (5-8 photos)  
✅ Real-time Face Recognition & Attendance Marking  
✅ Advanced Liveness Detection  
✅ Firebase Storage with Image Compression  
✅ Offline-first Architecture with Room Database  
✅ Analytics Dashboard with Reports  
✅ Kotlin Coroutines for Async Operations  
✅ Hilt Dependency Injection  
✅ Material Design 3 UI  
✅ Complete Error Handling with Result Sealed Class  

## Technical Stack

- **Language**: Kotlin (SDK 21+, Target 34)
- **Build Tool**: Gradle 8.0+
- **Face Detection**: Firebase ML Kit
- **Face Recognition**: TensorFlow Lite + FaceNet
- **Backend**: Firebase (Auth, Realtime DB, Storage, Cloud Functions)
- **Local DB**: Room
- **Image Processing**: CameraX API
- **Async**: Kotlin Coroutines
- **DI**: Hilt
- **UI**: Material Design 3

## Project Structure

```
src/main/kotlin/com/attendance/facerec/
├── presentation/          # UI Layer (Activities, Fragments, ViewModels)
├── domain/               # Business Logic (Models, Use Cases)
├── data/                 # Data Layer (Repositories, DB, API)
├── service/              # Background Services
├── util/                 # Utilities & Constants
└── FaceRecognitionApp.kt # Application Entry Point
```

## Getting Started

### Prerequisites

- Android Studio Giraffe or later
- JDK 17+
- Firebase Project Setup
- Google Cloud Project for ML Kit

### Installation

1. Clone the repository
```bash
git clone https://github.com/preethamdev05/face-recognition-attendance.git
cd face-recognition-attendance
```

2. Configure Firebase
- Download `google-services.json` from Firebase Console
- Place it in `app/` directory

3. Build & Run
```bash
./gradlew build
./gradlew installDebug
```

## Architecture

Follows Clean Architecture + MVVM pattern:
- **Presentation Layer**: UI with ViewModels
- **Domain Layer**: Business logic with Use Cases
- **Data Layer**: Repositories with Firebase & Room
- **Error Handling**: Sealed Result<T> class
- **Async Operations**: Kotlin Coroutines

## Key Components

### Authentication
- Email/Password Login
- Phone Number Authentication
- Two-Factor Authentication (OTP)
- Biometric Authentication (Fingerprint)

### Student Registration
- Multi-angle Face Capture
- Image Validation (Size, Quality)
- Liveness Detection
- Facial Encoding Generation

### Attendance Marking
- Real-time Camera Feed (30fps)
- Face Detection & Recognition
- Location Tracking (GPS)
- Confidence Scoring
- Duplicate Prevention (5-min window)

### Dashboard
- Attendance Summary (Present/Late/Absent)
- Analytics & Reports
- PDF/Excel Export
- Trend Analysis

## Database Schema

```
attendance-system/
├── students/{studentId}/     # Student profiles
├── attendance/{classId}/{date}/{studentId}/ # Records
├── classes/{classId}/        # Class information
└── admins/{adminId}/         # Admin accounts
```

## Testing

```bash
# Unit Tests
./gradlew test

# Integration Tests
./gradlew connectedAndroidTest

# Specific Test
./gradlew test --tests TestClassName
```

## Performance Metrics

- Image Compression: 70% reduction
- Face Detection: <500ms per frame
- Attendance Marking: <2 seconds
- Batch Upload: 50 records per transaction
- Pagination: 50 records per page

## Security

- AES-256 Encryption at Rest
- HTTPS-only Firebase Connections
- API Key Restrictions
- Firebase Security Rules
- Audit Logging
- GDPR Compliance
- Biometric Data Consent

## Contributing

Fork → Create Branch → Make Changes → Push → Pull Request

## License

MIT License - See LICENSE file

## Support

For issues, contact: support@attendance-system.dev
