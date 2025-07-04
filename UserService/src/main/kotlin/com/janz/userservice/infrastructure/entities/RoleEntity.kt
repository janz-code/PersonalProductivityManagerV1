package com.janz.userservice.infrastructure.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "roles")
@Entity()
data class RoleEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(length = 32, unique = true, nullable = false)
    var name: String,
    @Column(nullable = true)
    var description: String? = "no description"
)