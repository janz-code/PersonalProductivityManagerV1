package com.janz.userservice.infrastructure.adapters.driving.controllers

import com.janz.userservice.application.ports.driving.useCases.UserSessionUseCase
import com.janz.userservice.infrastructure.configurations.entities.ApiResponse
import com.janz.userservice.infrastructure.configurations.utils.ResponseUtil
import com.janz.userservice.infrastructure.dto.CreateUserDTO
import com.janz.userservice.infrastructure.dto.LoginDTO
import com.janz.userservice.infrastructure.dto.LoginResponseDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public")
class PublicController(private val userSessionUseCase: UserSessionUseCase) {

    @PostMapping("/create-user")
    suspend fun createUser(@Valid @RequestBody user: CreateUserDTO): ResponseEntity<ApiResponse<Nothing>> {
        userSessionUseCase.registerNewUser(user);
        return ResponseUtil.success(message = "User successfully created", status = HttpStatus.CREATED);
    }

    @PostMapping("/login")
    suspend fun login(@Valid @RequestBody user: LoginDTO): ResponseEntity<ApiResponse<LoginResponseDTO>> {
        val userResponse = userSessionUseCase.loginAnUser(user);
        return ResponseUtil.success(message = "User successfully logged in", status = HttpStatus.OK, data = userResponse);
    }
}
