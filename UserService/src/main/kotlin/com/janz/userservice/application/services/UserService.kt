package com.janz.userservice.application.services

import com.janz.userservice.application.ports.driven.repositories.UserRepository
import com.janz.userservice.infrastructure.dto.CreateUserDTO
import com.janz.userservice.infrastructure.entities.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository, private val passwordEncoder: PasswordEncoder) {
    suspend fun emailExists(email: String): Boolean {
        val user = repository.findUserByEmail(email);
        return user != null;
    }
    suspend fun findUserByEmail(email: String): UserEntity {
        return repository.findUserByEmail(email) ?: throw Exception("User not found");
    }
    suspend fun saveUser(user: UserEntity) {
        repository.save<UserEntity>(user);
    }
    suspend fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password);
    }
    suspend fun verifyPassword(password: String, encryptedPassword: String): Boolean {
        return passwordEncoder.matches(password, encryptedPassword);
    }
    suspend fun generateUsername(firstName: String): String{
        val sanitizedName = firstName
            .lowercase()
            .replace("\\s+".toRegex(), "")
        val randomNumber = (1000..9999).random()
        return "${sanitizedName}${randomNumber}"
    }
}