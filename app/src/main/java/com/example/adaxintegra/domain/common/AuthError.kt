package com.example.adaxintegra.domain.common

import com.example.adaxintegra.domain.repository.AuthRepository

// G-01/ G-02 / G-09: errors the auth flows can end with.

// Message for each one so the domain does not depend on UI texts

sealed class AuthError : Exception(){
    data object InvalidCredentials : AuthError()
    data object EmailNotVerified : AuthError()
    data object EmailTaken : AuthError()
    data object CodeInvalid : AuthError()
    data object CodeExpired : AuthError()
    data object AlreadyVerified : AuthError()
    data object ResendTooSoon : AuthError()
    data object TooManySends : AuthError()
    data object TooManyAttempts : AuthError()

    // rate limit from the server
    data object TooManyRequests : AuthError()
    data object Network : AuthError()

    // the server rejected the data; serverMessage helps when debugging
    data class Validation(val serverMessage: String?) : AuthError()
    data class Unknown(val serverMessage: String?) : AuthError()
}
