package com.costafot.stickers.result

sealed class Result<out L, out R> {
    class Error<L>(val value: L) : Result<L, Nothing>()
    class Success<R>(val value: R) : Result<Nothing, R>()
}

fun <L, R> Result<L, R>.isSuccess(): Boolean = this is Result.Success
fun <L, R> Result<L, R>.isError(): Boolean = this is Result.Error

fun <R> R.success() = Result.Success(this)
fun <L> L.error() = Result.Error(this)

fun <L, R, R1> Result<L, R>.map(f: (R) -> R1): Result<L, R1> = when (this) {
    is Result.Success -> f(value).success()
    is Result.Error -> value.error()
}

inline fun <L, R, R1> Result<L, R>.flatMap(f: (R) -> Result<L, R1>): Result<L, R1> = when (this) {
    is Result.Success -> f(value)
    is Result.Error -> value.error()
}

inline fun <L, R, R1> Result<L, R>.fold(ifError: (L) -> R1, ifSuccess: (R) -> R1): R1 =
    when (this) {
        is Result.Error -> ifError(value)
        is Result.Success -> ifSuccess(value)
    }

fun <L, R, L1> Result<L, R>.mapError(f: (L) -> L1): Result<L1, R> = when (this) {
    is Result.Success -> value.success()
    is Result.Error -> f(value).error()
}

inline fun <R> attempt(f: () -> R): Result<Throwable, R> = try {
    f().success()
} catch (error: Throwable) {
    error.error()
}

inline fun <L, R> attempt(errorMapper: (Throwable) -> L, f: () -> R): Result<L, R> = try {
    f().success()
} catch (error: Throwable) {
    errorMapper(error).error()
}
