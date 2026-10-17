package com.example.adaxintegra.domain.common

object PasswordValidator {
    private val specialCharacterPattern = Regex("[\\p{P}\\p{S}]")

    fun hasSpecialCharacter(password: String): Boolean {
        return specialCharacterPattern.containsMatchIn(password)
    }
}
