package com.janz.userservice.infrastructure.dto

import java.time.LocalDateTime

data class UserResponseDTO(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthday: LocalDateTime?,
    val username: String,
)
