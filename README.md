# Face Recognition Attendance System

[![CI](https://github.com/preethamdev05/face-recognition-attendance/actions/workflows/ci.yml/badge.svg)](https://github.com/preethamdev05/face-recognition-attendance/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-21%2B-green.svg)](https://developer.android.com)

A production-grade Android application for automated attendance tracking using face recognition technology. Built with Clean Architecture, MVVM, and modern Android development best practices.

## ✨ Features

### Core Functionality
- **Face Recognition**: Real-time face detection and recognition using ML Kit and TensorFlow Lite
- **Attendance Tracking**: Automated attendance marking with timestamp and location
- **Multi-face Detection**: Support for multiple students in a single frame
- **Offline Support**: Local caching with background sync using WorkManager
- **Admin Dashboard**: Comprehensive reports and analytics
- **Biometric Authentication**: Fingerprint/face unlock for secure access

### Security
- Root and emulator detection with graceful UX handling
- AES-256-GCM encryption for sensitive data
- Network security config (HTTPS-only)
- ProGuard/R8 obfuscation for release builds
- Firebase security rules with role-based access control

### Technical Highlights
- **Clean Architecture + MVVM**: Separation of concerns with testable code
- **Kotlin Coroutines & Flow**: Reactive programming for async operations
- **Hilt Dependency Injection**: Type-safe DI framework
- **Room Database**: Local persistence with migrations
- **CameraX**: Modern camera API with lifecycle awareness
- **Material Design 3**: Modern UI/UX following Material guidelines

## 📱 Screenshots

> Add screenshots here

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (Activities, Fragments, ViewModels)    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│          Domain Layer                   │
│    (Models, Use Cases, Repositories)    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│           Data Layer                    │
│   (Room DB, Firebase, Repositories)     │
└─────────────────────────────────────────┘
```

See [ARCHITECTURE.md](ARCHITECTURE.md) for detailed documentation.

## 🚀 Getting Started

### Prerequisites

- **Android Studio Giraffe (2022.3.1)** or later
- **JDK 17** (Oracle JDK or OpenJDK)
- **Android SDK 34**
- **Gradle 8.0+** (included via wrapper)
- **Firebase Project** (for backend services)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/preethamdev05/face-recognition-attendance.git
   cd face-recognition-attendance
   ```

2. **Create Firebase project:**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project
   - Add an Android app with package name: `com.attendance.facerec`
   - Download `google-services.json` and place it in `app/` directory

3. **Configure secrets:**
   ```bash
   cp local.properties.example local.properties
   ```
   
   Edit `local.properties` and add your Firebase credentials:
   ```properties
   firebase.database.url=https://your-project-id.firebaseio.com
   firebase.storage.bucket=your-project-id.appspot.com
   sdk.dir=/path/to/Android/sdk
   ```

4. **Install Git hooks:**
   ```bash
   cp pre-commit .git/hooks/pre-commit
   chmod +x .git/hooks/pre-commit
   ```

5. **Sync and build:**
   ```bash
   ./gradlew build
   ```

6. **Run on device/emulator:**
   ```bash
   ./gradlew installDebug
   ```

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Run Static Analysis
```bash
# Ktlint (code formatting)
./gradlew ktlintCheck
./gradlew ktlintFormat  # Auto-fix

# Detekt (static analysis)
./gradlew detekt
```

### Security Verification
```bash
chmod +x verify_phase2.sh
./verify_phase2.sh
```

## 📦 Building for Production

### 1. Create Keystore
```bash
keytool -genkey -v -keystore keystore.jks -keyalg RSA \
  -keysize 2048 -validity 10000 -alias attendance-key
```

### 2. Build Release APK
```bash
./gradlew assembleRelease
```

### 3. Sign APK
```bash
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore keystore.jks app/build/outputs/apk/release/app-release-unsigned.apk \
  attendance-key

zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/app-release-signed.apk
```

See [DEPLOYMENT.md](DEPLOYMENT.md) for complete deployment guide.

## 🤝 Contributing

We welcome contributions! Please read our [CONTRIBUTING.md](CONTRIBUTING.md) for details on:

- Development workflow
- Coding standards (Ktlint + Detekt)
- Commit message conventions (Conventional Commits)
- Pull request process

### Quick Start for Contributors

```bash
# Fork and clone
git clone https://github.com/YOUR_USERNAME/face-recognition-attendance.git

# Create feature branch
git checkout -b feat/your-feature-name

# Make changes and commit
git commit -m "feat: add your feature"

# Push and create PR
git push origin feat/your-feature-name
```

## 🔒 Security

Security is a top priority. Please read our [SECURITY.md](SECURITY.md) for:

- Supported versions
- Reporting vulnerabilities (email: support@attendance-system.dev)
- Security best practices

**Do not open public issues for security vulnerabilities.**

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [ML Kit](https://developers.google.com/ml-kit) for face detection
- [TensorFlow Lite](https://www.tensorflow.org/lite) for face recognition models
- [Firebase](https://firebase.google.com/) for backend services
- [Android Jetpack](https://developer.android.com/jetpack) for modern Android components

## 📞 Contact

- **Email:** support@attendance-system.dev
- **GitHub Issues:** [Create an issue](https://github.com/preethamdev05/face-recognition-attendance/issues/new/choose)
- **Discussions:** [Join the discussion](https://github.com/preethamdev05/face-recognition-attendance/discussions)

## 📊 Project Status

- ✅ Core face recognition functionality
- ✅ Attendance tracking and reporting
- ✅ Admin dashboard
- ✅ Offline support with sync
- ✅ Security hardening
- 🚧 Play Store release (planned)
- 🚧 Multi-language support (planned)
- 🚧 Dark mode (planned)

## 🗺️ Roadmap

### v1.1.0 (Q2 2026)
- [ ] Dark mode support
- [ ] Multi-language (Hindi, Tamil, Telugu)
- [ ] Export reports to PDF/Excel
- [ ] QR code attendance fallback

### v1.2.0 (Q3 2026)
- [ ] Wear OS companion app
- [ ] NFC attendance support
- [ ] Advanced analytics dashboard
- [ ] Notification system for admins

### v2.0.0 (Q4 2026)
- [ ] Cloud-based face recognition (scalability)
- [ ] Multi-tenant support
- [ ] REST API for integrations
- [ ] Web admin portal

---

**Made with ❤️ by Preetham**

**Star ⭐ this repository if you find it useful!**
