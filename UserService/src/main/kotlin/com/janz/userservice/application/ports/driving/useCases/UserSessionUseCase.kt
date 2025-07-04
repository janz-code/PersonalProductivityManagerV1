package com.janz.userservice.application.ports.driving.useCases

import com.janz.userservice.infrastructure.dto.CreateUserDTO
import com.janz.userservice.infrastructure.dto.LoginDTO
import com.janz.userservice.infrastructure.dto.LoginResponseDTO

interface UserSessionUseCase {
    suspend fun registerNewUser(user: CreateUserDTO)
    suspend fun readAnUserById(userId: Int)
    suspend fun loginAnUser(user: LoginDTO): LoginResponseDTO
}