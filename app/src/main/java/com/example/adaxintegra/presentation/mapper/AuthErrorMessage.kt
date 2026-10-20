package com.example.adaxintegra.presentation.mapper

import com.example.adaxintegra.domain.common.AuthError

// G-01 / G-02 / G-09: the message the user sees for each auth error
fun Throwable.toAuthMessage(): String = when (this) {
    AuthError.InvalidCredentials -> "Correo o contraseña incorrectos."
    AuthError.EmailNotVerified -> "Debes verificar tu correo antes de iniciar sesión."
    AuthError.EmailTaken -> "Este correo ya está registrado."
    AuthError.CodeInvalid -> "Código incorrecto. Revisa el código e inténtalo de nuevo."
    AuthError.CodeExpired -> "El código expiró. Solicita uno nuevo."
    AuthError.AlreadyVerified -> "Tu cuenta ya está verificada. Inicia sesión."
    AuthError.ResendTooSoon -> "Espera un momento antes de pedir otro código."
    AuthError.TooManySends -> "Alcanzaste el límite de reenvíos. Intenta más tarde."
    AuthError.TooManyAttempts -> "Demasiados intentos. Solicita un código nuevo."
    AuthError.TooManyRequests -> "Demasiados intentos. Espera unos minutos e inténtalo de nuevo."
    AuthError.Network -> "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."
    is AuthError.Validation -> "Revisa los datos ingresados."
    is AuthError.Unknown -> "Ocurrió un error. Inténtalo de nuevo."
    else -> "Ocurrió un error. Inténtalo de nuevo."
}
