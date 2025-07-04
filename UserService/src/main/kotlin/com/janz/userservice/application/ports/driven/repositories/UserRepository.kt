package com.janz.userservice.application.ports.driven.repositories

import com.janz.userservice.infrastructure.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID>{
    fun findUserByEmail(email: String): UserEntity?
    fun findUserByUsername(userName: String): UserEntity?
}