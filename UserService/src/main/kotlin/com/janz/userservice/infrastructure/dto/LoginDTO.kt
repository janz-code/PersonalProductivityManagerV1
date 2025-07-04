package com.janz.userservice.infrastructure.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class LoginDTO(
    @field:NotBlank(message = "email is required")
    @field:Email(message = "Email address contains invalid characters")
    val email: String,
    @field:NotBlank(message = "Password is required")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
        message = "Password must be 8-16 characters, include a letter, a number, and a special character"
    )
    val password: String,
)
