package com.example.adaxintegra.domain.common

//states for API responses
sealed class Result<out T> {
    object Loading : Result<Nothing>()

    data class Success<T>(
        val data: T,
    ) : Result<T>()

    data class Error(
        val exception: Throwable,
    ) : Result<Nothing>()
}
