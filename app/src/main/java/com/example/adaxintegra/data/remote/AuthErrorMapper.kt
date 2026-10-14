package com.example.adaxintegra.data.remote

import com.example.adaxintegra.data.remote.dto.ApiErrorDto
import com.example.adaxintegra.domain.common.AuthError
import com.google.gson.Gson
import java.io.IOException
import retrofit2.HttpException

private val gson = Gson()

// Turn any exception from Retrofit into aun AuthError, reading the "code"
// field the backend sends instead of guessing from the message text

fun Throwable.toAuthError(): AuthError = when (this){
    is AuthError -> this
    is IOException -> AuthError.Network
    is HttpException -> {
        val body = try {
            response()?.errorBody()?.string()?.let {
                gson.fromJson(it, ApiErrorDto::class.java)
            }
        } catch (_: Exception) {
            null
        }

        when (body?.code) {
            "INVALID_CREDENTIALS" -> AuthError.InvalidCredentials
            "EMAIL_NOT_VERIFIED" -> AuthError.EmailNotVerified
            "EMAIL_TAKEN" -> AuthError.EmailTaken
            "CODE_INVALID" -> AuthError.CodeInvalid
            "CODE_EXPIRED" -> AuthError.CodeExpired
            "ALREADY_VERIFIED" -> AuthError.AlreadyVerified
            "RESEND_TOO_SOON" -> AuthError.ResendTooSoon
            "TOO_MANY_SENDS" -> AuthError.TooManySends
            "TOO_MANY_ATTEMPTS" -> AuthError.TooManyAttempts
            "VALIDATION_ERROR" -> AuthError.Validation(body.error)
            else -> when (code()){
                // express-rate-limit answers 429 without a code
                429 -> AuthError.TooManyRequests
                else -> AuthError.Unknown(body?.error)
            }
        }
    }
    else -> AuthError.Unknown(message)
}
