# Contributing to Face Recognition Attendance System

Thank you for your interest in contributing to the Face Recognition Attendance System! This document provides guidelines and standards for contributing to this project.

## Table of Contents

- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Code of Conduct](#code-of-conduct)

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio Giraffe (2022.3.1) or later**
- **JDK 17** (Oracle JDK or OpenJDK)
- **Kotlin 1.9.20+**
- **Gradle 8.0+** (handled by Gradle Wrapper)
- **Git** for version control

### Environment Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/preethamdev05/face-recognition-attendance.git
   cd face-recognition-attendance
   ```

2. **Create `local.properties`:**
   ```bash
   cp local.properties.example local.properties
   ```
   
   Edit `local.properties` and add your Firebase credentials:
   ```properties
   firebase.database.url=https://your-project.firebaseio.com
   firebase.storage.bucket=your-project.appspot.com
   sdk.dir=/path/to/Android/sdk
   ```

3. **Install Git hooks:**
   ```bash
   cp pre-commit .git/hooks/pre-commit
   chmod +x .git/hooks/pre-commit
   ```

4. **Sync project dependencies:**
   Open the project in Android Studio and let Gradle sync.

5. **Run static analysis checks:**
   ```bash
   ./gradlew ktlintCheck detekt
   ```

## Development Workflow

### 1. Fork the Repository

Click the "Fork" button on the GitHub repository page to create your own copy.

### 2. Create a Feature Branch

Always create a new branch for your work. Use descriptive branch names:

```bash
# For new features
git checkout -b feat/add-biometric-auth

# For bug fixes
git checkout -b fix/camera-permission-crash

# For documentation
git checkout -b docs/update-architecture-guide

# For refactoring
git checkout -b refactor/optimize-face-detection

# For tests
git checkout -b test/add-repository-tests
```

### 3. Make Your Changes

- Write clean, readable code
- Follow the coding standards (see below)
- Add tests for new features
- Update documentation as needed

### 4. Test Your Changes

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run static analysis
./gradlew ktlintCheck detekt

# Build release APK
./gradlew assembleRelease
```

### 5. Commit Your Changes

Follow the commit guidelines (see below):

```bash
git add .
git commit -m "feat: add biometric authentication support"
```

### 6. Push to Your Fork

```bash
git push origin feat/add-biometric-auth
```

### 7. Open a Pull Request

- Go to the original repository
- Click "New Pull Request"
- Select your fork and branch
- Fill out the PR template completely
- Wait for review and address feedback

## Coding Standards

### Architecture

This project follows **Clean Architecture + MVVM** pattern:

```
Presentation Layer (UI)
    ├── Activities/Fragments
    ├── ViewModels (StateFlow)
    └── UI State (Result<T>)

Domain Layer (Business Logic)
    ├── Models
    ├── Use Cases
    └── Repository Interfaces

Data Layer (Data Sources)
    ├── Local (Room Database)
    ├── Remote (Firebase)
    └── Repository Implementations
```

### Code Style

**Follow the project's static analysis rules:**

1. **Ktlint:** All Kotlin code must pass `ktlintCheck`
   ```bash
   ./gradlew ktlintFormat  # Auto-format code
   ./gradlew ktlintCheck   # Verify formatting
   ```

2. **Detekt:** Follow all rules defined in `detekt.yml`
   - **Complexity:** Functions must be < 20 lines, conditions < 3 complexity
   - **Naming:** camelCase for functions/variables, UPPER_SNAKE_CASE for constants
   - **Potential Bugs:** No unsafe casts, avoid excessive `lateinit`
   ```bash
   ./gradlew detekt
   ```

### Best Practices

#### Kotlin Conventions

```kotlin
// ✅ GOOD: Immutable by default
val studentName: String = "John Doe"
val attendanceList: List<Attendance> = emptyList()

// ❌ BAD: Mutable without reason
var studentName: String = "John Doe"
var attendanceList: MutableList<Attendance> = mutableListOf()

// ✅ GOOD: Use data classes for models
data class Student(
    val id: String,
    val name: String,
    val email: String
)

// ✅ GOOD: Use sealed classes for states
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// ✅ GOOD: Use coroutines for async work
suspend fun markAttendance(studentId: String): Result<Unit> {
    return withContext(Dispatchers.IO) {
        try {
            attendanceRepository.mark(studentId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

#### Dependency Injection (Hilt)

```kotlin
// ✅ GOOD: Constructor injection
@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: AttendanceRepository
) : ViewModel()

// ❌ BAD: Manual instantiation
class AttendanceViewModel : ViewModel() {
    private val repository = AttendanceRepositoryImpl()
}
```

#### Error Handling

```kotlin
// ✅ GOOD: Proper error handling with Result
viewModelScope.launch {
    _state.value = Result.Loading
    _state.value = try {
        val data = repository.getData()
        Result.Success(data)
    } catch (e: Exception) {
        Result.Error(e)
    }
}

// ❌ BAD: Silent failures
try {
    repository.getData()
} catch (e: Exception) {
    // Do nothing
}
```

### Documentation

- Add KDoc comments for public APIs:
  ```kotlin
  /**
   * Marks attendance for a student using face recognition.
   *
   * @param studentId The unique identifier of the student
   * @param faceImage Bitmap of the captured face
   * @return Result indicating success or failure
   * @throws SecurityException if device is rooted
   */
  suspend fun markAttendance(studentId: String, faceImage: Bitmap): Result<Unit>
  ```

- Update `ARCHITECTURE.md` for architectural changes
- Update `README.md` for user-facing changes

## Commit Guidelines

We follow **Conventional Commits** specification:

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat:** New feature for the user
- **fix:** Bug fix for the user
- **docs:** Documentation changes
- **style:** Code style changes (formatting, missing semicolons, etc.)
- **refactor:** Code change that neither fixes a bug nor adds a feature
- **perf:** Performance improvements
- **test:** Adding or updating tests
- **chore:** Maintenance tasks, dependency updates
- **ci:** CI/CD configuration changes
- **build:** Build system changes
- **revert:** Reverting a previous commit

### Examples

```bash
# Feature
feat(auth): add biometric authentication support

# Bug fix
fix(camera): resolve permission crash on Android 13

# Documentation
docs(readme): update Firebase setup instructions

# Refactoring
refactor(face-detection): optimize ML model inference

# Performance
perf(database): add indexing to attendance queries

# Test
test(repository): add unit tests for AttendanceRepository

# Chore
chore(deps): update Firebase BOM to 32.8.0

# Security
security(proguard): tighten obfuscation rules
```

### Scope (Optional)

Scope indicates the area of the codebase:

- `auth` - Authentication
- `face-detection` - Face recognition
- `database` - Room/Firebase database
- `ui` - User interface
- `camera` - CameraX integration
- `analytics` - Firebase Analytics
- `deps` - Dependencies

### Rules

1. Use lowercase for type and scope
2. Subject should be imperative, present tense ("add" not "added")
3. Subject should not end with a period
4. Keep subject under 50 characters
5. Body is optional but recommended for complex changes
6. Footer can reference issues: `Fixes #123` or `Closes #456`

### Pre-commit Hook

The installed Git hook automatically runs:
- `ktlintCheck` - Code formatting
- `detekt` - Static analysis

If any check fails, the commit is blocked. Fix issues before committing.

## Pull Request Process

### Before Opening a PR

1. **Update your branch:**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Run all checks:**
   ```bash
   ./gradlew ktlintCheck detekt test
   ```

3. **Verify build:**
   ```bash
   ./gradlew assembleRelease
   ```

### PR Requirements

- [ ] All tests pass
- [ ] Code follows style guidelines (Ktlint + Detekt)
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No new warnings introduced
- [ ] Descriptive title using conventional commit format
- [ ] PR template filled out completely

### Review Process

1. **Automated Checks:** GitHub Actions will run tests and static analysis
2. **Peer Review:** At least one maintainer must approve
3. **Address Feedback:** Respond to all review comments
4. **Squash Commits:** Maintainer will squash before merging

### After Merge

Your contribution will be included in the next release. Thank you!

## Code of Conduct

### Our Standards

- Be respectful and inclusive
- Accept constructive criticism gracefully
- Focus on what is best for the community
- Show empathy towards other community members

### Unacceptable Behavior

- Harassment, discrimination, or offensive comments
- Trolling, insulting, or derogatory comments
- Publishing others' private information
- Other conduct which could reasonably be considered inappropriate

### Enforcement

Violations may result in:
1. Warning
2. Temporary ban from repository
3. Permanent ban from repository

Report violations to: support@attendance-system.dev

## Questions?

If you have questions about contributing:

- Open a [Discussion](https://github.com/preethamdev05/face-recognition-attendance/discussions)
- Email: support@attendance-system.dev

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
