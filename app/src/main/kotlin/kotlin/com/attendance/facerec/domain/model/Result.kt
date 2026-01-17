package com.attendance.facerec.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    inline fun <R> mapSuccess(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        Loading -> Loading
    }

    suspend inline fun <R> mapAsync(transform: suspend (T) -> R): Result<R> = when (this) {
        is Success -> try {
            Success(transform(data))
        } catch (e: Exception) {
            Error(e)
        }
        is Error -> this
        Loading -> Loading
    }

    inline fun onSuccess(action: (T) -> Unit): Result<T> = apply {
        if (this is Success) action(data)
    }

    inline fun onError(action: (Exception) -> Unit): Result<T> = apply {
        if (this is Error) action(exception)
    }

    inline fun onLoading(action: () -> Unit): Result<T> = apply {
        if (this is Loading) action()
    }

    suspend inline fun onEach(
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend (Exception) -> Unit = {},
        onLoading: suspend () -> Unit = {}
    ): Result<T> {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception)
            Loading -> onLoading()
        }
        return this
    }

    fun getOrNull(): T? = (this as? Success)?.data
    fun exceptionOrNull(): Exception? = (this as? Error)?.exception
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading
}

fun <T> Result<T>.getOrElse(fallback: T): T = getOrNull() ?: fallback
fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error -> throw exception
    Result.Loading -> throw IllegalStateException("Result is in Loading state")
}

suspend inline fun <T, R> Result<T>.flatMap(transform: suspend (T) -> Result<R>): Result<R> =
    when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
        Result.Loading -> Result.Loading
    }

fun <T> resultOf(block: () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Error(e)
}

suspend fun <T> suspendResultOf(block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Error(e)
}
