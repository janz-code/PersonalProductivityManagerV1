package com.janz.userservice.infrastructure.dto

data class LoginResponseDTO(
    val token: String,
    val user: UserResponseDTO
)
