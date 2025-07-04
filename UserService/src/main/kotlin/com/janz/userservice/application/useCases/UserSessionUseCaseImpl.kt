package com.janz.userservice.application.useCases

import com.janz.userservice.application.mappers.UserMapper.toEntity
import com.janz.userservice.application.mappers.UserMapper.toResponse
import com.janz.userservice.application.ports.driving.useCases.UserSessionUseCase
import com.janz.userservice.application.services.RoleService
import com.janz.userservice.application.services.UserService
import com.janz.userservice.infrastructure.configurations.JwtSupport
import com.janz.userservice.infrastructure.dto.CreateUserDTO
import com.janz.userservice.infrastructure.dto.LoginDTO
import com.janz.userservice.infrastructure.dto.LoginResponseDTO
import com.janz.userservice.infrastructure.entities.UserEntity
import org.springframework.stereotype.Service

@Service
class UserSessionUseCaseImpl(
    private val userService: UserService, private val roleService: RoleService,
    private val jwtSupport: JwtSupport
): UserSessionUseCase {
    override suspend fun registerNewUser(user: CreateUserDTO) {
        if(userService.emailExists(user.email)) throw IllegalArgumentException("Email already exists");
        val newPassword = userService.encryptPassword(user.password);
        val role = roleService.getOrCreate("user");
        val userName = userService.generateUsername(user.firstName);
        val newUser: UserEntity = user.toEntity(user, newPassword, role, userName);
        userService.saveUser(newUser);
    }

    override suspend fun readAnUserById(userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun loginAnUser(user: LoginDTO): LoginResponseDTO {
        if(!userService.emailExists(user.email)) throw IllegalArgumentException("Email do not exists");
        val userEntity = userService.findUserByEmail(user.email);
        if(!userService.verifyPassword(user.password, userEntity.password))
            throw IllegalArgumentException("Password do not match");
        val token = jwtSupport.generate(userEntity.username, userEntity.role.name);
        val userResponse = userEntity.toResponse(userEntity);
        return LoginResponseDTO(token=token.value, user=userResponse)
    }
}