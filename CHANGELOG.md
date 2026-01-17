# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial project setup
- Face recognition using ML Kit and TensorFlow Lite
- Attendance tracking with Firebase Realtime Database
- Clean Architecture + MVVM implementation
- Hilt dependency injection
- Room database for local caching
- WorkManager for background sync
- Security features:
  - Root and emulator detection
  - AES-256-GCM encryption
  - Network security config (HTTPS-only)
  - ProGuard/R8 obfuscation
- CI/CD workflows:
  - Automated testing on PR
  - Code quality checks (Ktlint, Detekt)
  - Release automation
  - Dependabot for dependency updates
- Comprehensive documentation:
  - README with setup instructions
  - CONTRIBUTING guide
  - SECURITY policy
  - ARCHITECTURE documentation
- GitHub templates:
  - Bug report template
  - Feature request template
  - Pull request template

### Security
- Implemented BuildConfig secrets management
- Tightened ProGuard rules for better obfuscation
- Added Firebase security rules
- Enabled network security configuration

## [1.0.0] - 2026-01-17

### Added
- Initial release
- Core face recognition functionality
- Attendance marking and tracking
- Admin dashboard
- Offline support with background sync
- Biometric authentication
- Material Design 3 UI

[Unreleased]: https://github.com/preethamdev05/face-recognition-attendance/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/preethamdev05/face-recognition-attendance/releases/tag/v1.0.0
