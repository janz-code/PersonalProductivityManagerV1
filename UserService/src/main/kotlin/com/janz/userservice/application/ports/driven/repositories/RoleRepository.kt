package com.janz.userservice.application.ports.driven.repositories

import com.janz.userservice.infrastructure.entities.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Int> {
    fun findByName(name: String): RoleEntity?
    fun save(role: RoleEntity): RoleEntity
}