package com.example.adaxintegra.domain.common

//R-02: the backend repeats the same rules as the form and answers which field failed,
// so the screen can mark each input instead of showing one message
class FieldValidationException (
    val fieldErrors: Map<String, String>,
    message: String,
): Exception(message)

