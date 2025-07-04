package com.janz.userservice.application.mappers

import com.janz.userservice.infrastructure.dto.CreateUserDTO
import com.janz.userservice.infrastructure.dto.UserResponseDTO
import com.janz.userservice.infrastructure.entities.RoleEntity
import com.janz.userservice.infrastructure.entities.UserEntity

object UserMapper {
    suspend fun CreateUserDTO.toEntity(
        user: CreateUserDTO, password: String, role: RoleEntity, username: String
    ): UserEntity {
        return UserEntity(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = password,
            active = true,
            role = role,
            username = username
        )
    }
    suspend fun UserEntity.toResponse(user: UserEntity): UserResponseDTO{
        return UserResponseDTO(
            id = user.id.toString(),
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            birthday = user.birthday,
            username = user.username,
        )
    }
}