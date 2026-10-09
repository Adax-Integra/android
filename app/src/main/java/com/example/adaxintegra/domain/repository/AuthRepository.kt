package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    // Expresa los cambios en la sesión del usuario actual
    val session: StateFlow<UserSession?>

    // Inicio de sesión (con validación de correo confirmado)
    fun login(
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // Cierra la sesión activa y local
    fun logout()

    // Registro de nuevo usuario
    fun register(
        name: String,
        lastname: String,
        phone: String,
        email: String,
        password: String,
    ): Flow<Result<UserSession>>

    // Reenvío de correo de verificación OTP
    fun resendVerificationEmail(email: String): Flow<Result<Unit>>

    // Verificación de código OTP enviado por correo
    fun verifyEmailCode(
        email: String,
        code: String,
    ): Flow<Result<UserSession>>

    // Solicitud de recuperación de contraseña (forgot password)
    suspend fun forgotPassword(email: String): Result<Unit>

    // Restablecimiento de contraseña
    suspend fun resetPassword(newPassword: String): Result<Unit>

    // Cambio de contraseña para usuario autenticado
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Result<String>>
}
