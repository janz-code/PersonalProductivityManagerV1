package com.janz.userservice.application.services

import com.janz.userservice.application.ports.driven.repositories.RoleRepository
import com.janz.userservice.infrastructure.entities.RoleEntity
import org.springframework.stereotype.Service

@Service
class RoleService (private val roleRepository: RoleRepository){
    fun getOrCreate(name: String): RoleEntity {
        val role = roleRepository.findByName(name);
        if(role!=null) return role;
        val temp = RoleEntity(name=name);
        return roleRepository.save(temp);
    }
}