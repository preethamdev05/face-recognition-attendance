# Architecture Documentation

## Clean Architecture + MVVM Pattern

### Layers

#### Presentation Layer
- **Activities & Fragments**: UI components
- **ViewModels**: State management with StateFlow
- **UI State**: Result<T> sealed class for type-safe handling

#### Domain Layer
- **Models**: Data classes and enums
- **Use Cases**: Business logic (future expansion)
- **Repository Interfaces**: Abstraction contracts

#### Data Layer
- **Local**: Room Database with DAOs
- **Remote**: Firebase services
- **Repositories**: Implementation with error handling

### Key Patterns

#### Result Sealed Class
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

#### StateFlow for Reactive State
```kotlin
private val _state = MutableStateFlow<Result<T>>(Result.Loading)
val state: StateFlow<Result<T>> = _state.asStateFlow()
```

#### Coroutine-based Async
- Dispatchers for background work
- withContext for thread switching
- Flow for reactive streams

### Dependency Injection

Hilt modules for:
- Firebase service instances
- Repository implementations
- Room database
- Service instances

## Error Handling Strategy

1. **Try-Catch**: Wrap suspending functions
2. **Result<T>**: Type-safe error propagation
3. **Firebase Errors**: Mapped to user-friendly messages
4. **Retry Logic**: Exponential backoff for network failures
5. **Logging**: Firebase Crashlytics integration

## Security Architecture

- **Encryption**: AES-256 for sensitive data
- **Firebase Rules**: Role-based access control
- **API Keys**: Restricted to Android package name
- **Session Management**: Token expiry handling
- **Permissions**: Runtime checks for Camera, Location, Storage

## Performance Optimization

- **Image Compression**: 70% size reduction
- **Pagination**: 50 records per page
- **Caching**: Local Room database
- **Batch Operations**: Group Firebase writes
- **Thread Management**: Executor services for face processing

## Testing Strategy

### Unit Tests
- Result sealed class behavior
- Face similarity calculations
- Email/phone validation
- Utility functions

### Integration Tests
- Repository operations
- Database transactions
- Firebase integration (mocked)

### UI Tests
- Espresso for critical flows
- Activity navigation
- ViewModel state transitions

## Offline Architecture

- **Local Caching**: All data cached in Room
- **WorkManager**: Background sync when online
- **Sync Status**: Track pending operations
- **Conflict Resolution**: Last-write-wins strategy

## Monitoring & Analytics

- **Firebase Analytics**: Track user events
- **Crashlytics**: Error tracking
- **Performance Monitoring**: Attendance marking time
- **Custom Events**: Registration, sync, face detection
